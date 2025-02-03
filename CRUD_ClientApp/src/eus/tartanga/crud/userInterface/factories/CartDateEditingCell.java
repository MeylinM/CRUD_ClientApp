package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Cart;
import java.text.DateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;

/**
 * Celda editable para la columna de fecha en la tabla de carrito.
 */
public class CartDateEditingCell extends TableCell<Cart, Date> {

    private DatePicker dpCartCell;
    private DateFormat dateFormatter;
    private Date previousDate;

    public CartDateEditingCell() {
        dpCartCell = new DatePicker();
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
                dpCartCell.setValue(date != null
                        ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null);
                setText(null);
                setGraphic(dpCartCell);
            } else if (date != null) {
                setText(dateFormatter.format(date));
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            previousDate = getItem(); // Guardamos la fecha anterior
            Date today = new Date(); // Fecha actual

            dpCartCell.setValue(previousDate != null
                    ? previousDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            // Manejar cuando el usuario elige una fecha
            dpCartCell.setOnAction(event -> validateAndCommitDate());

            // Manejar cuando el usuario presiona Enter
            dpCartCell.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    validateAndCommitDate();
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });

            setText(null);
            setGraphic(dpCartCell);
        }
    }

    private void validateAndCommitDate() {
        Date today = new Date();
        Date selectedDate = Date.from(dpCartCell.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (selectedDate.before(today)) {
            showAlert("Fecha inválida", "No puedes seleccionar una fecha anterior a la actual.");
            dpCartCell.setValue(previousDate != null
                    ? previousDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            commitEdit(selectedDate);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(previousDate != null ? dateFormatter.format(previousDate) : null);
        setGraphic(null);
    }
}
