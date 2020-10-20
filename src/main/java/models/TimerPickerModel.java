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
 * @author Team 4
 */
public class TimerPickerModel {

	private Dialog<LocalTime> timePicker;
	JFXComboBox<String> hourList;
	JFXComboBox<String> minList;

	/**
	 * Constructor to create Time picker model
	 */
	public TimerPickerModel() {
		timePicker = new Dialog<>();
		timePicker.setHeaderText("Set or change Time");
		ObservableList<String> h = FXCollections.observableArrayList();
		ObservableList<String> min = FXCollections.observableArrayList();
		for(int i=0;i<24;i++){
			if(i<10){
				h.add("0"+i);
			}
			else{
				h.add(i+"");
			}
		}
		for(int i=0;i<=59;i++){
			if(i<10){
				min.add("0"+i);
			}
			else{
				min.add(i+"");
			}
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

	/**
	 * Getter to obtain the time picker dialog
	 * @return time picker dialog
	 */
	public Dialog<LocalTime> getTimePicker() {
		return timePicker;
	}

	/**
	 * Getter to obtain the list of hours comboBox
	 * @return combBox with list of hours
	 */
	public JFXComboBox<String> getHourList() {
		return hourList;
	}

	/**
	 * Getter to obtain the list of minutes comboBox
	 * @return combBox with list of minutes
	 */
	public JFXComboBox<String> getMinList() {
		return minList;
	}
}
