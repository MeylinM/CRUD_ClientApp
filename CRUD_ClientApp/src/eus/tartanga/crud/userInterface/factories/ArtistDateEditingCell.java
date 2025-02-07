package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Artist;
import java.text.DateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

/**
 * Custom {TableCell} for editing {Date} values in a table of {Artist} objects.
 * <p>
 * This cell displays a date picker when editing, and formats the date when not in editing mode.
 * The date is displayed in the format "dd/MM/yyyy", and the cell allows users to select a date.
 * </p>
 * 
 * @author olaia
 */
public class ArtistDateEditingCell extends TableCell<Artist, Date> {

    private DatePicker dpArtistCell;
    private DateFormat dateFormatter;

    /**
     * Initializes the date picker control for editing dates in the cell.
     * The date picker is set up to allow users to select a date when editing.
     */
    public ArtistDateEditingCell() {
        dpArtistCell = new DatePicker();
    }
    
    /**
     * Updates the content of the cell based on whether it is empty or in editing mode.
     * <p>
     * When not in editing mode, the cell displays the date in the format "dd/MM/yyyy".
     * When in editing mode, the cell displays a {DatePicker} for selecting a new date.
     * </p>
     * 
     * @param date The date to display in the cell.
     * @param empty Indicates whether the cell is empty.
     */
    @Override
    protected void updateItem(Date date, boolean empty) {
        super.updateItem(date, empty);

        dateFormatter = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                dpArtistCell.setValue(date != null
                        ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null);
                setText(null);
                setGraphic(dpArtistCell);
              } else if (date != null) {
                String dateText = dateFormatter.format(date);
                setText(dateText);
                setGraphic(null);
            }
        }
    }
    

    /**
     * Starts the editing process by showing the date picker.
     * <p>
     * When editing starts, the date picker is shown and an event listener is added to commit the new date
     * when the user selects one. If no new date is selected, the previous date is retained.
     * </p>
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            Date previousDate = getItem();
            dpArtistCell.setOnAction((e) -> {
                Date newDate = dpArtistCell.getValue() != null
                        ? Date.from(dpArtistCell.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                        : previousDate; // If no new value is selected, retain the previous date
                commitEdit(newDate);
            });
            setText(null);
            setGraphic(dpArtistCell);
        }
    }

    /**
     * Cancels the editing process and removes the date picker from the cell.
     * The cell returns to its non-editing state.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
    }
}
