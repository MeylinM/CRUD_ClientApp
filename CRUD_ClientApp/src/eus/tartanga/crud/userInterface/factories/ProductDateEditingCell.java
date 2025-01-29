package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.exception.WrongDateException;
import eus.tartanga.crud.model.Product;
import eus.tartanga.crud.userInterface.controllers.ProductViewController;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

/**
 *
 * @author Elbire
 */
public class ProductDateEditingCell extends TableCell<Product, Date> {

    private DatePicker dpProductCell;
    private DateFormat dateFormatter;

    public ProductDateEditingCell() {
        dpProductCell = new DatePicker();
        dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }

    @Override
    protected void updateItem(Date date, boolean empty) {
        super.updateItem(date, empty);

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
            } else {
                // Asegurar que el texto siempre se muestra cuando no estamos editando
                setText(date != null ? dateFormatter.format(date) : null);
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();

            // Guardar el valor previo
            Date previousDate = getItem();
            dpProductCell.setValue(previousDate != null
                    ? previousDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : null);

            dpProductCell.setOnAction((e) -> {
                try {
                    LocalDate selectedDate = dpProductCell.getValue();

                    // Validar si la fecha está en el rango permitido
                    validateDate(selectedDate);

                    // Convertir LocalDate a Date y realizar commit
                    Date newDate = selectedDate != null
                            ? Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                            : previousDate;
                    commitEdit(newDate);
                } catch (WrongDateException ex) {
                    // Mostrar alerta de error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de validación");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();

                    // Restaurar el valor previo
                    dpProductCell.setValue(previousDate != null
                            ? previousDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            : null);
                    cancelEdit();
                }
            });

            setText(null);
            setGraphic(dpProductCell);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        // Asegurar que se restaure el texto cuando se cancela la edición
        Date date = getItem();
        setText(date != null ? dateFormatter.format(date) : null);
        setGraphic(null);
    }

    private void validateDate(LocalDate date) throws WrongDateException {
        if (date == null) {
            throw new WrongDateException("La fecha no puede ser nula.");
        }

        int year = date.getYear();
        if (year < 1980 || year > 2030) {
            throw new WrongDateException("La fecha debe estar entre los años 1980 y 2030.");
        }
    }
}
