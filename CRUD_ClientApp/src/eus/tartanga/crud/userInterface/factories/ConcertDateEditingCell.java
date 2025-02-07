/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Concert;
import java.text.DateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

/**
 * Celda editable de una tabla en la interfaz de usuario que permite editar fechas
 * asociadas a conciertos. Utiliza un {@link DatePicker} para permitir que el
 * usuario seleccione una fecha. 
 * 
 * Esta clase extiende {@link TableCell} y gestiona cómo se muestra y edita la fecha
 * en una celda de la tabla, mostrando la fecha de manera legible y permitiendo la edición
 * con un selector de fecha.
 * 
 * @author Irati
 */
public class ConcertDateEditingCell extends TableCell<Concert, Date> {

    private DatePicker dpConcertCell;  // Selector de fecha
    private DateFormat dateFormatter;  // Formato de la fecha

    /**
     * Constructor de la clase. Inicializa el {@link DatePicker} utilizado para
     * la edición de la fecha en la celda.
     */
    public ConcertDateEditingCell() {
        dpConcertCell = new DatePicker();
    }

    /**
     * Actualiza la celda con un nuevo valor de fecha, o la limpia si está vacía.
     * 
     * @param date La fecha a mostrar en la celda.
     * @param empty Indica si la celda está vacía.
     */
    @Override
    protected void updateItem(Date date, boolean empty) {
        super.updateItem(date, empty);

        // Formato de fecha que se utilizará para mostrar la fecha
        dateFormatter = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                // Si la celda está en modo edición, mostramos el DatePicker
                dpConcertCell.setValue(date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);
                setText(null);
                setGraphic(dpConcertCell);
            } else if (date != null) {
                // Si no está en edición, mostramos la fecha formateada
                String dateText = dateFormatter.format(date);
                setText(dateText);
                setGraphic(null);
            }
        }
    }

    /**
     * Inicia la edición de la celda, mostrando el {@link DatePicker} para que
     * el usuario seleccione una nueva fecha.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            Date previousDate = getItem();
            dpConcertCell.setOnAction((e) -> {
                // Cuando se selecciona una nueva fecha, se confirma la edición
                Date newDate = dpConcertCell.getValue() != null
                        ? Date.from(dpConcertCell.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                        : previousDate;
                commitEdit(newDate);
            });
            setText(null);
            setGraphic(dpConcertCell);
        }
    }

    /**
     * Cancela la edición y restaura el contenido original de la celda.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
    }
}
