package controller;

import com.google.gson.Gson;
import helper.Person;
import helper.Room;
import helper.allRoom;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Maincontroller extends Application {

    public Stage currentstate;
    private Person loggeduser;

    public Person getLoggeduser() {
        return loggeduser;
    }

    public void setLoggeduser(Person loggeduser) {
        this.loggeduser = loggeduser;
    }



    private ObservableList<Person> persondata  = FXCollections.observableArrayList();
    private ObservableList<Person> temppersondata = FXCollections.observableArrayList();

    public ObservableList<Person> getTemppersondata() {
        return temppersondata;
    }
    public void setPersondata(ObservableList<Person> persondata) {
        this.persondata = persondata;
    }
    public void setTemppersondata(ObservableList<Person> temppersondata) {
        // at first we should not save to the person data because when they add they could cancel
        // so when the click save the temp = person
        this.temppersondata = temppersondata;
    }


    public ObservableList<Person> getPersondata() {

        return persondata;
    }

    public static void main(String[] args) {
        launch(args);
    }
    public void closewindow(){
        currentstate.close();
    }



    @Override
    public void start(Stage primaryStage)  {
//        temppersondata.add(new Person("kim", "child"));
        persondata.add(new Person("jim", "parent"));
        String json = "[{'name':'kitchen','windownum': 2, 'doornum': 1, 'lightnum': 2, 'nextroomname':'livingroom'},{'name':'livingroom','windownum': 1, 'doornum': 2, 'lightnum': 3, 'nextroomname':'bedroom'}]";
        Room[] obj = new Gson().fromJson(json, Room[].class);
        allRoom.setAllroom(obj);
        currentstate = primaryStage;
        try{
            setlayoutwindow();
//            setcontextsimulationwindow();
        }
        catch (IOException e){
            e.printStackTrace();
        }



    }
    public void setlayoutwindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Maincontroller.class.getResource("../view/houselayout.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        Scene scene = new Scene(anchorPane);
        houselayoutcontroller houselay = fxmlLoader.getController();
        // return the reference so we can use it to set the value of controller of houselayout
        houselay.setMaincontroller(this);

        currentstate.setScene(scene);
        currentstate.show();
    }
    public void setdashboardwindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Maincontroller.class.getResource("../view/dashboard.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        Scene scene = new Scene(anchorPane);
        dashboardcontroller dashboardcontroller = fxmlLoader.getController();
        dashboardcontroller.setMaincontroller(this);
        currentstate.setScene(scene);
        currentstate.show();
    }
    public void setcontextsimulationwindow() throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(Maincontroller.class.getResource("../view/contextofsimulation.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        Scene scene = new Scene(anchorPane);
        contextofsimulationcontroller contextcontroller = fxmlLoader.getController();
        contextcontroller.setMaincontroller(this);
        currentstate.setScene(scene);
        currentstate.show();


    }
    public void seteditwindow() throws IOException{

        // keep the current window and display another

        FXMLLoader fxmlLoader = new FXMLLoader(Maincontroller.class.getResource("../view/edituser.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        Scene scene = new Scene(anchorPane);
        editusercontroller editusercontroller = fxmlLoader.getController();
        Stage editstage= new Stage();
        editusercontroller.setMaincontroller(this,editstage);
        editstage.initOwner(currentstate);
        temppersondata.clear();

        persondata.forEach((person)->{
            temppersondata.add(person);
        });
        editstage.initModality(Modality.WINDOW_MODAL);
        editstage.setScene(scene);
        editstage.show();
    }


}

