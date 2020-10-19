package controllers;

import javafx.event.ActionEvent;
import java.io.IOException;

public class SimulatorParameterController {

    private Main mainController;
    /**
     * store the instance of Main object so we can use the method in the main
     * @param main
     */
    public void setMainController(Main main ) {
        mainController =main;
    }
    /**
     * to display the userManger window so they edit add delete and save
     * @param event
     */
    public void handleEditUser(ActionEvent event) {
        try{
            mainController.setUserManagerWindow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleContinueDashboard(ActionEvent event) {
        mainController.closeWindow();
        try{
            mainController.setDashboardWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
