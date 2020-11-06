package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * This class acts as a controller for permissionsExplained.fxml
 * It explains to the user the permissions that come with each role
 * @author Team 4
 *
 */
public class PermissionsExplainedController {

    @FXML
    public Button closeButton; //Name of the close button

    /**
     * When user clicks the close button, the permissions stage will be closed
     * @param event user clicks on close button
     */
    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
