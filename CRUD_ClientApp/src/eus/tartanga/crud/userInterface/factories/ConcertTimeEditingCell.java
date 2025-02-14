package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Concert;
import java.text.DateFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Celda editable de una tabla en la interfaz de usuario que permite editar la hora
 * de un concierto. Utiliza un {@link TextField} para permitir que el usuario ingrese
 * la hora en formato "HH:mm".
 * 
 * Esta clase extiende {@link TableCell} y gestiona cómo se muestra y edita la hora
 * en una celda de la tabla, mostrando la hora de manera legible y permitiendo la edición
 * con un campo de texto.
 * 
 * @author Irati
 */
public class ConcertTimeEditingCell extends TableCell<Concert, Date> {
    
    private SimpleDateFormat timeFormat = new SimpleDateFormat("K:mm a,z", Locale.US);
    private TextField textField;  // Campo de texto para la edición de la hora

    public ConcertTimeEditingCell() {
        textField = new TextField();
        timeFormat = new SimpleDateFormat("K:mm a,z",  Locale.US);
                
    }

    
    /**
     * Actualiza la celda con un nuevo valor de hora, o la limpia si está vacía.
     * 
     * @param time La hora a mostrar en la celda.
     * @param empty Indica si la celda está vacía.
     */
    @Override
    public void updateItem(Date time, boolean empty) {
        super.updateItem(time, empty);
        if (empty || time == null) {
            setText(null);
        } else {
            setText(timeFormat.format(time)); // Formatear la hora en HH:mm
        }
    }

    /**
     * Inicia la edición de la celda, mostrando un {@link TextField} para que
     * el usuario ingrese una nueva hora en formato "HH:mm".
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            textField = new TextField(getText());
            
            // Detectar eventos de teclas en el campo de texto
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(parseTime(textField.getText()));  // Confirmar la edición
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();  // Cancelar la edición
                }
            });
            
            setText(null);
            setGraphic(textField);
            textField.selectAll();  // Selecciona todo el texto al empezar la edición
        }
    }

    /**
     * Cancela la edición.
     */
    @Override
    public void cancelEdit() {
         super.cancelEdit();

        // Asegurar que se restaure el texto cuando se cancela la edición
        Date time = getItem();
        setText(time != null ? timeFormat.format(time) : null);
        setGraphic(null);
        
        
    }

    /**
     * Parsea una cadena de texto en formato "HH:mm" a un objeto {@link Date}.
     * 
     * @param timeString La cadena de texto que representa la hora a convertir.
     * @return El objeto {@link Date} correspondiente a la hora, o {@code null} si el formato es incorrecto.
     */
    private Date parseTime(String timeString) {
        try {
            Date date = timeFormat.parse(timeString);
            return date;
        } catch (Exception e) {
            return null;  // Si el formato es incorrecto, no modificar el valor
        }
    }
}
