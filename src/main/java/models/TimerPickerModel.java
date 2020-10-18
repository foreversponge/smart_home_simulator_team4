package models;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import java.time.LocalTime;

/**
 * create the timepicker Dialog
 */
public class TimerPickerModel {

    private Dialog<LocalTime> timePicker;
    JFXComboBox<Integer> hourList;
    JFXComboBox<Integer> minList;

    public TimerPickerModel() {
        timePicker = new Dialog<>();
        timePicker.setHeaderText("Set or change Time");
        ObservableList<Integer> h = FXCollections.observableArrayList();
        ObservableList<Integer> min = FXCollections.observableArrayList();
        for(int i=1;i<=24;i++){
            h.add(i);
        }
        for(int i=0;i<=59;i++){
            min.add(i);
        }
        hourList = new JFXComboBox<>();
        minList = new JFXComboBox<>();
        hourList.setItems(h);
        minList.setItems(min);
        GridPane gridPane = new GridPane();
        gridPane.add(hourList, 1, 1);
        gridPane.add(minList, 2, 1);
        DialogPane timeDialogPane = timePicker.getDialogPane();
        timeDialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        timeDialogPane.setContent(gridPane);
    }
    public Dialog<LocalTime> getTimePicker() {
        return timePicker;
    }
    public JFXComboBox<Integer> getHourList() {
        return hourList;
    }
    public JFXComboBox<Integer> getMinList() {
        return minList;
    }
}
