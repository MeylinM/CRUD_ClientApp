package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.exception.WrongDateException;
import eus.tartanga.crud.model.Cart;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

/**
 * Editable cell for the date column in the shopping cart table.
 * <p>
 * This class represents a custom table cell that allows users to edit a date
 * value using a {@link DatePicker}. It validates that the selected date is not
 * earlier than the current date, and handles the conversion between Date and
 * LocalDate.
 * </p>
 *
 * @author Elbire And Meylin
 */
public class CartDateEditingCell extends TableCell<Cart, Date> {

    // The DatePicker used for editing the date.
    private DatePicker dpCartCell;
    // Formatter for displaying the date in a readable format.
    private DateFormat dateFormatter;

    /**
     * Constructor for the CartDateEditingCell. Initializes the DatePicker and
     * sets the default date format.
     */
    public CartDateEditingCell() {
        dpCartCell = new DatePicker();
        dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }

    /**
     * Updates the item displayed in the table cell based on whether the cell is
     * in edit mode or not.
     *
     * @param date The current date value of the cell.
     * @param empty True if the cell is empty, false otherwise.
     */
    @Override
    protected void updateItem(Date date, boolean empty) {
        super.updateItem(date, empty);

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
            } else {
                // When not editing, display the date in text form.
                setText(date != null ? dateFormatter.format(date) : null);
                setGraphic(null);
            }
        }
    }

    /**
     * Starts editing the cell by displaying the DatePicker and handling the
     * date selection.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();

            // Save the previous date value before editing.
            Date previousDate = getItem();
            dpCartCell.setValue(previousDate != null
                    ? previousDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : null);

            dpCartCell.setOnAction((e) -> {
                try {
                    LocalDate selectedDate = dpCartCell.getValue();

                    // Validate if the selected date is not earlier than today or null.
                    validateDate(selectedDate);
                    // Convert the LocalDate to Date and commit the change.
                    Date newDate = selectedDate != null
                            ? Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                            : previousDate;
                    commitEdit(newDate);
                } catch (WrongDateException ex) {
                    // Show error alert if the validation fails.
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Validation Error");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    // Restore the previous date value if validation fails.
                    dpCartCell.setValue(previousDate != null
                            ? previousDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            : null);
                    cancelEdit();
                }
            });

            setText(null);
            setGraphic(dpCartCell);
        }
    }

    /**
     * Cancels editing and restores the original value to the cell if the user
     * cancels the edit.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();

        // Restore the text when editing is canceled.
        Date date = getItem();
        setText(date != null ? dateFormatter.format(date) : null);
        setGraphic(null);
    }

    /**
     * Validates if the selected date is earlier than today's date.
     *
     * @param date The date to validate.
     * @throws WrongDateException If the date is earlier than today's date.
     */
    private void validateDate(LocalDate date) throws WrongDateException {
        if (date == null) {
            throw new WrongDateException("La fecha no puede ser nula.");
        }
        LocalDate today = LocalDate.now();

        // Validate that the date is not earlier than today
        if (date.isBefore(today)) {
            throw new WrongDateException("The date cannot be earlier than today's date.");
        }
    }
}
