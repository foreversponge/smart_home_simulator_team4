package controller;

import com.google.gson.Gson;
import com.jfoenix.controls.*;
import helper.Person;
import helper.Room;
import helper.allRoom;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class dashboardcontroller  {
    @FXML private Label loc;
    private Maincontroller maincontroller;
    public ImageView window1;
    public ImageView door1;
    public ImageView light1;
    @FXML private AnchorPane anchorpaneroom1;
    @FXML private Label room1;
    @FXML private JFXButton togglelightbtn;
    LocalTime choosentime;
    LocalDate choosendate;
    IncTask mytask;
    Timer timer;

     @FXML
    private StackPane stackpaneroot;
    @FXML
    private Label time;
    @FXML
    private Label date;
    @FXML
    private JFXDatePicker datepicker;
    @FXML
    private JFXTimePicker timepicker;



    public void togglelight(ActionEvent event){
        String mode =  ((JFXButton)event.getSource()).getText();
        if(mode.equals("Off")){
            togglelightbtn.setText("On");
            light1.setImage(new Image("file:src/image/lighton.png"));
            // call to display the light
        }
        else {
            togglelightbtn.setText("Off");
            light1.setImage(new Image("file:src/image/lightoff.png"));
        }
    }



    public void timepickerhandler(ActionEvent event) {
        if(timer!=null){
            timer.cancel();
            timer = new Timer(true);
        }
        mytask=new IncTask();
        LocalTime picktime = timepicker.getValue();
        mytask.settime(LocalTime.parse(picktime.format(DateTimeFormatter.ofPattern("hh:mm:ss"))));
        timer.scheduleAtFixedRate(mytask,1000,1000);

    }


    public void addlayoutdialog(ActionEvent event) {

    }
    public void setMaincontroller(Maincontroller maincontroller) {

        this.maincontroller = maincontroller;
        choosendate = maincontroller.getLoggeduser().getDate();
        choosentime = maincontroller.getLoggeduser().getTime();
        mytask.settime(choosentime);
        timer.scheduleAtFixedRate(mytask,1000,1000);
    }
    public void initialize() {
        // init run first , it run even before setMaincontroller
        mytask=new IncTask();
        timer =new Timer(true);

        Room[] allr = allRoom.getAllroom();
        int index=1;
        for(Room i: allr){
            switch (index){
                case 1:
                    room1.setText(i.getName());
                    window1.setImage(new Image("file:src/image/closewindow.png"));
                    light1.setImage(new Image("file:src/image/lightoff.png"));
                    door1.setImage(new Image("file:src/image/closedoor.png"));
                    anchorpaneroom1.setVisible(true);
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            index++;
        }
    }

    public void changetime(MouseEvent mouseEvent) {
        // we should change the context of simulation(logged user), live time
        Dialog<LocalTime> updatetime = new Dialog<>();
        updatetime.setHeaderText("Change context time ");
        JFXTimePicker picktime= new JFXTimePicker();
        DialogPane dipane = updatetime.getDialogPane();
        dipane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dipane.setContent(picktime);
        updatetime.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && picktime.getValue()!=null) {
                // set the time
                timer.cancel();
                timer = new Timer(true);
                mytask=new IncTask();
                mytask.settime(picktime.getValue());
                timer.scheduleAtFixedRate(mytask,1000,1000);
            }
            return null;
        });
        updatetime.showAndWait();

    }

    public void changedate(MouseEvent mouseEvent) {
        Dialog<LocalDate> changedatedialgo = new Dialog<>();
        changedatedialgo.setHeaderText("Change context time ");
        JFXDatePicker pickdate= new JFXDatePicker();
        DialogPane dipane = changedatedialgo.getDialogPane();
        dipane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dipane.setContent(pickdate);
        changedatedialgo.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && pickdate.getValue()!=null) {
                // set the time
                date.setText(pickdate.getValue().toString());
            }
            return null;
        });
        changedatedialgo.showAndWait();
    }

    public void changelocation(MouseEvent mouseEvent) {
        // check if the location is valid, maybe make combobox of the house you scan

        Dialog<LocalTime> updatetime = new Dialog<>();
        updatetime.setHeaderText("Choose a location ");
        ObservableList<String> name= FXCollections.observableArrayList();
        ComboBox<String> alocation = new ComboBox<>();
        for(Room r : allRoom.getAllroom()){
           name.add(r.getName());
        }
        alocation.setItems(name);
        DialogPane dipane = updatetime.getDialogPane();
        dipane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dipane.setContent(alocation);
        updatetime.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && alocation.getValue()!=null) {
                // set the time
                loc.setText(alocation.getValue());
            }
            return null;
        });
        updatetime.showAndWait();
    }


    class IncTask extends TimerTask{
        public LocalTime loctime;


        public void settime(LocalTime settime) {
            this.loctime = settime;
        }
        @Override
        public void run() {
            Platform.runLater(new Runnable() {// it has to run from the fx application
                @Override
                public void run() {
                    loctime = loctime.plusSeconds(1);
                    time.setText(loctime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                }
            });

        }
    }
}
