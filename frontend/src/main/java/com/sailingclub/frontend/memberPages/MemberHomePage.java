package com.sailingclub.frontend.memberPages;

import com.sailingclub.frontend.Helpers;
import com.sailingclub.frontend.controllers.member.MemberHomePageController;
import entities.Member;
import javafx.fxml.FXMLLoader;

public class MemberHomePage {
    Member currentMember;

    public MemberHomePage(Member currentMember){
        this.currentMember = currentMember;
    }

    /**
     * Gets the path of the FXML for the current page
     * and attach the resource to the scene
     */
    public void render() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/member/member-home-page.fxml"));
            Helpers.staticScene.setRoot(fxmlLoader.load());

            // pass data from the previous page
            MemberHomePageController memberHomePageController = fxmlLoader.<MemberHomePageController>getController();
            memberHomePageController.initialize(currentMember);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error in MemberHomePage render()");
        }

    }
}
