package com.sailingclub.frontend.controllers.employee;

import com.sailingclub.frontend.Helpers;
import com.sailingclub.frontend.employeePages.EmployeeHomePage;
import entities.Boat;
import entities.Employee;
import entities.StorageFee;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import messageManagement.Message;
import messageManagement.MessageType;
import messageManagement.Reply;
import messageManagement.ReplyType;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NotifyStorageFeesPageController {
    Employee currentEmployee;
    ArrayList<Boat> boats;

    @FXML
    private Button backButton;
    @FXML
    private TableView<Boat> tableView;
    @FXML
    private TableColumn<Boat, String> boat;
    @FXML
    private TableColumn<Boat, String> member;

    TableView.TableViewSelectionModel<Boat> selectionModel;

    /**
     * Initialize FXML components,
     */
    @FXML
    public void initialize(Employee employee) {
        this.currentEmployee = employee;
        this.backButton.setStyle("-fx-background-radius: 5em; ");

        this.selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        getAllData();

        initTableView();
    }

    public void onNotifyMemberClick(){
        Boat selectedBoat = selectionModel.getSelectedItem();
        Message<Employee> message = Message.newInstance(currentEmployee, MessageType.NOTIFY_MEMBER_STORAGE_FEES, selectedBoat);

        try {
            Helpers.getOutputStream().writeObject(message);

            Object o = Helpers.getInputStream().readObject();

            if (o instanceof Reply) {
                Reply reply = (Reply) o;
                if (reply.getResponseCode() == ReplyType.OK){
                    tableView.getItems().remove(selectedBoat);
                }
                if (reply.getResponseCode() == ReplyType.ERROR) {
                    Helpers.showStage("Some error occurred while notifying member");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getAllData() {
        Message<Employee> message = Message.newInstance(currentEmployee, MessageType.GET_EMPLOYEE_STORAGE_FEES_TO_PAY);

        try {
            Helpers.getOutputStream().writeObject(message);

            Object o = Helpers.getInputStream().readObject();

            if (o instanceof Reply) {
                Reply reply = (Reply) o;
                if (reply.getResponseCode() == ReplyType.OK) {
                    // make cast of serializable array of response
                    ArrayList<Serializable> serializableArrayList = reply.getResults();
                    boats = (ArrayList<Boat>) serializableArrayList.get(0);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initTableView() {
        // Fill table with data passed by backend
        member.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Boat, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Boat, String> data) {
                if(data.getValue().getOwner().getUsername() != null)
                    return new ReadOnlyStringWrapper(data.getValue().getOwner().getUsername());

                return new ReadOnlyStringWrapper("");
            }
        });

        boat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Boat, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Boat, String> data) {
                if (data.getValue().getName() != null)
                    return new ReadOnlyStringWrapper(data.getValue().getName());

                return new ReadOnlyStringWrapper("");
            }
        });

        if (boats != null)
            tableView.getItems().setAll(boats);
    }

    /**
     * Goes back to the previous page
     */
    public void onGoBackClick() throws IOException {
        new EmployeeHomePage(currentEmployee).render();
    }
}
