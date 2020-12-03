package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.input.MouseEvent;
import models.RoomModel;
import models.HouseRoomsModel;
import models.TimerPickerModel;
import models.UserModel;

/**
 * This class is responsible for handling 
 * the simulation parameters setup by the user
 * @author Team 4
 *
 */
public class SimulationParametersController {

	@FXML private Label timeLabel;
	@FXML private DatePicker  dateSelected;
	@FXML private TableView<UserModel> allUsers;
	@FXML private TableColumn<UserModel, String> colname;
	@FXML private TableColumn<UserModel, String> colrole;
	@FXML private JFXComboBox roomLocation;
	@FXML private JFXComboBox selectSeason;
	private Main mainController;
	private HouseRoomsModel houseRoomsModel = HouseRoomsModel.getInstance();

	/**
	 * Initalize the values of the table that contains the user names and roles
	 * & the room names extracted from the house layout file
	 * set the date to current date and time to current time by default
	 */
	public void initialize(){
		colname.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
		colrole.setCellValueFactory(new PropertyValueFactory<UserModel, String>("role"));
		ObservableList<String> locationNames= FXCollections.observableArrayList();
		for(RoomModel r : houseRoomsModel.getAllRoomsArray()){
			locationNames.add(r.getName());
		}
		ObservableList<String> seasonOptions =
				FXCollections.observableArrayList(
						"Summer",
						"Winter"
				);
		roomLocation.setItems(locationNames);
		dateSelected.setValue(LocalDate.now());
		LocalTime pickTime = LocalTime.now();
		timeLabel.setText(pickTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString());
		selectSeason.setItems(seasonOptions);
	}

	/**
	 * This method sets the controller provided in the parameter to replace the
	 * main controller so that it can get access to all the users from the user model.
	 * @param controller controller that will replace the main controller
	 */
	public void setMainController(Main controller ) {
		this.mainController = controller;
		allUsers.setItems(mainController.getUserModelData());
	}

	/**
	 * This method handles when there is a mouse click on the edit label that will open the
	 * user management window
	 * @param event on mouse click
	 */
	public void handleEditUser(ActionEvent event) {
		try{
			mainController.setUserManagerWindow();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method handles when the continue button is clicked. It will verify that all the necessary
	 * simulation parameters are set (date, time, user and location).
	 * @param mouseEvent on mouse click
	 */
	public void onContinueClick(MouseEvent mouseEvent) {
		UserModel userSelected = allUsers.getSelectionModel().getSelectedItem();
		if (dateSelected != null && userSelected != null && roomLocation.getValue() != null && timeLabel.getText()!=null) { // ADD TIMER HERE
			mainController.setLoggedUser(userSelected);
			mainController.getLoggedUser().setDate(dateSelected.getValue());
			mainController.getLoggedUser().setCurrentLocation(roomLocation.getValue().toString());
			mainController.getLoggedUser().setTime(LocalTime.parse(timeLabel.getText()));
			mainController.getLoggedUser().setSeason((String) selectSeason.getValue());
//			for(int i = 0; i < houseRoomsModel.getAllRoomsArray().length;  i++){
//				houseRoomsModel.getAllRoomsArray()[i].getTemperature().;
//			}
			mainController.closeWindow();
			try {
				mainController.setDashboardWindow();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Selections");
			alert.setHeaderText("Missing Selections.");
			alert.setContentText("You must select a date,time, user and location to proceed. \nPlease try again.");
			alert.showAndWait();
		}
	}

	/**
	 * display the dialog and set the label text
	 * @param event
	 */
	public void setTime(ActionEvent event) {
		TimerPickerModel timeDialog = new TimerPickerModel();
		Dialog<LocalTime> updateTime = timeDialog.getTimePicker();
		updateTime.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK && timeDialog.getHourList().getValue()!=null &&timeDialog.getMinList().getValue()!=null) {
				LocalTime pickTime = LocalTime.parse(timeDialog.getHourList().getValue()+":"+timeDialog.getMinList().getValue()+":00", DateTimeFormatter.ofPattern("HH:mm:ss"));
				timeLabel.setText(pickTime.toString());
			}
			return null;
		});
		updateTime.showAndWait();
	}

	/**
	 * On the simulation parameters page, when the user clicks the help icon, the explanation of the permissions window appears
	 * @param event user clicks the help icon
	 */
	public void onPermissionsClick(MouseEvent event) {
		try {
			mainController.setPermissionsWindow();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
