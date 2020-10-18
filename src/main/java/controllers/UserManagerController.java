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

    public void initialize(){
        tableColumnName.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<UserModel, String>("status"));
    }

    public void setMaincontroller(Main maincontroller, Stage currentstage) {
        this.mainController = maincontroller;
        this.currentStage = currentstage;
        tableView.setItems(mainController.getTempPersonData());
    }

    public void handleCancel(ActionEvent event) {
        currentStage.close();
    }

    public Dialog<UserModel> getDialog(UserModel editUser){
        String editName="";
        String editSta="";
        if(editUser !=null){
            editName = editUser.getName();
            editSta= editUser.getStatus();
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
                "Child",
                "Guest",
                "Stranger"
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
                mainController.getTempPersonData().add(new UserModel(name.getText(),status.getValue()));
            }
            return null;
        });

        return addDialog;
    }

    public void handleAdd(ActionEvent event) {
        Dialog<UserModel> addDialog = getDialog(null);
        addDialog.showAndWait();
    }

    public void handleEdit(ActionEvent event) {
        UserModel selectedUser = (UserModel) tableView.getSelectionModel().getSelectedItem();
        if(selectedUser !=null){
            Dialog<UserModel> editdialog = getDialog(selectedUser);
            editdialog.showAndWait();
        }
        else{
            errorLabel.setText("Cannot edit");
        }
    }

    public void handleDelete(ActionEvent event) {
        UserModel selectedUser = (UserModel) tableView.getSelectionModel().getSelectedItem();
        if(selectedUser !=null){
            mainController.getTempPersonData().remove(selectedUser);
        }
        else{
            errorLabel.setText("Cannot delete");
        }
    }

    public void handleSave(ActionEvent event) {
        mainController.getPersonData().clear();
        mainController.getTempPersonData().forEach((user)->{
            mainController.getPersonData().add(user);
        });
        currentStage.close();
    }
}
