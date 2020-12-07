package controllers;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;

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
	@FXML private Label season;
	@FXML private JFXListView consolelog;
	@FXML private JFXToggleButton toggleSimBtn;
	@FXML private Label userLocation;
	@FXML private Label outsideT;
	@FXML private Label time;
	@FXML private Label date;
	@FXML private Label delay_minutes_label;
	private double outSideTemperature=Double.MAX_VALUE;
	private Main mainController;
	Stage currentStage;
	LocalTime choosentime;
	IncrementTask incrementTask;
	Timer scheduleTimer;
	int outsideTemp = 0;
	private String selectItem;
	private List<String> selectLocation;
	private Map<String, ArrayList<LocalTime>> awayModeLight= new HashMap<>();
	// the key of this map it the roomName and arrayList would store the fromTime to ToTime that the light should be on
	@FXML private ScrollPane scroll;
	@FXML private GridPane grid;
	private String seasonStr=null;

	/**
	 * Check if HAVC is on or off
	 * @return true if On, false if off
	 */
	public boolean isHavc() {
		return havc;
	}

	/**
	 * Setter to set HAVC value
	 * @param havc true for On, false for Off
	 */
	public void setHavc(boolean havc) {
		this.havc = havc;
	}

	private boolean havc=false;

	/**
	 * getter awayModeLight map which store the key of the room name and the value it is array of start time to open and time to close
	 * @return
	 */
	public Map<String, ArrayList<LocalTime>> getAwayModeLight() {
		return awayModeLight;
	}
	/**
	 * setter awayModeLight map which store the key of the room name and the value it is array of start time to open and time to close
	 * @param awayModeLight
	 */
	public void setAwayModeLight(Map<String, ArrayList<LocalTime>> awayModeLight) {
		this.awayModeLight = awayModeLight;
	}

	/**
	 * inner class which extends TimerTask
	 * so Timer can generate action of this Task at fix rate
	 */
	class IncrementTask extends TimerTask{
		private LocalTime localTime;
		private int timeInc =1;
		private String mode;

		/**
		 * constructor by default it increment timerTask
		 */
		public IncrementTask(){
			this.mode = "Increment";
		}
		/**
		 * constructor to set the mode
		 */
		public IncrementTask(String mode){
			this.mode = mode;
		}

		/**
		 * set how much would the time increment
		 * @param timeInc
		 */
		public void setTimeInc(int timeInc) {
			this.timeInc = timeInc;
		}

		/**
		 * set the Time
		 * @param ctime
		 */
		private void setTime(LocalTime ctime) {
			this.localTime = ctime;
		}

		/**
		 * to handle the light to be open by checking the autolight in the away mode
		 */
		private void awayModeLight(){
			boolean update =false;
			if(!awayModeLight.isEmpty()) {
				RoomModel[] allRoom = houseRoomsModel.getAllRoomsArray();
				for(RoomModel rm : allRoom){
					if(awayModeLight.containsKey(rm.getName())){
						if(isTimeDuringAwayModeLights(rm))
						{
							if(rm.getNumOpenLights()==0){
								rm.setNumOpenLights(rm.getNumLights());
								update = true;
							}
						}
						else{
							if(rm.getNumOpenLights()!=0){
								rm.setNumOpenLights(0);
								update = true;
							}
						}
					}
				}
			}
			if(update){
				displayLayout();
			}
		}
		
		/**
		 * Checks if local time is during the away mode lights time interval
		 * @param rm room
		 * @return true if it is, false otherwise
		 */
		public boolean isTimeDuringAwayModeLights(RoomModel rm) {
			return localTime.isAfter(awayModeLight.get(rm.getName()).get(0))
					&& localTime.isBefore(awayModeLight.get(rm.getName()).get(1));
		}

		/**
		 * handle summer temperature,
		 * if the current temperature higher than the desired temperature , turn on the AC
		 * if it reach the desired temperature(0.25 different with the desired temperature) close the AC
		 * if anything change AC icon would be update
		 * @param time
		 */
		public void summerSeason(String time){
			RoomModel [] roomModels = houseRoomsModel.getAllRoomsArray();
			for(RoomModel rm : roomModels){
				double currentTemperature = rm.getCurrentTemperature();
				switch (time){
					case "day":
						if(Math.abs(currentTemperature-rm.getTemperature().getDayTemp()) > 0.25 || round(currentTemperature, 1) != round(rm.getTemperature().getDayTemp(), 1)) {
							if(currentTemperature>rm.getTemperature().getDayTemp()){
								if(!rm.isAc()){
									rm.setAc(true);
									displayLayout();
								}
								rm.setCurrentTemperature(currentTemperature-0.1*timeInc);
								addToConsoleLog("HAVC is decreasing day temperature of the " + rm.getName() + " " + round(rm.getCurrentTemperature(), 1));
							}
						}
						else{
							if(rm.isAc()){
								rm.setAc(false);
								displayLayout();
							}
						}
						break;
					case "night":
						if(Math.abs(currentTemperature-rm.getTemperature().getNightTemp()) > 0.25 || round(currentTemperature, 1) != round(rm.getTemperature().getDayTemp(), 1)) {
							if(currentTemperature>rm.getTemperature().getNightTemp()){
								if(!rm.isAc()){
									rm.setAc(true);
									displayLayout();
								}
								rm.setCurrentTemperature(currentTemperature-0.1*timeInc);
								addToConsoleLog("HAVC is decreasing night temperature of the " + rm.getName() + " " + round(rm.getCurrentTemperature(), 1));
							}
						}
						else{
							if(rm.isAc()){
								rm.setAc(false);
								displayLayout();
							}
						}
						break;
					case "morning":
						if(Math.abs(currentTemperature-rm.getTemperature().getMorningTemp()) > 0.25 || round(currentTemperature, 1) != round(rm.getTemperature().getDayTemp(), 1)) {
							if(currentTemperature>rm.getTemperature().getMorningTemp()){
								if(!rm.isAc()){
									rm.setAc(true);
									displayLayout();
								}
								rm.setCurrentTemperature(currentTemperature-0.1*timeInc);
								addToConsoleLog("HAVC is decreasing morning temperature of the " + rm.getName() + " " + round(rm.getCurrentTemperature(), 1));
							}
						}
						else{
							if(rm.isAc()){
								rm.setAc(false);
								displayLayout();
							}
						}
						break;
				}
			}
		}
		
		/**
		 * handle winter temperature
		 * if the current temperature is lower than the desired temperature turn on the heat
		 * if it reach the desired temperature(0.25 different with the desired temperature) close the heating
		 * if anything change heating icon would be update
		 * @param time
		 */
		public void winterSeason(String time){
			RoomModel [] roomModels = houseRoomsModel.getAllRoomsArray();
			for(RoomModel rm : roomModels){
				double currentTemperature = rm.getCurrentTemperature();

				switch (time){
					case "day":
						if(Math.abs(currentTemperature-rm.getTemperature().getDayTemp()) > 0.25 || round(currentTemperature, 1) != round(rm.getTemperature().getDayTemp(), 1)) {
							if(currentTemperature<rm.getTemperature().getDayTemp()){
								if(!rm.isHeating()){
									rm.setHeating(true);
									displayLayout();
								}
								rm.setCurrentTemperature(currentTemperature+0.1*timeInc);
								addToConsoleLog("HAVC is increasing temperature of the " + rm.getName() + " " + round(rm.getCurrentTemperature(), 1));
							}
						}
						else{
							if(rm.isHeating()){
								rm.setHeating(false);
								displayLayout();
							}
						}
						break;
					case "night":
						if(Math.abs(currentTemperature-rm.getTemperature().getNightTemp()) > 0.25 || round(currentTemperature, 1) != round(rm.getTemperature().getDayTemp(), 1)) {
							if(currentTemperature<rm.getTemperature().getNightTemp()){
								if(!rm.isHeating()){
									rm.setHeating(true);
									displayLayout();
								}
								rm.setCurrentTemperature(currentTemperature+0.1*timeInc);
								addToConsoleLog("HAVC is increasing temperature of the " + rm.getName() + " " + round(rm.getCurrentTemperature(), 1));
							}
						}
						else{
							if(rm.isHeating()){
								rm.setHeating(false);
								displayLayout();
							}
						}
						break;
					case "morning":
						if(Math.abs(currentTemperature-rm.getTemperature().getMorningTemp()) > 0.25 || round(currentTemperature, 1) != round(rm.getTemperature().getDayTemp(), 1)) {
							if(currentTemperature<rm.getTemperature().getMorningTemp()){
								if(!rm.isHeating()){
									rm.setHeating(true);
									displayLayout();
								}
								rm.setCurrentTemperature(currentTemperature+0.1*timeInc);
								addToConsoleLog("HAVC is increasing temperature of the " + rm.getName() + " " + round(rm.getCurrentTemperature(), 1));
							}
						}
						else{
							if(rm.isHeating()){
								rm.setHeating(false);
								displayLayout();
							}
						}
						break;
				}
			}
		}
		
		/**
		 * continuous monitor temperature
		 * continuous monitor current temperature of the room with the desired temperature
		 */
		public void temperatureMonitor(){
			if(outSideTemperature != Double.MAX_VALUE && isHavc()){
				if(isNight()){
					if(seasonStr!=null&& seasonStr.equalsIgnoreCase("winter")){
						winterSeason("night");
					}
					else{
						summerSeason("night");
					}
				}
				else if(isMorning()){
					System.out.println("morning");
					if(seasonStr!=null&& seasonStr.equalsIgnoreCase("winter")){
						winterSeason("morning");
					}
					else{
						summerSeason("morning");
					}
				}
				else{
					if(seasonStr!=null&& seasonStr.equalsIgnoreCase("winter")){
						winterSeason("day");
					}
					else{
						summerSeason("day");
					}
				}
			}
		}
		
		/**
		 * Checks if the current time is during the morning
		 * @return true if yes, false otherwise
		 */
		public boolean isMorning() {
			return localTime.isAfter(LocalTime.parse("04:00:00",DateTimeFormatter.ofPattern("HH:mm:ss"))) && localTime.isBefore(LocalTime.parse("10:00:00" , DateTimeFormatter.ofPattern("HH:mm:ss")));
		}
		
		/**
		 * Checks if the current time is during the night
		 * @return true if yes, false otherwise
		 */
		public boolean isNight() {
			return localTime.isAfter(LocalTime.parse("20:00:00",DateTimeFormatter.ofPattern("HH:mm:ss"))) && localTime.isBefore(LocalTime.parse("03:00:00" , DateTimeFormatter.ofPattern("HH:mm:ss")));
		}

		/**
		 * to increment the time and handle the awayModeLight
		 */
		private void increment(){
			localTime = localTime.plusSeconds(timeInc);
			time.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
			awayModeLight();
			temperatureMonitor();
		}

		/**
		 * to decrement the time
		 */
		private void decrement(){
			localTime = localTime.minusSeconds(timeInc);
			
		}

		/**
		 * would call different method if it is increment or decrement
		 */
		@Override
		public void run() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(mode.equalsIgnoreCase("increment")){
						increment();
					}
					else{
						decrement();
					}
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
		seasonStr=maincontroller.getLoggedUser().getSeason();
		season.setText("Season: " + maincontroller.getLoggedUser().getSeason() + " (" + maincontroller.getLoggedUser().getSeasonStart() + " - " + maincontroller.getLoggedUser().getSeasonEnd() + ")");
		incrementTask.setTime(choosentime);
		scheduleTimer.scheduleAtFixedRate(incrementTask,1000,1000);
		mainController.getShpModel().setConsoleLog(consolelog);
		consolelog.setItems(mainController.getLogMessages());
		displayLayout();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/SHH.fxml"));
		try {
			Parent root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SHHController shhController = fxmlLoader.getController();
		shhController.setMainController(mainController);
	}

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
		mainController.getShpModel().setAwayModeOn(false);
		for(RoomModel rm : houseRoomsModel.getAllRoomsArray()){
			rm.setObjectBlockingWindow(false);
			rm.setNumOpenWindows(0);
			rm.setNumOpenDoor(0);
			rm.setNumOpenLights(0);
		}
		scheduleTimer.cancel();
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
		LogToFileModel.log(Level.INFO, err);
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

	/**
	 * This method checks if an item was selected as well as a location
	 */
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
	 * when either item or room not select display error message on console
	 * if both is selected turn on the item in the select room(multiple room can be selected)
	 * @param event
	 */
	public void handleOnSelection(ActionEvent event) {
		RoomModel[] allRoom = toggleOnOff("on");
		houseRoomsModel.setAllRooms(allRoom);
		displayLayout();
	}

	/**
	 * This method handles the ON or OFF requests for items
	 * @param mode of the simulator
	 * @return an array containing information about each room
	 */
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
							if (!mainController.getShpModel().isAwayModeOn()) {
								rm.setNumOpenDoor(mode.equals("on") ? rm.getNumDoors() : 0);
							} else if (mainController.getShpModel().isAwayModeOn() && !(rm.getName().equalsIgnoreCase("House Entrance")
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
	 * when either item or room not select display error message on console
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
	 * toggle the disable of the element such as time slider, item list view, location list view
	 * @param disable
	 */
	public void toggleDisable(boolean disable){
		timeSlider.setDisable(disable);
		itemView.setDisable(disable);
		locationView.setDisable(disable);
		OnBtn.setDisable(disable);
		OffBtn.setDisable(disable);
		toggleAwayMode.setDisable(disable);
		delay_minutes_label.setDisable(disable);
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
	 * Display the dialog , and all the available location that logged user can choose to change location
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
				choosentime = LocalTime.parse(tPicker.getHourList().getValue()+":"+tPicker.getMinList().getValue()+":00", DateTimeFormatter.ofPattern("HH:mm:ss"));
			}
			return null;
		});
		updateTime.showAndWait();
	}

	/**
	 * display dialog and DatePicker to allow logged user to choose the date and update it on the dashboard
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
		SHHController shhcontrol = new SHHController();
		content.getChildren().setAll(sign, newTemp);
		TempDialogPane.setContent(content);
		updateTemp.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK && newTemp.getText()!=null ) {
				if(newTemp.getText().matches("[0-9]+")){
					if(sign.getValue()==null || sign.getValue().equals("+")){
						outsideT.setText("Outside Temperature: "+newTemp.getText());
						outSideTemperature = Double.parseDouble(newTemp.getText());
					}
					else{
						outsideT.setText("Outside Temperature: "+"-"+newTemp.getText());
						outsideTemp = Integer.parseInt(newTemp.getText());
						shhcontrol.monitorTemp();
					}
					for(RoomModel rm : houseRoomsModel.getAllRoomsArray()){
						rm.setCurrentTemperature(outSideTemperature);
						outsideTemp = Integer.parseInt(newTemp.getText());
						shhcontrol.monitorTemp();
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

	/**
	 * Turn On or Off away Mode
	 * @param event user clicks the away mode toggle
	 */
	public void setAwayMode(MouseEvent event) {
		String awayMode = toggleAwayMode.getText();
		ArrayList<ZoneModel> allZone = houseRoomsModel.getAllZonesArray();
		switch (awayMode){
			case "ON":
				toggleAwayMode.setText("OFF");
				mainController.getShpModel().setAwayModeOn(false);
				break;
			case "OFF":
				if(mainController.getLoggedUser().getRole().equalsIgnoreCase("guest") || mainController.getLoggedUser().getRole().equalsIgnoreCase("stranger")) {
					addToConsoleLog("Cannot do the command, Permission denial");
					toggleAwayMode.setSelected(false);
				}
				else if (!mainController.getLoggedUser().getCurrentLocation().equals("outside")) {
					addToConsoleLog("Away Mode can only be set if not home. You must be outside.");
					toggleAwayMode.setSelected(false);
				}
				else {
					toggleAwayMode.setText("ON");
					addToConsoleLog("Away Mode is now ON");
					mainController.getLoggedUser().setCurrentLocation("outside");
					updateLoggedLocation();
					mainController.getShpModel().setAwayModeOn(true);
					handleAwayModeOn();
				}
				break;
		}
	}

	/**
	 * When away mode is turned ON, all doors and windows must be closed 
	 */
	public void handleAwayModeOn() {
		addToConsoleLog("Request sent to SHC to close doors, windows and lights");
		for(RoomModel room : houseRoomsModel.getAllRoomsArray()){
			room.setNumOpenLights(0);
			if (room.isObjectBlockingWindow()) {
				addToConsoleLog("AWAY MODE WARNING: Cannot close window in " + room.getName() + " because object present");
			} else {
				room.setNumOpenWindows(0);
			}
			room.setNumOpenDoor(0);
		}
		for (UserModel user : mainController.getUserModelData()) {
			if (user.getCurrentLocation() != null && !user.getCurrentLocation().equals("outside")) {
				addToConsoleLog("AWAY MODE WARNING: Person present in " + user.getCurrentLocation());
			}
		}
		displayLayout();
	}

	/**
	 * When the away mode is on, if the user clicks the Pick Lights to Keep On button, a window handling lights to remain on will appear
	 * @param event user clicks the Pick Lights to Keep On button
	 */
	public void pickLightsToKeepOnClick(MouseEvent event) {
		String awayMode = toggleAwayMode.getText();
		try {
			if (awayMode.equals("ON")) {
				mainController.setLightsToRemainOpenWindow();
			} else {
				//display error message to console if away mode is OFF
				addToConsoleLog("The away mode must be ON to be able to pick lights that will remain on");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * The user will be able to enter the number of minutes they want to delay the call to the authorities
	 */
	public void handleAuthoritiesDelayMin(MouseEvent mouseEvent){

		SHPModel delay_time = new SHPModel();

		Dialog<String> updateDelayTime = new Dialog<>();
		updateDelayTime.setHeaderText("Set Authorities Call delay in minutes");
		JFXTextField newDelay = new JFXTextField();
		ObservableList<String> symbol = FXCollections.observableArrayList();
		DialogPane TempDialogPane = updateDelayTime.getDialogPane();
		TempDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		HBox content = new HBox();
		content.getChildren().setAll(newDelay);
		TempDialogPane.setContent(content);

		updateDelayTime.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK && newDelay.getText()!=null ) {
				if(newDelay.getText().matches("[0-9]+")){

					delay_minutes_label.setText(newDelay.getText() + " minutes");

					delay_time.setAlertpolice(newDelay.toString());
				}
				else{
					consolelog.getItems().add("[" + time.getText() + "] " + "The time cannot contain a letter. Please try again.");
				}
			}
			return null;
		});
		updateDelayTime.showAndWait();
	}
	
	/**
	 * Given a number and a rounding precision, the method will return the rounded value
	 * (e.g. 1.23 with a precision of 1 will return 1.2)
	 * @param value number you want to round
	 * @param precision number of decimals of precision to round the number
	 * @return the rounded number
	 */
	private static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
}

//update layout when temp change
