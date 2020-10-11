package controller;

import com.google.gson.Gson;
import com.jfoenix.controls.*;
import helper.Room;
import helper.allRoom;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class dashboardcontroller implements Initializable {

    public ImageView window1;
    public ImageView door1;
    public ImageView light1;
    @FXML private AnchorPane anchorpaneroom1;
    @FXML private Label room1;
    @FXML private JFXButton togglelightbtn;
    LocalTime currentime;
    LocalDate currentdate;
    IncTask mytask;
    Timer timer;

     @FXML
    private StackPane stackpaneroot;
    @FXML
    private Label time;
    @FXML
    private Label date;
    @FXML
    private JFXDatePicker datepicker;
    @FXML
    private JFXTimePicker timepicker;

//    public void timepicker(ActionEvent event){
//        JFXTimePicker timpepick = new JFXTimePicker();
//        timpepick.setOnAction(event1 -> {
//            timepickerhandler(event);
//                }
//        );
//
//    }
    // read from file and create the object of room
    // display on the stack pane too
    public void readfromfile(String url){
        try{
            File file = new File(url);
            String data="";
            Scanner readfile = new Scanner(file);
            while(readfile.hasNextLine()){
                data +=readfile.nextLine()+"\r\n";
            }
            readfile.close();
            System.out.println(data);
        } catch (Exception e) {
            System.out.println("The file can not be founded!");

        }

    }

    public void togglelight(ActionEvent event){
        String mode =  ((JFXButton)event.getSource()).getText();
        if(mode.equals("Off")){
            togglelightbtn.setText("On");
            light1.setImage(new Image("file:src/image/lighton.png"));
            // call to display the light
        }
        else {
            togglelightbtn.setText("Off");
            light1.setImage(new Image("file:src/image/lightoff.png"));
        }
    }



    public void timepickerhandler(ActionEvent event) {
        if(timer!=null){
            timer.cancel();
            timer = new Timer(true);
        }
        mytask=new IncTask();
        LocalTime picktime = timepicker.getValue();
        mytask.settime(LocalTime.parse(picktime.format(DateTimeFormatter.ofPattern("hh:mm:ss"))));
        timer.scheduleAtFixedRate(mytask,1000,1000);

    }

    public void datepickerhandler(ActionEvent event) {
        LocalDate pickedate = datepicker.getValue();
        date.setText(pickedate.toString());
    }

    public void addlayoutdialog(ActionEvent event) {
        Stage stage = (Stage) stackpaneroot.getScene().getWindow();
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("choose file"));
        JFXButton choosefile = new JFXButton("choose file");
        JFXButton cancel = new JFXButton("cancel");
        JFXDialog dialog = new JFXDialog(stackpaneroot,content, JFXDialog.DialogTransition.CENTER);
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        choosefile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = new FileChooser().showOpenDialog(stage);

                if (file != null) {
//                    System.out.println(file.getAbsolutePath());
                    readfromfile(file.getAbsolutePath());
                    dialog.close();
//                    label.setText(file.getAbsolutePath()
//                            + "  selected");
                    String json = "[{'name':'kitchen','windownum': 2, 'doornum': 1, 'lightnum': 2, 'nextroomname':'livingroom'},{'name':'livingroom','windownum': 1, 'doornum': 2, 'lightnum': 3, 'nextroomname':'bedroom'}]";
                    Room[] obj = new Gson().fromJson(json, Room[].class);
                    for(Room i: obj)
                    System.out.println(i);
                }
            }
        });
        content.setActions(choosefile,cancel);

        dialog.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mytask=new IncTask();
        timer =new Timer(true);
        currentdate=LocalDate.now();
        date.setText(currentdate.toString());
        mytask.settime(LocalTime.now());
        timer.scheduleAtFixedRate(mytask,1000,1000);

        String json = "[{'name':'kitchen','windownum': 2, 'doornum': 1, 'lightnum': 2, 'nextroomname':'livingroom'},{'name':'livingroom','windownum': 1, 'doornum': 2, 'lightnum': 3, 'nextroomname':'bedroom'}]";
        Room[] obj = new Gson().fromJson(json, Room[].class);
        allRoom.setAllroom(obj);
        Room[] allr = allRoom.getAllroom();
        int index=1;
        for(Room i: allr){
            switch (index){
                case 1:
                    room1.setText(i.getName());
                    window1.setImage(new Image("file:src/image/closewindow.png"));
                    light1.setImage(new Image("file:src/image/lightoff.png"));
                    door1.setImage(new Image("file:src/image/closedoor.png"));
                    anchorpaneroom1.setVisible(true);
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            index++;
        }
    }




    class IncTask extends TimerTask{
        public LocalTime loctime;

        public LocalTime getSettime() {
            return loctime;
        }

        public void settime(LocalTime settime) {
            this.loctime = settime;
        }
        @Override
        public void run() {
            Platform.runLater(new Runnable() {// it has to run from the fx application
                @Override
                public void run() {
                    loctime = loctime.plusSeconds(1);
                    time.setText(loctime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                }
            });

        }
    }
}
