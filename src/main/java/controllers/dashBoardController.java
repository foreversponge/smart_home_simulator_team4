package controllers;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class dashBoardController {

    @FXML private Label logUser;
    @FXML private JFXListView consolelog;
    @FXML private TableView consoleTableView;
    @FXML private TableColumn columnTime;
    @FXML private TableColumn columnMessage;
    @FXML private JFXToggleButton toggleSimBtn;
    @FXML private Label userLocation;
    @FXML private Label outsideT;
    @FXML private Label time;
    @FXML private Label date;
    private Main mainController;
    Stage currentStage;
    LocalTime choosentime;
    LocalDate choosendate;
    IncrementTask incrementTask;
    Timer scheduleTimer;

    @FXML private ScrollPane scroll;
    @FXML private GridPane grid;

    /**
     * inner class which extends TimerTask
     * so Timer can generate action of this Task at fix rate
     */
    class IncrementTask extends TimerTask{
        private LocalTime localTime;
        private void setTime(LocalTime ctime) {
            this.localTime = ctime;
        }
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    localTime = localTime.plusSeconds(1);
                    time.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }
            });
        }
    }
    /**
     * Store an instance of the Main and currentStage
     * set the schdeuleTimer so that the time would continuous display the value
     * @param maincontroller
     * @param currentStage
     */
    public void setMainController(Main maincontroller, Stage currentStage) {
        this.mainController = maincontroller;
        this.currentStage = currentStage;
        choosendate=maincontroller.getLoggedUser().getDate();
        choosentime=maincontroller.getLoggedUser().getTime();
        date.setText(choosendate.toString());
        userLocation.setText(maincontroller.getLoggedUser().getCurrentLocation());
        logUser.setText(maincontroller.getLoggedUser().getNameAndRole());
        incrementTask.setTime(choosentime);
        scheduleTimer.scheduleAtFixedRate(incrementTask,1000,1000);
        consolelog.setItems(mainController.getLogMessages());
    }
    /**
     *initialize the list view of the console log
     */

    /** Initializes dynamically the house layout depending on the information receive in the layout file
     *
     * @throws IOException
     */
    public void initialize() throws IOException {
        incrementTask = new IncrementTask();
        scheduleTimer = new Timer(true);

        RoomModel[] allr = HouseRoomsModel.getAllRoomsArray();
        int column = 0;
        int row = 0;
        for (int i = 0; i < allr.length; i++) {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/room.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            RoomController roomController = fxmlLoader.getController();
            roomController.setData(allr[i]);

            if (column == 2) {
                column = 0;
                row++;
            }

            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);

            //set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);


            grid.add(anchorPane, column++, row);
            GridPane.setMargin(anchorPane, new Insets(15));

        }
    }
    /**
     * Turn on and Turn off Simulation
     * @param event
     */
    public void toggleSimulation(ActionEvent event) {
        String mode = toggleSimBtn.getText();
        switch (mode){
            case "On":
                toggleSimBtn.setText("Off");
                break;
            case "Off":
                toggleSimBtn.setText("On");
                break;
        }
    }
    /**
     * Display the dialog , and all the avaible location that logged user can choose to change location
     * @param mouseEvent
     */
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

    /**
     * display the dialog and allow use to change the time
     * set the cancel the old scheduleTimer and set the new scheduleTimer to continuous display the time
     * @param mouseEvent
     */
    public void handleChangeTime(MouseEvent mouseEvent) {
        TimerPickerModel tPicker = new TimerPickerModel();
        Dialog<LocalTime> updateTime = tPicker.getTimePicker();
        updateTime.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && tPicker.getHourList().getValue()!=null &&tPicker.getMinList().getValue()!=null) {
                scheduleTimer.cancel();
                scheduleTimer = new Timer(true);
                LocalTime pickTime = LocalTime.parse(tPicker.getHourList().getValue()+":"+tPicker.getMinList().getValue()+":00", DateTimeFormatter.ofPattern("HH:mm:ss"));
                incrementTask =new IncrementTask();
                incrementTask.setTime(pickTime);
                scheduleTimer.scheduleAtFixedRate(incrementTask,1000,1000);
            }
            return null;
        });
        updateTime.showAndWait();
    }

    /**
     * display dialg and DatePicker to allow logged user choose the date and update it on the dashboard
     * @param mouseEvent
     */
    public void handleChangeDate(MouseEvent mouseEvent) {
        Dialog<LocalDate> updateDate = new Dialog<>();
        updateDate.setHeaderText("Change context date ");
        DatePicker newDate= new DatePicker();
        DialogPane dateDialogPane = updateDate.getDialogPane();
        dateDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dateDialogPane.setContent(newDate);
        updateDate.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK && newDate.getValue()!=null) {
                date.setText(newDate.getValue().toString());
            }
            return null;
        });
        updateDate.showAndWait();
    }
    /**
     * Display the dialog and allow logged user to change temperature outside home
     * if logged user does not choose the sign before desired number of temperatuer, it consider positive
     * @param mouseEvent
     */
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
                        outsideT.setText("Outside Temperature: "+newTemp.getText());
                    }
                    else{
                        outsideT.setText("Outside Temperature: "+"-"+newTemp.getText());
                    }
                }
                else{
                    consolelog.getItems().add("[" + time.getText() + "] " + "The simulation must be ON to edit its context");
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
    public void onEditClicked(MouseEvent event) {
    	try {
    		if (toggleSimBtn.getText().equals("On")) {
    			mainController.setEditContextWindow();
    		}
    		else {
    			//display error message to console if simulation is OFF
    			consolelog.getItems().add("[" + time.getText() + "] " + "The simulation must be ON to edit its context");
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
