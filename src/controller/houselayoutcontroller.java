package controller;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import helper.Room;
import helper.allRoom;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Scanner;

public class houselayoutcontroller {


    public JFXButton todashboardbtn;
    private Maincontroller maincontroller ;
    public void setMaincontroller(Maincontroller maincontroller) {
        this.maincontroller = maincontroller;
    }

    private String pathtofile;
    @FXML private Label pathtofilelabel;



    public String readfromfile(String url){
        String jsonstring="";
        try{
            File file = new File(url);
            Scanner readfile = new Scanner(file);
            while(readfile.hasNextLine()){
                jsonstring +=readfile.nextLine()+"\r\n";
            }
            readfile.close();
            return jsonstring;
        } catch (Exception e) {
            System.out.println("The file can not be founded!");
        }
        return null;
    }

    public void choosefile(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        File choosenfile = filechooser.showOpenDialog(null);
        if(choosenfile != null){
            pathtofile=choosenfile.getAbsolutePath();
            pathtofilelabel.setText(pathtofile);
            todashboardbtn.setDisable(false);
        }

    }


    public Room[] extractfromjson(String jsontext){
        Room[] arrayroom = new Gson().fromJson(jsontext, Room[].class);
        return arrayroom;

    }


    public void todashboard(ActionEvent event) {
        // handle the txt file then proceed to dash board and
        String jsonstring = readfromfile(pathtofile);
        Room[] aroom = extractfromjson(jsonstring);
        allRoom.setAllroom(aroom);

//        for(Room i : allRoom.getAllroom()){
//            System.out.println(i);
//        }
        maincontroller.closewindow();
        try {

            maincontroller.setcontextsimulationwindow();
            // have to keep track of user type

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
