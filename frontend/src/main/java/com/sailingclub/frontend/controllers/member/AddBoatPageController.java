package com.sailingclub.frontend.controllers.member;

import com.sailingclub.frontend.Helpers;
import com.sailingclub.frontend.memberPages.MemberHomePage;
import com.sailingclub.frontend.paymentType.PaymentTypePage;
import entities.Boat;
import entities.Member;
import entities.StorageFee;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import messageManagement.Message;
import messageManagement.MessageType;
import messageManagement.Reply;
import messageManagement.ReplyType;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class AddBoatPageController {
    AtomicReference<String> ref = new AtomicReference<>("");

    @FXML
    private Button backButton;
    @FXML
    private TextField name;
    @FXML
    private TextField length;
    private Member currentMember;

    /**
     * Initialize FXML components,
     */
    @FXML
    public void initialize(Member currentMember) {
        this.backButton.setStyle("-fx-background-radius: 5em; ");
        this.currentMember = currentMember;
    }

    public void onAddBoatClick(){
        if (!name.getText().isEmpty() && !length.getText().isEmpty()) {
            // let the user choose payment type
            new PaymentTypePage(ref).render();

            Boat boatToAdd = new Boat(name.getText(), currentMember, Double.parseDouble(length.getText()));

            Message<Member> message = Message.newInstance(currentMember, MessageType.ADD_BOAT, boatToAdd);

            try {
                Helpers.getOutputStream().writeObject(message);

                Object o = Helpers.getInputStream().readObject();
                if (o instanceof Reply){
                    Reply reply = (Reply) o;
                    if(reply.getResponseCode() == ReplyType.OK){
                        ArrayList<Serializable> serializableArrayList = reply.getResults();
                        boatToAdd = (Boat) serializableArrayList.get(0);

                        payStorageFee(boatToAdd);
                        new MemberHomePage(currentMember).render();
                    } else if(reply.getResponseCode() == ReplyType.ERROR) {
                        Helpers.showStage("Some error occurred in the Add Boat process");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO BoatDAO insert should return the object with the ID
    // so that we could use it to do other researches
    private void payStorageFee(Boat boat) throws IOException, ClassNotFoundException {
        // create an array of objects to send to backend
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(boat);
        arrayList.add(ref.get());
        Message<Member> message = Message.newInstance(currentMember, MessageType.PAY_STORAGE_FEE, arrayList);

        Helpers.getOutputStream().writeObject(message);

        Object o = Helpers.getInputStream().readObject();
        if (o instanceof Reply) {
            Reply reply = (Reply) o;
            if (reply.getResponseCode() == ReplyType.ERROR) {
                throw new IOException();
            }
        }
    }

    /**
     * Goes back to the previous page
     */
    public void onGoBackClick() throws IOException {
        new MemberHomePage(currentMember).render();
    }
}
