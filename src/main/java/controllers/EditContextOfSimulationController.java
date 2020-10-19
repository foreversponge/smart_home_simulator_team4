package controllers;

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
 * @author Team 4
 *
 */
public class EditContextOfSimulationController {

	private Main mainController;	//instance of Main to obtain userData
	private Stage currentStage;		//instance of the current Stage to open or close it
	private HouseRoomsModel houseRoomsModel;	//contains data about each room of the house
	@FXML private TableView<UserModel> tableView;	//Place Users tableView of EditContextOfSimuatlion.fxml
	@FXML private TableColumn nameOfUser;		//Name of user column of tableView of EditContextOfSimuatlion.fxml
	@FXML private TableColumn permission;		//Permission column of tableView of EditContextOfSimuatlion.fxml
	@FXML private TableColumn locationColumn;	//location of user column of tableView of EditContextOfSimuatlion.fxml

	private ObservableList<RoomModel> roomData = FXCollections.observableArrayList();
	@FXML private TableView<RoomModel> objectWindowTableView;	
	@FXML private TableColumn roomNameColumn;	
	@FXML private TableColumn windowNumColumn;	
	@FXML private TableColumn objectPresentColumn;

	/**
	 * Setup of table (columns) with attributes from the User class
	 */
	public void initialize(){
		nameOfUser.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
		permission.setCellValueFactory(new PropertyValueFactory<UserModel, String>("status"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<UserModel, ComboBox<String>>("location"));

		//Windows
		roomNameColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, String>("name"));
		windowNumColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, String>("numWindows"));
		objectPresentColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, ComboBox<String>>("objectBlockingWindowComboBox"));
		for (RoomModel room : houseRoomsModel.getAllRoomsArray()) {
			roomData.add(room);
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
		mainController.getPersonData().forEach(person -> {
			person.setLocation(setupComboBox(person.getCurrentLocation()));
		});
		tableView.setItems(mainController.getPersonData());
		
		roomData.forEach(room -> {
			room.setObjectBlockingWindowComboBox(setupOjectPresentComboBox(room.isObjectBlockingWindow()));
		});
		objectWindowTableView.setItems(roomData);
	}

	/**
	 * Setups a ComboBox for the location of each user in the tableView
	 * The ComboBox contains all locations that were obtained from the houseLayout file
	 * @param currentUserLocation the current location of a user in the house
	 * @return ComboBox with a value set at the current location of the user in the house
	 */
	public ComboBox setupComboBox(String currentUserLocation) {
		ComboBox<String> locationComboBox = new ComboBox<String>();

		//Add all rooms in the ComboBox
		Arrays.stream(houseRoomsModel.getAllRoomsArray()).forEach(room -> {
			locationComboBox.getItems().add(room.getName());
		});

		locationComboBox.getItems().add("outside");   //add outside since users can be placed outside house
		locationComboBox.setValue(currentUserLocation == null ? "outside" : currentUserLocation);	//if user location has not been set, the user will be placed outside by default.
		return locationComboBox;
	}
	
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
		System.out.println(houseRoomsModel.getAllRoomsArray()[0].getName() + " " + houseRoomsModel.getAllRoomsArray()[0].getNumWindows());
		System.out.println(houseRoomsModel.getAllRoomsArray()[1].getName());
		currentStage.close();
	}

	/**
	 * When user clicks the save button, the edit context of simulation stage will be closed and
	 * the current location of each user will be updated
	 * @param event user clicks on save button
	 */
	public void onSaveClick(MouseEvent event) {
		ArrayList<String> currentLocations = new ArrayList<String>();	//stores location of each user from the tableView
		for (UserModel user : tableView.getItems()) {
			currentLocations.add(user.getLocation().getSelectionModel().getSelectedItem());
		}

		//update the current location of each user
		for (int index = 0; index < currentLocations.size(); index++) {
			mainController.getPersonData().get(index).setCurrentLocation(currentLocations.get(index));
		}
		//TODO - Before closing stage update house layout with new user locations
		currentStage.close();
	}

}
