package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Concert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConcertTimeEditingCell extends TableCell<Concert, Date> {
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private TextField textField;

    @Override
    public void updateItem(Date time, boolean empty) {
        super.updateItem(time, empty);
        if (empty || time == null) {
            setText(null);
        } else {
            setText(timeFormat.format(time)); // Formatear la hora en HH:mm
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            textField = new TextField(getText());
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(parseTime(textField.getText()));
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
            setText(null);
            setGraphic(textField);
            textField.selectAll(); // Selecciona todo el texto al empezar la edición
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(timeFormat.format(getItem())); // Restaurar el texto cuando se cancela la edición
        setGraphic(null);
    }

    private Date parseTime(String timeString) {
        try {
            Date date = timeFormat.parse(timeString);
            return date;
        } catch (Exception e) {
            return null; // Si el formato es incorrecto, no modificar el valor
        }
    }
}
