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

    public void handleContinueDashboard(ActionEvent event) {
        mainController.CloseWindow();
        try{
            mainController.setDashboardWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
