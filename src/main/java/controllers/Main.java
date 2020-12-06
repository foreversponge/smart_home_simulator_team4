package controllers;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.LogMessageModel;
import models.Observer;
import models.SHPModel;
import models.UserModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for running the application and
 * opening the various windows and scenes
 * @author Team 4
 *
 */
public class Main extends Application {

	private Stage currentState;
	private ObservableList<UserModel> userModelData = FXCollections.observableArrayList();
	private ObservableList<UserModel> tempUserModelData = FXCollections.observableArrayList();
	private ObservableList<LogMessageModel> logMessageModels = FXCollections.observableArrayList();
	private UserModel loggedUser;
	private dashBoardController dashBoardController;
	private SHPModel shpModel = new SHPModel();

	/**
	 * Launches the simulator
	 * if the there is user from text file that save in the previous launching app
	 * set the userModelData and tempUserModelData to that value
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		currentState = primaryStage;
		UserModel[] all= new Gson().fromJson(readFromFile(), UserModel[].class);
		if(all!=null){
			for(UserModel u: all){
				u.setListOfObservers(new ArrayList<Observer>());
				userModelData.add(u);
				tempUserModelData.add(u);
				u.registerObserver(shpModel);	//each user observes the SHP Model
			}
		}
		try {
			setHouseLayoutWindow();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * to read the user and permission from file
	 * avoid add every time launching the app
	 * @return
	 */
	public String readFromFile(){
		String jsonString="";
		try{
			File file = new File("allUser.txt");
			Scanner readFile = new Scanner(file);
			while(readFile.hasNextLine()){
				jsonString +=readFile.nextLine()+"\r\n";
			}
			readFile.close();
			return jsonString;
		} catch (Exception e) {
			System.out.println("The file can not be found.");
		}
		return null;
	}

	/**
	 * getter method of the UserModelData that is ObservableList of the all the User in the application
	 * @return
	 */
	public ObservableList<UserModel> getUserModelData() {
		return userModelData;
	}

	/**
	 * getter method of the TempUserModelData, which is use for User Management window
	 * @return
	 */
	public ObservableList<UserModel> getTempUserModelData() {
		return tempUserModelData;
	}

	/**
	 * getter to get the ObservableList of LogMessages
	 * @return
	 */
	public ObservableList<LogMessageModel> getLogMessages() {
		return logMessageModels;
	}

	/**
	 * close the current window
	 */
	public void closeWindow() {
		currentState.close();
	}

	/**
	 * getter method to get the Logged User
	 * @return
	 */
	public UserModel getLoggedUser() {
		return this.loggedUser;
	}
	/**
	 * setter method to set the Logged User
	 * @return
	 */
	public void setLoggedUser(UserModel loggedUser) {
		this.loggedUser = loggedUser;
	}

	/**
	 * getter to get the dashboardController
	 * @return
	 */
	public dashBoardController getDashBoardController() {
		return dashBoardController;
	}

	/**
	 * set house layout window
	 * @throws IOException
	 */
	public void setHouseLayoutWindow() throws IOException {
		currentState.setTitle("Smart Home Simulator");
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/houseLayout.fxml"));
		Parent root = fxmlLoader.load();
		HouseLayoutController houseLayout = fxmlLoader.getController();
		houseLayout.setMainController(this);
		Scene houseLayoutScene = new Scene(root);
		currentState.setScene(houseLayoutScene);
		currentState.show();
	}

	/**
	 * method to open Simulation parameter Window
	 * @throws IOException
	 */
	public void setSimulationParametersWindow() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/simulationParameters.fxml"));
		Parent root = fxmlLoader.load();
		SimulationParametersController simParController = fxmlLoader.getController();
		simParController.setMainController(this);
		Scene simulationParametersScene = new Scene(root);
		currentState.setScene(simulationParametersScene);
		currentState.show();
	}

	/**
	 * method to open the user manager window
	 * when you close this window you still on the simulator parameter window
	 * @throws IOException
	 */
	public void setUserManagerWindow() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/userManager.fxml"));
		Parent root = fxmlLoader.load();
		UserManagerController simParcontroller = fxmlLoader.getController();
		Stage editstage = new Stage();
		simParcontroller.setMaincontroller(this, editstage);
		editstage.initOwner(currentState);
		tempUserModelData.clear();
		userModelData.forEach((userModel)->{
			tempUserModelData.add(userModel);
		});
		editstage.initModality(Modality.WINDOW_MODAL);
		Scene userManager = new Scene(root);
		editstage.setScene(userManager);
		editstage.show();
	}

	/**
	 * method to load the DashBoard Window
	 * @throws IOException
	 */
	public void setDashboardWindow() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/dashBoard.fxml"));
		Parent root = fxmlLoader.load();
		dashBoardController = fxmlLoader.getController();
		dashBoardController.setMainController(this,currentState);
		Scene simScene = new Scene(root);
		currentState.setScene(simScene);
		currentState.show();
	}

	/**
	 * method to open the edit Context of Simulation window
	 * when closing this window, the user is returned to the dashboard
	 * @throws IOException
	 */
	public void setEditContextWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EditContextOfSimulation.fxml"));
		Parent root = fxmlLoader.load();
		EditContextOfSimulationController editContextController = fxmlLoader.getController();
		Stage editContextStage= new Stage();
		editContextController.setMaincontroller(this, editContextStage);
		editContextStage.initOwner(currentState);
		editContextStage.setTitle("Edit Context of Simulation");
		editContextStage.initModality(Modality.WINDOW_MODAL);
		Scene editContextScene = new Scene(root);
		editContextStage.setScene(editContextScene);
		editContextStage.show();
	}

	/**
	 * Method that allows you to set the room temperature
	 * @throws IOException
	 */
	public void setRoomTemperatureWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/setRoomTemperature.fxml"));
		Parent root = fxmlLoader.load();
		Stage setRoomTemperatureStage= new Stage();
		setRoomTemperatureStage.initOwner(currentState);
		setRoomTemperatureStage.setTitle("Set Room Temperature");
		setRoomTemperatureStage.initModality(Modality.WINDOW_MODAL);
		Scene setRoomTemperatureScene = new Scene(root);
		setRoomTemperatureStage.setScene(setRoomTemperatureScene);
		setRoomTemperatureStage.show();
	}
	
	/**
	 * Getter to obtain the SHPModel
	 * @return SHPModel
	 */
	public SHPModel getShpModel() {
		return shpModel;
	}

	/**
	 * Method to open the permissions explained window
	 * when closing this window, the user is returned to the simulation parameter window
	 * @throws IOException
	 */
	public void setPermissionsWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/permissionsExplained.fxml"));
		Parent root = fxmlLoader.load();
		Stage permissionsStage = new Stage();
		permissionsStage.initOwner(currentState);
		permissionsStage.setTitle("Roles and Permissions");
		permissionsStage.initModality(Modality.WINDOW_MODAL);
		Scene permissionsScene = new Scene(root);
		permissionsStage.setScene(permissionsScene);
		permissionsStage.setResizable(false);
		permissionsStage.show();
	}

	/**
	 * Method to open the setup of lights to remain on window
	 * when closing this window, the user is returned to the dashboard
	 * @throws IOException
	 */
	public void setLightsToRemainOpenWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/lightsToRemainOn.fxml"));
		Parent root = fxmlLoader.load();
		LightsToRemainOnController lightsToRemainOnController = fxmlLoader.getController();
		Stage lightsToRemainOnStage = new Stage();
		lightsToRemainOnController.setCurrentStage(lightsToRemainOnStage);
		lightsToRemainOnController.setCurrentDashboard(dashBoardController);
		lightsToRemainOnStage.initOwner(currentState);
		lightsToRemainOnStage.setTitle("Lights to Remain On");
		lightsToRemainOnStage.initModality(Modality.WINDOW_MODAL);
		Scene lightsToRemainOnScene = new Scene(root);
		lightsToRemainOnStage.setScene(lightsToRemainOnScene);
		lightsToRemainOnStage.setResizable(false);
		lightsToRemainOnStage.show();
	}

	/**
	 * set the zone Room set up window
	 * to add new zone and add room to the existing zone
	 * @param shhCont
	 * @throws IOException
	 */
	public void setZoneRoomWindow(SHHController shhCont) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/setUpZoneRoom.fxml"));
		Parent root = fxmlLoader.load();
		setUpZoneRoomController setUpZoneRoomController = fxmlLoader.getController();
		Stage editstage = new Stage();
		setUpZoneRoomController.setMaincontroller(this, editstage, shhCont);
		editstage.initOwner(currentState);
		editstage.initModality(Modality.WINDOW_MODAL);
		Scene userManager = new Scene(root);
		editstage.setScene(userManager);
		editstage.show();
	}
}

