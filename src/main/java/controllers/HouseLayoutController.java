package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;
import com.google.gson.Gson;
import javafx.scene.control.Label;
import models.RoomModel;
import models.HouseRoomsModel;
import java.util.Scanner;

public class HouseLayoutController {

    @FXML private JFXButton continueButton;
    private String pathToFile = null;
    @FXML private Label pathToFileLabel;
    private File chosenFile;
    private Main mainController;

    /**
     * This method sets the controller provided in the parameter to replace the
     * main controller so that it can get access to all the users from the user model.
     * @param mainController controller that will replace the main controller
     */
    public void setMainController(Main mainController) {
        this.mainController = mainController;
    }

    /**
     * This method handles when the upload button is clicked. It will prompt a window to
     * let choose a file in the computer system.
     * @param mouseEvent on mouse click
     */
    public void onUploadClick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload House Layout File");
        File chosenFile = fileChooser.showOpenDialog(null);
        if(chosenFile != null) {
            pathToFile = chosenFile.getAbsolutePath();
            pathToFileLabel.setText(pathToFile);
            continueButton.setDisable(false);
        }
    }

    /**
     * This method will read the file that is chosen to be uploaded. It creates a string that will be
     * a replica of the file.
     * @param url path to the file
     * @return jsonString that represents the file
     */
    public String readFromFile(String url){
        String jsonString="";
        try{
            File file = new File(url);
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
     * This method will extract the information from the json and place it in the room model array.
     * @param jsonText string to be read
     * @return array from room model that contains all the rooms in the house layout file.
     */
    public RoomModel[] extractFromJson(String jsonText){
        RoomModel[] arrayRoomModel = new Gson().fromJson(jsonText, RoomModel[].class);
        return arrayRoomModel;
    }

    /**
     * This method extracts the information from the file provided and creates data
     * in the Room model. It will then make the window switch to the simulation parameters window.
     * @param mouseEvent on mouse click
     */
    public void onContinueClick(MouseEvent mouseEvent) {
        String jsonString = readFromFile(pathToFile);
        RoomModel[] allRoomModels = extractFromJson(jsonString);
        HouseRoomsModel.setAllRooms(allRoomModels);
        mainController.closeWindow();
        try{
            mainController.setSimulationParametersWindow();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}