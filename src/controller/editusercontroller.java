package controller;


import helper.Person;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;



public class editusercontroller {
    public Label errorlabel;
    @FXML private TableView<Person> tableview;
    @FXML private TableColumn<Person, String> tablecolumnname;
    @FXML private TableColumn<Person, String> tablecolumnstatus;

    private Maincontroller maincontroller;
    private Stage currentstage;


    public void initialize(){
        tablecolumnname.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
//        so it set the value of the attribute name of the object person to the column name
        tablecolumnstatus.setCellValueFactory(new PropertyValueFactory<Person, String>("status"));

    }


    public void setMaincontroller(Maincontroller maincontroller, Stage currentstage) {

        this.maincontroller = maincontroller;
        this.currentstage = currentstage;
        // this would call when  we set the scene so it would be run first
        tableview.setItems(maincontroller.getTemppersondata());
    }

    public void handlecancel(ActionEvent event) {

        currentstage.close();


    }

    public Dialog<Person> getdialog(Person editperson){
        String editname="";
        String editlocation="";
        String editsta="";
        if(editperson!=null){
            editname = editperson.getName();
            editsta=editperson.getStatus();
        }
        Dialog<Person> adddialog = new Dialog<>();
        adddialog.setTitle("Add/Edit a person");
        DialogPane dipane = adddialog.getDialogPane();
        dipane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField name = new TextField();
        name.setPromptText("User name");
        name.setText(editname);
//        TextField location = new TextField();
//        location.setPromptText("location");
        Label stat = new Label("Status");
        ComboBox<String> status = new ComboBox<>();
        status.getItems().addAll(
                "Parent",
                "child",
                "guest",
                "stranger"
        );
        status.setValue(editsta);

        GridPane grid = new GridPane();
        grid.add(name, 1, 1);
        grid.add(status, 1, 2);
//        grid.add(location, 1, 3);
        dipane.setContent(grid);
        adddialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                if(editperson!=null){
                    maincontroller.getTemppersondata().remove(editperson);
                    System.out.println("edit user");
                }
                maincontroller.getTemppersondata().add(new Person(name.getText(),status.getValue()));

                //maincontroller.getPersondata().add(new Person(name.getText(),status.getText()));
//                System.out.println(name.getText()+"\t"+location.getText()+"\t"+status.getValue());
                // we should add to obervaable list of perso
            }
            return null;
        });

        return adddialog;
    }


    public void handleadd(ActionEvent event) {
        Dialog<Person> adddialog = getdialog(null);
        adddialog.showAndWait();
    }

    public void handleedit(ActionEvent event) {
        // make use of get select model
        //getSelectionModel().getSelectedItem()
        Person selectedperson = tableview.getSelectionModel().getSelectedItem();
        if(selectedperson !=null){
            Dialog<Person> editdialog = getdialog(selectedperson);
            editdialog.showAndWait();
        }
        else{
            errorlabel.setText("cannot edit");
        }

    }

    public void handledelete(ActionEvent event) {
        Person selectedperson = tableview.getSelectionModel().getSelectedItem();
        if(selectedperson !=null){
            maincontroller.getTemppersondata().remove(selectedperson);
        }
        else{
            errorlabel.setText("cannot delete");
        }

    }


    public void handlesave(ActionEvent event) {
        maincontroller.getPersondata().clear();
        maincontroller.getTemppersondata().forEach((person)->{
            maincontroller.getPersondata().add(person);
        });
        currentstage.close();
    }
}
