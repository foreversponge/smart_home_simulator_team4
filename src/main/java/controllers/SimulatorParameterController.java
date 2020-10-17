package controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class SimulatorParameterController {
    private Main mainController;

    public void setMainController(Main main ) {

        mainController =main;
    }

    public void handleEditUser(ActionEvent event) {
        try{
            mainController.setUserManagerWindow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
