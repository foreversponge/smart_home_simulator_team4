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
     * help to keep an instance of Main so we can call closeWindow method to close the current screen and load another screen
     * @param mainController
     */
    public void setMainController(Main mainController) {
        this.mainController = mainController;
    }
    /**
     * method is called when the upload button is clicked
     * then set the continue button enable to clicked
     * @param mouseEvent
     */
    public void onUploadClick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload House Layout File");
        chosenFile = fileChooser.showOpenDialog(null);
        if(chosenFile != null) {
            pathToFile = chosenFile.getAbsolutePath();
            pathToFileLabel.setText(pathToFile);
            continueButton.setDisable(false);
        }
    }
    /**
     * help to read all content of the file that is choosen by the user
     * @param url
     * @return
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
     * help to extract all the object by converting the json object to RoomModel object
     * @param jsonText
     * @return
     */
    public RoomModel[] extractFromJson(String jsonText){
        RoomModel[] arrayRoomModel = new Gson().fromJson(jsonText, RoomModel[].class);
        return arrayRoomModel;
    }
    /**
     * set the static HouseRoomModel with the data we get from the user
     * It is static because we gonna use it for whole application
     * then use mainController to clsoe the current window and open another window
     * @param mouseEvent
     */
    public void onContinueClick(MouseEvent mouseEvent) {
        String jsonString = readFromFile(pathToFile);
        RoomModel[] allRoomModels = extractFromJson(jsonString);
        HouseRoomsModel.setAllRooms(allRoomModels);
        mainController.CloseWindow();
        try{
            mainController.setSimulatorParameterWindow();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}