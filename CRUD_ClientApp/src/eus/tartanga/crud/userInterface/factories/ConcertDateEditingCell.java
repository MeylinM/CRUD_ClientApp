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
 *
 * @author Irati
 */
public class ConcertDateEditingCell extends TableCell<Concert, Date> {

    private DatePicker dpConcertCell;
    private DateFormat dateFormatter;

    public ConcertDateEditingCell() {
        dpConcertCell = new DatePicker();
    }

    @Override
    protected void updateItem(Date date, boolean empty) {
        super.updateItem(date, empty);

        dateFormatter = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                dpConcertCell.setValue(date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);
                setText(null);
                setGraphic(dpConcertCell);
            } else if (date != null) {
                String dateText = dateFormatter.format(date);
                setText(dateText);
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            Date previousDate = getItem();
            dpConcertCell.setOnAction((e) -> {
                Date newDate = dpConcertCell.getValue() != null
                        ? Date.from(dpConcertCell.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                        : previousDate;
                commitEdit(newDate);
            });
            setText(null);
            setGraphic(dpConcertCell);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
    }
}
