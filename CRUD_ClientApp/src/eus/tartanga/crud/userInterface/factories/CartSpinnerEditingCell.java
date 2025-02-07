package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Cart;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;

/**
 * A custom TableCell that allows for editing the quantity of an item in a Cart
 * using a Spinner. This class enables users to select a value for an item in
 * the cart through a spinner instead of manually typing the value. The Spinner
 * allows values between 1 and a maximum value based on the stock of the
 * product. The selected value is displayed once the editing is finished.
 *
 * This cell also ensures that the Spinner reflects the current value of the
 * item and updates it in real-time when the user makes changes.
 *
 * The Spinner value is limited by the stock of the product associated with the
 * item.
 *
 * @author Meylin
 */
public class CartSpinnerEditingCell extends TableCell<Cart, Integer> {

    private Spinner<Integer> spinner;

    /**
     * Constructor that initializes the Spinner with the minimum value set to 1,
     * maximum value set to 10, and default value set to 1. The Spinner is also
     * set to be editable.
     */
    public CartSpinnerEditingCell() {
        // Initialize the Spinner
        spinner = new Spinner<>();
        // Set the minimum, maximum, and initial value (1 in this case)
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spinner.setValueFactory(valueFactory);  // Set the value factory for the spinner
        spinner.setEditable(true); // Allow the value to be edited manually
    }

    /**
     * Updates the TableCell's content based on whether it's empty or being
     * edited. When in editing mode, the Spinner is displayed. Otherwise, the
     * value is shown as text.
     *
     * @param item The item to be displayed (in this case, the quantity of the
     * cart item).
     * @param empty A boolean indicating if the cell is empty or not.
     */
    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                // Show the Spinner during editing
                setText(null);
                setGraphic(spinner);
            } else {
                // Display the item value as text when not editing
                setText(item != null ? item.toString() : "");
                setGraphic(null);
            }
        }
    }

    /**
     * Starts the editing mode in the TableCell and initializes the Spinner with
     * the current value of the item. The maximum value of the Spinner is set
     * according to the stock available for the product in the cart.
     *
     * When the user changes the value in the Spinner, it is committed as the
     * new value for the item.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();

            // Get the current value of the item
            Integer previousValue = getItem();
            // Set the initial value of the Spinner based on the current item's value
            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, previousValue != null ? previousValue : 1);
            spinner.setValueFactory(valueFactory);

            // Listener to confirm the value when changed
            spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                // Confirmar el valor cuando se cambia
                commitEdit(newValue != null ? newValue : previousValue);  // Commit the new value
            });

            setText(null);
            setGraphic(spinner); // Display the Spinner during editing
        }
    }

    /**
     * Cancels the editing mode and restores the original value of the
     * TableCell. The original value is displayed as text when editing is
     * canceled.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();

        // Restore the original value when editing is canceled
        Integer item = getItem();
        setText(item != null ? item.toString() : "");
        setGraphic(null);
    }

}
