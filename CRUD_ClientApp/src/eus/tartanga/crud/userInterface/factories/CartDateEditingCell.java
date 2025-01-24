package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Cart;
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
public class CartDateEditingCell extends TableCell<Cart, Date> {

    private DatePicker dpProductCell;
    private DateFormat dateFormatter;

    public CartDateEditingCell() {
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
