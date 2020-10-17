package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;

public class UserManagerController {

    @FXML private TableView tableView;
    @FXML private TableColumn tableColumnName;
    @FXML private TableColumn tableColumnStatus;
    @FXML private Label errorLabel;
    private Main mainController;
    private Stage currentStage;
    /**
     * help to set the column of the table view with the attribute from the table list
     */
    public void initialize(){
        tableColumnName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<User, String>("status"));
    }
    /**
     * keep an instance of Main and currentStage
     * set the table View with the Observable list
     * @param maincontroller
     * @param currentstage
     */
    public void setMaincontroller(Main maincontroller, Stage currentstage) {
        this.mainController = maincontroller;
        this.currentStage = currentstage;
        tableView.setItems(mainController.getTempPersonData());
    }
    /**
     * cancel all the action that is done during user Manager window
     * @param event
     */
    public void handleCancel(ActionEvent event) {
        currentStage.close();
    }
    /**
     * helper class to create the Dialog of type User object so it can be use when user want to add or delete user
     * @param editUser
     * @return
     */
    public Dialog<User> getDialog(User editUser){
        String editName="";
        String editSta="";
        if(editUser !=null){
            editName = editUser.getName();
            editSta= editUser.getStatus();
        }
        Dialog<User> addDialog = new Dialog<>();
        addDialog.setTitle("Add/Edit a person");
        DialogPane dialogPane = addDialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField name = new TextField();
        name.setPromptText("User name");
        name.setText(editName);
        Label stat = new Label("Status");
        ComboBox<String> status = new ComboBox<>();
        status.getItems().addAll(
                "Parent",
                "child",
                "guest",
                "stranger"
        );
        status.setValue(editSta);
        GridPane grid = new GridPane();
        grid.add(name, 1, 1);
        grid.add(status, 1, 2);
        dialogPane.setContent(grid);
        addDialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                if(editUser !=null){
                    mainController.getTempPersonData().remove(editUser);
                }
                mainController.getTempPersonData().add(new User(name.getText(),status.getValue()));
            }
            return null;
        });
        return addDialog;
    }
    /**
     * display the Dialog to add User
     * @param event
     */
    public void handleAdd(ActionEvent event) {
        Dialog<User> addDialog = getDialog(null);
        addDialog.showAndWait();
    }
    /**
     * display the Dialog to Edit the existing user
     * @param event
     */
    public void handleEdit(ActionEvent event) {
        User selectedUser = (User) tableView.getSelectionModel().getSelectedItem();
        if(selectedUser !=null){
            Dialog<User> editdialog = getDialog(selectedUser);
            editdialog.showAndWait();
        }
        else{
            errorLabel.setText("cannot edit");
        }
    }
    /**
     * Delete that is selected and remove from the list
     * @param event
     */
    public void handleDelete(ActionEvent event) {
        User selectedUser = (User) tableView.getSelectionModel().getSelectedItem();
        if(selectedUser !=null){
            mainController.getTempPersonData().remove(selectedUser);
        }
        else{
            errorLabel.setText("cannot delete");
        }
    }
    /**
     * Save all the action in the window and update the PersonData observable list
     * @param event
     */
    public void handleSave(ActionEvent event) {
        mainController.getPersonData().clear();
        mainController.getTempPersonData().forEach((user)->{
            mainController.getPersonData().add(user);
        });
        currentStage.close();
    }
}
