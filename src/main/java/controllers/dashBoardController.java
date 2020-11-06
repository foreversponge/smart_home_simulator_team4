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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.*;

import javafx.scene.image.ImageView;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class acts as a controller for the dashBoard.fxml
 * It is the simulator dashboard which allows users to access the various features
 * of the simulation
 * @author Team 4
 *
 */
public class dashBoardController {
	private HouseRoomsModel houseRoomsModel=HouseRoomsModel.getInstance();
	@FXML private JFXToggleButton toggleAwayMode;
	@FXML private JFXButton OnBtn;
	@FXML private JFXButton OffBtn;
	@FXML private JFXButton autoBtn;
	@FXML private JFXListView locationView;
	@FXML private JFXListView itemView;
	@FXML private JFXSlider timeSlider;
	@FXML private Label logUser;
	@FXML private JFXListView consolelog;
	@FXML private JFXToggleButton toggleSimBtn;
	@FXML private Label userLocation;
	@FXML private Label outsideT;
	@FXML private Label time;
	@FXML private Label date;
	private Main mainController;
	Stage currentStage;
	LocalTime choosentime;
	IncrementTask incrementTask;
	Timer scheduleTimer;
	private String selectItem;
	private List<String> selectLocation;

	public static boolean isAwayModeOn = false;

	@FXML private ScrollPane scroll;
	@FXML private GridPane grid;

	/**
	 * inner class which extends TimerTask
	 * so Timer can generate action of this Task at fix rate
	 */
	class IncrementTask extends TimerTask{
		private LocalTime localTime;
		private int timeInc =1;
		public void setTimeInc(int timeInc) {
			this.timeInc = timeInc;
		}
		private void setTime(LocalTime ctime) {
			this.localTime = ctime;
		}
		@Override
		public void run() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					localTime = localTime.plusSeconds(timeInc);
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
		choosentime=maincontroller.getLoggedUser().getTime();
		date.setText(maincontroller.getLoggedUser().getDate().toString());
		userLocation.setText(maincontroller.getLoggedUser().getCurrentLocation());
		logUser.setText(maincontroller.getLoggedUser().getNameAndRole());
		incrementTask.setTime(choosentime);
		scheduleTimer.scheduleAtFixedRate(incrementTask,1000,1000);
		consolelog.setItems(mainController.getLogMessages());
		displayLayout();
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
		setItemView();
		setLocationView();
		locationView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		toggleDisable(true);
		toggleAwayMode.setDisable(true);
	}

	/**
	 * method to clear the console log, clear date time, logged user location before return back the simulation parameter screen
	 * reset the block window, close the dashboard screen to go back to simulation parameter
	 * @param event
	 */
	public void handleBack(ActionEvent event) {
		UserModel userModel = mainController.getLoggedUser();
		userModel.setTime(null);
		userModel.setDate(null);
		userModel.setCurrentLocation("outside");
		consolelog.getItems().clear();
		for(RoomModel rm : houseRoomsModel.getAllRoomsArray()){
			rm.setObjectBlockingWindow(false);
			rm.setNumOpenWindows(0);
			rm.setNumOpenDoor(0);
			rm.setNumOpenLights(0);
		}
		mainController.closeWindow();
		try {
			mainController.setSimulationParametersWindow();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * check the permission of logged user before before the action like turn on/turn off window light door
	 * @param user
	 * @param location
	 * @return
	 */
	public boolean checkPermission(UserModel user,String location){
		String role = user.getRole();
		String userLocation = user.getCurrentLocation();
		boolean permission =false;
		switch (role.toLowerCase()){
			case "parent":
				permission = true;
				break;
			case "child":
			case "guest":
				if(userLocation.equalsIgnoreCase(location)){
					permission= true;
				}
				else {
					permission= false;
				}
				break;
			case "stranger":
				permission= false;
				break;
		}
		return permission;
	}

	/**
	 * to close all the door windows, lights and doors when the away is on
	 * @param event
	 */
	public void handleAwayToggle(ActionEvent event) {
		String mode = toggleAwayMode.getText();
		switch (mode){
			case "On":
				// off
				toggleAwayMode.setText("Off");
				isAwayModeOn = false;
				displayLayout();
				break;
			case "Off":
				//on
				if(mainController.getLoggedUser().getRole().equalsIgnoreCase("guest") ||
						mainController.getLoggedUser().getRole().equalsIgnoreCase("stranger")){
					addToConsoleLog("Cannot execute the command, you do not have the necessary permissions (Attempt to turn on/off the away mode).");
					toggleAwayMode.setText("Off");
					isAwayModeOn = false;
					displayLayout();
					return;
				}
				for(RoomModel rm: houseRoomsModel.getAllRoomsArray()){
					rm.setNumOpenLights(0);
					rm.setNumOpenWindows(0);
					rm.setNumOpenDoor(0);
				}
				isAwayModeOn = true;
				toggleAwayMode.setText("On");
				displayLayout();
				break;

		}
	}

	/**
	 * @return boolean if the user is in away mode
	 */
	public static boolean getAwayModeOn() {
		return isAwayModeOn;
	}

	/**
	 * dynamically display the room of the house
	 */
	public void displayLayout(){
		RoomModel[] allRoom = houseRoomsModel.getAllRoomsArray();
		int column = 0;
		int row = 0;
		for (int i = 0; i < allRoom.length; i++) {

			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("/views/room.fxml"));
			AnchorPane anchorPane = null;
			try {
				anchorPane = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			RoomController roomController = fxmlLoader.getController();
			roomController.setMainController(mainController);
			roomController.setData(allRoom[i]);

			if (column == 2) {
				column = 0;
				row++;
			}

			grid.add(anchorPane, column++, row);
			GridPane.setMargin(anchorPane, new Insets(15));
		}
	}

	/**
	 * when the select item is light, the auto mode is enable other wise set to disable
	 * @param mouseEvent
	 */
	public void handleItemSelected(MouseEvent mouseEvent) {
		List<String> listSelectItem = itemView.getSelectionModel().getSelectedItems();
		if(listSelectItem.get(0).equalsIgnoreCase("light")){
			autoBtn.setDisable(false);
		}
		else{
			autoBtn.setDisable(true);
		}
	}

	/**
	 * add the error message to the console list view
	 * @param err
	 */
	public void addToConsoleLog(String err){
		consolelog.getItems().add("[" + time.getText() + "] " + err);

	}

	/**
	 * initialize the item in the room
	 */
	public void setItemView(){
		itemView.getItems().add("light");
		itemView.getItems().add("door");
		itemView.getItems().add("window");
	}

	/**
	 * initialize the location of room in the house
	 */
	public void setLocationView(){
		for(RoomModel rm:houseRoomsModel.getAllRoomsArray() ){
			locationView.getItems().add(rm.getName());
		}
	}

	public void checkItemAndLocationSelection(){
		List<String> listSelectItem = itemView.getSelectionModel().getSelectedItems();
		List<String> listSelectLocation = locationView.getSelectionModel().getSelectedItems();
		if(listSelectItem.isEmpty() || listSelectLocation.isEmpty()){
			consolelog.getItems().add("[" + time.getText() + "] " + "Cannot do the command, please select both item and room");
			return;
		}
		selectItem = listSelectItem.get(0);
		selectLocation = listSelectLocation;
	}

	/**
	 * handle the on button
	 * when either item or room not select display error messge on console
	 * if both is selecte turn on the item in the select room(multiple room can be selected)
	 * @param event
	 */
	public void handleOnSelection(ActionEvent event) {
		RoomModel[] allRoom = toggleOnOff("on");
		houseRoomsModel.setAllRooms(allRoom);
		displayLayout();
	}

	public RoomModel[] toggleOnOff(String mode){
		checkItemAndLocationSelection();
		RoomModel[] allRoom= houseRoomsModel.getAllRoomsArray();
		if(selectLocation != null) {
			for(RoomModel rm : allRoom){
				for (String loc : selectLocation) {
					if (!checkPermission(mainController.getLoggedUser(), loc) && rm.getName().equalsIgnoreCase(loc)) {
						addToConsoleLog("Cannot execute the command, you do not have the necessary permissions (Attempt to set " + mode + " mode in " + loc + ").");
						continue;
					}
					if (rm.getName().equalsIgnoreCase(loc)) {
						switch (selectItem) {
							case "light":
								rm.setNumOpenLights(mode.equals("on") ? rm.getNumLights() : 0);
								break;
							case "door":
								if (!isAwayModeOn) {
									rm.setNumOpenDoor(mode.equals("on") ? rm.getNumDoors() : 0);
								} else if (isAwayModeOn && !(rm.getName().equalsIgnoreCase("House Entrance")
										|| rm.getName().equalsIgnoreCase("Garage")
										|| rm.getName().equalsIgnoreCase("Backyard"))) {
									rm.setNumOpenDoor(mode.equals("on") ? rm.getNumDoors() : 0);
								} else {
									addToConsoleLog("Cannot execute command, away mode is turned on.");
								}
								break;
							case "window":
								if (!rm.isObjectBlockingWindow()) {
									rm.setNumOpenWindows(mode.equals("on") ? rm.getNumWindows() : 0);
								} else {
									addToConsoleLog("Cannot execute the command, there is an object blocking the window of the " + loc);
								}
								break;
						}
						if (mode.equals("off")) {
							rm.setMode("regular");
						}
					}
				}
			}
		}
		return allRoom;
	}

	/**
	 * handle the on button
	 * when either item or room not select display error messge on console
	 * if both is selecte turn off the item in the select room(multiple room can be selected)
	 * @param event
	 */
	public void handleOffSelection(ActionEvent event) {
		RoomModel[] allRoom= toggleOnOff("off");
		houseRoomsModel.setAllRooms(allRoom);
		displayLayout();
	}

	/**
	 * auto select is for light when there is at least one person in the room, the light is automatically turn on
	 * if there is no person in the room, it would turn off the light
	 * @param event
	 */
	public void handleAutoSelection(ActionEvent event) {
		checkItemAndLocationSelection();
		RoomModel[] allRoom= houseRoomsModel.getAllRoomsArray();
		for(RoomModel rm: allRoom){
			for(String loc: selectLocation){
				if(!checkPermission(mainController.getLoggedUser(),loc) && rm.getName().equalsIgnoreCase(loc)) {
					addToConsoleLog("Cannot execute the command, you do not have the necessary permissions (attempt to set auto mode in "+loc + ").");
					continue;
				}
				if(rm.getName().equalsIgnoreCase(loc)){
					rm.setMode("auto");
				}
			}
		}
		displayLayout();
	}

	/**
	 * reset the current timer
	 * @param i increment value of the time(speed time)
	 * @param time
	 */
	public void resetTimerTask(int i, LocalTime time){
		scheduleTimer.cancel();
		scheduleTimer = new Timer(true);
		incrementTask =new IncrementTask();
		incrementTask.setTimeInc(i);
		incrementTask.setTime(time);
		scheduleTimer.scheduleAtFixedRate(incrementTask,1000,1000);
	}

	/**
	 * handle the slider change of time
	 * @param mouseEvent
	 */
	public void timerSliderHandler(MouseEvent mouseEvent) {
		resetTimerTask((int) Math.round(timeSlider.getValue()), LocalTime.parse(time.getText()));

	}

	/**
	 * toggle the disable of the element such as timeslider, item list view, location list view
	 * @param disable
	 */
	public void toggleDisable(boolean disable){
		timeSlider.setDisable(disable);
		itemView.setDisable(disable);
		locationView.setDisable(disable);
		OnBtn.setDisable(disable);
		OffBtn.setDisable(disable);
	}

	/**
	 * Turn on and Turn off Simulation
	 * when simulation on, the time could adjust by the using time slider
	 * when simulation is off set back to default which is 1
	 * @param event
	 */
	public void toggleSimulation(ActionEvent event) {
		String mode = toggleSimBtn.getText();
		switch (mode){
		case "On":
			toggleSimBtn.setText("Off");
			resetTimerTask(1, LocalTime.parse(time.getText()));
			toggleDisable(true);
			toggleAwayMode.setDisable(false);
			break;
		case "Off":
			toggleSimBtn.setText("On");
			resetTimerTask((int) timeSlider.getValue(), LocalTime.parse(time.getText()));
			toggleDisable(false);
			toggleAwayMode.setDisable(false);
			break;
		}
	}

	/**
	 * update the logged user location label
	 */
	public void updateLoggedLocation(){
		userLocation.setText(mainController.getLoggedUser().getCurrentLocation());
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
		for(RoomModel room : houseRoomsModel.getAllRoomsArray()){
			name.add(room.getName());
		}
		name.add("outside");
		avaiLocation.setItems(name);
		DialogPane locationDialogPane = updateLocation.getDialogPane();
		locationDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		locationDialogPane.setContent(avaiLocation);
		updateLocation.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK && avaiLocation.getValue()!=null) {
				userLocation.setText(avaiLocation.getValue());
				mainController.getLoggedUser().setCurrentLocation(avaiLocation.getValue());
			}
			return null;
		});
		updateLocation.showAndWait();
		displayLayout();
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
				LocalTime pickTime = LocalTime.parse(tPicker.getHourList().getValue()+":"+tPicker.getMinList().getValue()+":00", DateTimeFormatter.ofPattern("HH:mm:ss"));
				resetTimerTask(1, pickTime);
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
					addToConsoleLog("The temperature cannot contain a letter. Please try again.");
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
				addToConsoleLog("The simulation must be ON to edit its context");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
