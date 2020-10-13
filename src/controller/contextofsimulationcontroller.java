package controller;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import helper.Person;

import helper.Room;
import helper.allRoom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;


public class contextofsimulationcontroller {
    @FXML private JFXTimePicker seltimer;
    @FXML private JFXDatePicker seldate;
    @FXML private JFXComboBox roomlocation;
    @FXML private TableView<Person> alluser;
    @FXML private TableColumn<Person, String> colname;
    @FXML private TableColumn<Person, String> colstatus;

    private Maincontroller maincontroller;


    public void initialize(){
        colname.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        colstatus.setCellValueFactory(new PropertyValueFactory<Person, String>("status"));
        ObservableList<String> name= FXCollections.observableArrayList();
        for(Room r : allRoom.getAllroom()){
            name.add(r.getName());
        }
        roomlocation.setItems(name);

    }
    public void handleedituser(ActionEvent event) {
        try{
            maincontroller.seteditwindow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setMaincontroller(Maincontroller maincontroller) {
        this.maincontroller = maincontroller;
        alluser.setItems(maincontroller.getPersondata());


    }


    public void handletodashboard(ActionEvent event)  {
        Person seletecperson = alluser.getSelectionModel().getSelectedItem();
        if(seldate.getValue() !=null && seltimer.getValue()!=null && seletecperson!=null && roomlocation.getValue()!=null){
            maincontroller.setLoggeduser(seletecperson);
            maincontroller.getLoggeduser().setDate(seldate.getValue());
            maincontroller.getLoggeduser().setLocation(roomlocation.getValue().toString());
            maincontroller.getLoggeduser().setTime(seltimer.getValue());
            maincontroller.closewindow();
            try{
                maincontroller.setdashboardwindow();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
           //cannot proceed to next one
        }

    }
}
