package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Concert;
import java.text.DateFormat;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import java.time.ZoneId;
import java.util.Date;

public class ConcertTimeEditingCell extends TableCell<Concert, Date> {

    private DatePicker dpConcertCell;
    private DateFormat dateFormatter;

    public ConcertTimeEditingCell() {
        dpConcertCell = new DatePicker(); // Asegúrate de inicializar el DatePicker
        dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT); // Si necesitas formatear la hora
    }

    @Override
    public void updateItem(Date date, boolean empty) {
        super.updateItem(date, empty);
        if (empty || date == null) {
            setText(null);
        } else {
            // Si el valor es un Date y la columna es de tipo Time
            java.sql.Time time = new java.sql.Time(date.getTime());  // Convierte Date a Time

            // Extraer hora y minutos
            int hours = time.getHours();
            int minutes = time.getMinutes();

            // Formatear la hora (por ejemplo, "HH:mm")
            String formattedTime = String.format("%02d:%02d", hours, minutes);
            setText(formattedTime);
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            Date previousTime = getItem();
            dpConcertCell.setOnAction((e) -> {
                Date newDate = dpConcertCell.getValue() != null
                        ? Date.from(dpConcertCell.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                        : previousTime;
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
