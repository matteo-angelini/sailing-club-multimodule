package com.sailingclub.frontend.controllers.member;

import com.sailingclub.frontend.authPages.member.MemberAuthHomePage;
import com.sailingclub.frontend.memberPages.*;
import entities.Member;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MemberHomePageController {
    Member currentMember;

    @FXML
    private Button backButton;

    /**
     * Initialize FXML components,
     */
    @FXML
    public void initialize(Member member) {
        this.currentMember = member;
        this.backButton.setStyle("-fx-background-radius: 5em; ");
    }

    public void onAddBoatClick(){
        new AddBoatPage(currentMember).render();
    }

    public void onRemoveBoatClick() { new RemoveBoatPage(currentMember).render(); }

    public void onPayStorageFeesClick() { new PayStorageFeesPage(currentMember).render(); }

    public void onPayMembershipFeesClick() { new PayMembershipFeesPage(currentMember).render(); }

    public void onRacesClick() { new RegisterToRacePage(currentMember).render(); }

    /**
     * Goes back to the previous page
     */
    public void onGoBackClick() throws IOException {
        new MemberAuthHomePage().render();
    }
}
