package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.HouseRoomsModel;
import models.RoomModel;
import models.UserModel;

/**
 * This class acts as a controller for the EditContextOfSimulation.fxml
 * It allows users to place users in a specific location/room of the house
 * or block window movement
 * @author Team 4
 *
 */
public class EditContextOfSimulationController {

	private Main mainController;	//instance of Main to obtain userData
	private Stage currentStage;		//instance of the current Stage to open or close it
	private HouseRoomsModel houseRoomsModel = HouseRoomsModel.getInstance();	//contains data about each room of the house
	@FXML private TableView<UserModel> moveUsersTableView;	//Place Users tableView of EditContextOfSimuatlion.fxml
	@FXML private TableColumn nameOfUserColumn;		//Name of user column of tableView of EditContextOfSimuatlion.fxml
	@FXML private TableColumn permissionColumn;		//Permission column of tableView of EditContextOfSimuatlion.fxml
	@FXML private TableColumn locationColumn;	//location of user column of tableView of EditContextOfSimuatlion.fxml

	private ObservableList<RoomModel> roomModelData = FXCollections.observableArrayList();	//stores data from RoomModel
	@FXML private TableView<RoomModel> objectWindowTableView;	//tableView for blocking window movement
	@FXML private TableColumn roomNameColumn;	//name of room column of tableView
	@FXML private TableColumn windowNumColumn;	//number of windows column of tableView
	@FXML private TableColumn objectPresentColumn; //is object present on window column of tableView

	/**
	 * Setup of table (columns) with attributes from the UserModel class and from the RoomModel class
	 */
	public void initialize(){
		nameOfUserColumn.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
		permissionColumn.setCellValueFactory(new PropertyValueFactory<UserModel, String>("role"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<UserModel, ComboBox<String>>("locationOptions"));

		//Windows
		roomNameColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, String>("name"));
		windowNumColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, String>("numWindows"));
		objectPresentColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, ComboBox<String>>("objectBlockingWindowComboBox"));
		for (RoomModel room : houseRoomsModel.getAllRoomsArray()) {
			roomModelData.add(room);
		}
	}

	/**
	 * Keeps an instance of Main in order to access the userData
	 * Keeps an instance of the currentStage to allow for stage transitions
	 * @param maincontroller instance of Main
	 * @param currentstage stage that is currently displayed
	 */
	public void setMaincontroller(Main maincontroller, Stage currentstage) {
		this.mainController = maincontroller;
		this.currentStage = currentstage;	
		mainController.getUserModelData().forEach(person -> {
			person.setLocationOptions(setupLocationComboBox(person.getCurrentLocation()));
		});
		moveUsersTableView.setItems(mainController.getUserModelData());

		roomModelData.forEach(room -> {
			room.setObjectBlockingWindowComboBox(setupOjectPresentComboBox(room.isObjectBlockingWindow()));
		});
		objectWindowTableView.setItems(roomModelData);
	}

	/**
	 * Setups a ComboBox for the location of each user in the tableView
	 * The ComboBox contains all locations that were obtained from the houseLayout file
	 * @param currentUserLocation the current location of a user in the house
	 * @return ComboBox with a value set at the current location of the user in the house
	 */
	public ComboBox setupLocationComboBox(String currentUserLocation) {
		ComboBox<String> locationComboBox = new ComboBox<String>();

		//Add all rooms in the ComboBox
		Arrays.stream(houseRoomsModel.getAllRoomsArray()).forEach(room -> {
			locationComboBox.getItems().add(room.getName());
		});
		locationComboBox.getItems().add("outside");   //add outside since users can be placed outside house
		locationComboBox.setValue(currentUserLocation == null ? "outside" : currentUserLocation);	//if user location has not been set, the user will be placed outside by default.
		return locationComboBox;
	}

	/**
	 * Setups a ComboBox for the presence of an object for windows of each room in the tableView
	 * The ComboBox contains "Yes" and "No". 
	 * "Yes" represents that an object is present on window
	 * "No" represents that an object is not present on window
	 * @param isWindowBlocked initial value of the ComboBox
	 * @return
	 */
	public ComboBox setupOjectPresentComboBox(boolean isWindowBlocked) {
		ComboBox<String> objectPresentComboBox = new ComboBox<String>();
		objectPresentComboBox.getItems().add("Yes");
		objectPresentComboBox.getItems().add("No");
		objectPresentComboBox.setValue(isWindowBlocked ? "Yes" : "No");	//if user location has not been set, the user will be placed outside by default.
		return objectPresentComboBox;
	}

	/**
	 * When user clicks the Cancel button, the edit context of simulation stage will be close
	 * @param event user clicks on cancel button
	 */
	public void onCancelClick(MouseEvent event) {
		currentStage.close();
	}

	/**
	 * When user clicks the save button, the edit context of simulation stage will be closed and
	 * the current location of each user as well as the window status of each room will be updated
	 * @param event user clicks on save button
	 */
	public void onSaveClick(MouseEvent event) {
		//User locations
		ArrayList<String> currentLocations = new ArrayList<String>();	//stores location of each user from the tableView
		for (UserModel user : moveUsersTableView.getItems()) {
			currentLocations.add(user.getLocationOptions().getSelectionModel().getSelectedItem());
		}
		String name = mainController.getLoggedUser().getName();
		String currLocation = "";
		//update the current location of each user
		for (int index = 0; index < currentLocations.size(); index++) {
			if (mainController.getUserModelData().get(index).getCurrentLocation() == null) {
				mainController.getUserModelData().get(index).setCurrentLocation(currentLocations.get(index));
			}
			else if (!mainController.getUserModelData().get(index).getCurrentLocation().equals(currentLocations.get(index))) {
				mainController.getUserModelData().get(index).setCurrentLocation(currentLocations.get(index));
			}
			if(mainController.getUserModelData().get(index).getName().equals(name)){
				currLocation = currentLocations.get(index);
			}
		}

		//Windows
		ArrayList<Boolean> windowsStatus = new ArrayList<Boolean>();	//stores true if a window is open and false if closed
		for (RoomModel room : objectWindowTableView.getItems()) {
			if (room.getObjectBlockingWindowComboBox().getSelectionModel().getSelectedItem().equals("Yes")) {
				windowsStatus.add(true);
			}
			else {
				windowsStatus.add(false);
			}
		}
		//update the status of windows for each room in the house
		for (int index = 0; index < windowsStatus.size(); index++) {
			roomModelData.get(index).setObjectBlockingWindow(windowsStatus.get(index).booleanValue());
		}
		mainController.getLoggedUser().setCurrentLocation(currLocation);
		currentStage.close();
		mainController.getDashBoardController().updateLoggedLocation();
		mainController.getDashBoardController().displayLayout();
	}
}
