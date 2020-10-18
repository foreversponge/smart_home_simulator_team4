package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.HouseRoomsModel;
import models.RoomModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class dashBoardController {
    @FXML private JFXButton edit;
    @FXML private JFXToggleButton toggleSimBtn;
    @FXML private Label userLocation;
    @FXML private Label outsideT;
    @FXML private Label time;
    @FXML private Label date;
    private Main maincontroller;
    public ImageView window1;
    public ImageView door1;
    public ImageView light1;
    @FXML private AnchorPane anchorpaneroom1;
    @FXML private Label room1;
    @FXML private JFXButton togglelightbtn;
    Stage currentStage;


    LocalTime choosentime;
    LocalDate choosendate;
    IncrementTask incrementTask;
    Timer scheduleTimer;



    class IncrementTask extends TimerTask{
        private LocalTime localTime;
        private void setTime(LocalTime ctime) {
            this.localTime = ctime;
        }
        @Override
        public void run() {
            Platform.runLater(new Runnable() {// it has to run from the fx application
                @Override
                public void run() {
                    localTime = localTime.plusSeconds(1);
                    time.setText(localTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                }
            });

        }
    }

//    @FXML private JFXDatePicker datepicker;
//    @FXML private JFXTimePicker timepicker;



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


    public void setMainController(Main maincontroller, Stage currentStage) {

        this.maincontroller = maincontroller;
        this.currentStage = currentStage;
//        choosendate = maincontroller.getLoggeduser().getDate();
//        choosentime = maincontroller.getLoggeduser().getTime();
        choosendate=LocalDate.now();
        choosentime=LocalTime.now();
        incrementTask.setTime(choosentime);
        scheduleTimer.scheduleAtFixedRate(incrementTask,1000,1000);
    }
    public void initialize() {
        incrementTask =new IncrementTask();
        scheduleTimer =new Timer(true);
//        RoomModel[] allr = HouseRoomsModel.getAllRoomsArray();
//        int index=1;
//        for(RoomModel i: allr){
//            switch (index){
//                case 1:
//                    room1.setText(i.getName());
//                    window1.setImage(new Image("file:src/image/closewindow.png"));
//                    light1.setImage(new Image("file:src/image/lightoff.png"));
//                    door1.setImage(new Image("file:src/image/closedoor.png"));
//                    anchorpaneroom1.setVisible(true);
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    break;
//            }
//            index++;
//        }
    }

    public void toggleSimulation(ActionEvent event) {
        String mode = toggleSimBtn.getText();
        switch (mode){
            case "On":
                toggleSimBtn.setText("Off");
                edit.setDisable(true);
                // set value to show it is start simulation
                break;
            case "Off":
                toggleSimBtn.setText("On");
                edit.setDisable(false);
                break;

        }
    }

    public void handleChangeLocation(MouseEvent mouseEvent) {



        Dialog<String> updateLocation  = new Dialog<>();
        updateLocation.setHeaderText("Choose a location ");
        ObservableList<String> name= FXCollections.observableArrayList();
        ComboBox<String> avaiLocation = new ComboBox<>();
        for(RoomModel room : HouseRoomsModel.getAllRoomsArray()){
            name.add(room.getName());
        }
        avaiLocation.setItems(name);
        DialogPane locationDialogPane = updateLocation.getDialogPane();
        locationDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        locationDialogPane.setContent(avaiLocation);
        updateLocation.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && avaiLocation.getValue()!=null) {

                userLocation.setText(avaiLocation.getValue());
            }
            return null;
        });
        updateLocation.showAndWait();
    }
    
    public void handleChangeTime(MouseEvent mouseEvent) {
        Dialog<LocalTime> updateTime = new Dialog<>();
        updateTime.setHeaderText("Change context time ");
        Label hour= new Label();
        Label minute=new Label();
        ObservableList<Integer> h = FXCollections.observableArrayList();
        ObservableList<Integer> min = FXCollections.observableArrayList();
        for(int i=1;i<=24;i++){
            h.add(i);
        }
        for(int i=0;i<=59;i++){
            min.add(i);
        }

        JFXComboBox<Integer> hourlist = new JFXComboBox<>();
        JFXComboBox<Integer> minlist = new JFXComboBox<>();
        hourlist.setItems(h);
        minlist.setItems(min);
        GridPane gridPane = new GridPane();
        gridPane.add(hourlist, 1, 1);
        gridPane.add(minlist, 2, 1);
        DialogPane timeDialogPane = updateTime.getDialogPane();
        timeDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        timeDialogPane.setContent(gridPane);
        updateTime.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && hourlist.getValue()!=null &&minlist.getValue()!=null) {
                scheduleTimer.cancel();
                scheduleTimer = new Timer(true);
                LocalTime pickTime = LocalTime.of(hourlist.getValue(),minlist.getValue());
                incrementTask =new IncrementTask();
                incrementTask.setTime(pickTime);
                scheduleTimer.scheduleAtFixedRate(incrementTask,1000,1000);
            }
            return null;
        });
        updateTime.showAndWait();
    }

    public void handleChangeDate(MouseEvent mouseEvent) {
        Dialog<LocalDate> updateDate = new Dialog<>();
        updateDate.setHeaderText("Change context date ");
        DatePicker newDate= new DatePicker();
        DialogPane dateDialogPane = updateDate.getDialogPane();
        dateDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dateDialogPane.setContent(newDate);
        updateDate.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && newDate.getValue()!=null) {
                // set the time
                date.setText(newDate.getValue().toString());
            }
            return null;
        });
        updateDate.showAndWait();
    }

    public void handleChangeTemp(MouseEvent mouseEvent) {
        Dialog<String> updateTemp = new Dialog<>();
        updateTemp.setHeaderText("Change Outside Temperature");
        JFXTextField newTemp = new JFXTextField();
        JFXComboBox<String> sign =  new JFXComboBox();
        ObservableList<String> symbol = FXCollections.observableArrayList();
        sign.getItems().addAll("+","-");
        DialogPane TempDialogPane = updateTemp.getDialogPane();
        TempDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().setAll(sign, newTemp);
        TempDialogPane.setContent(content);
        updateTemp.setResultConverter((ButtonType button) -> {

            if (button == ButtonType.OK && newTemp.getText()!=null ) {
                if(newTemp.getText().matches("[0-9]+")){
                    if(sign.getValue()==null || sign.getValue().equals("+")){
                        outsideT.setText(newTemp.getText());
                    }
                    else{
                        outsideT.setText("-"+newTemp.getText());
                    }
                }
                else{
                    // cannot set the temperature keep old value and display on the log
                }

            }
            return null;
        });
        updateTemp.showAndWait();
    }

    /**
     * When simulation is ON, if the user clicks the edit button, the Edit Context Of Simulation window will appear
     * @param event user clicks the edit button
     */
    public void handleEdit(ActionEvent event) {
    	try {
			maincontroller.setEditContextWindow();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public void changeLoggedUser(MouseEvent mouseEvent) {

    }



}
