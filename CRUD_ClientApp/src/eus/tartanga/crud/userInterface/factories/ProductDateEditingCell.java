/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Product;
import java.text.DateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

/**
 *
 * @author 2dam
 */
public class ProductDateEditingCell extends TableCell<Product, Date> {

    private DatePicker dpProductCell;
    private DateFormat dateFormatter;

    public ProductDateEditingCell() {
        dpProductCell = new DatePicker();
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
                dpProductCell.setValue(date != null
                        ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null);
                setText(null);
                setGraphic(dpProductCell);
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
            dpProductCell.setOnAction((e) -> {
                Date newDate = dpProductCell.getValue() != null
                        ? Date.from(dpProductCell.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                        : previousDate; // Si no hay valor nuevo, conserva la fecha anterior
                commitEdit(newDate);
            });
            setText(null);
            setGraphic(dpProductCell);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
    }
}
