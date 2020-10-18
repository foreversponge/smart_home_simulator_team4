package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.UserModel;

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
        tableColumnName.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<UserModel, String>("status"));
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
     * @param editUserModel
     * @return
     */
    public Dialog<UserModel> getDialog(UserModel editUserModel){
        String editName="";
        String editSta="";
        if(editUserModel !=null){
            editName = editUserModel.getName();
            editSta= editUserModel.getStatus();
        }
        Dialog<UserModel> addDialog = new Dialog<>();
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
                if(editUserModel !=null){
                    mainController.getTempPersonData().remove(editUserModel);
                }
                mainController.getTempPersonData().add(new UserModel(name.getText(),status.getValue()));
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
        Dialog<UserModel> addDialog = getDialog(null);
        addDialog.showAndWait();
    }
    /**
     * display the Dialog to Edit the existing user
     * @param event
     */
    public void handleEdit(ActionEvent event) {
        UserModel selectedUserModel = (UserModel) tableView.getSelectionModel().getSelectedItem();
        if(selectedUserModel !=null){
            Dialog<UserModel> editdialog = getDialog(selectedUserModel);
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
        UserModel selectedUserModel = (UserModel) tableView.getSelectionModel().getSelectedItem();
        if(selectedUserModel !=null){
            mainController.getTempPersonData().remove(selectedUserModel);
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
        mainController.getTempPersonData().forEach((userModel)->{
            mainController.getPersonData().add(userModel);
        });
        currentStage.close();
    }
}
