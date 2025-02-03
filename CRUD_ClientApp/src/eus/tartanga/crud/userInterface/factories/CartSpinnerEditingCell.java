package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Cart;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;

public class CartSpinnerEditingCell extends TableCell<Cart, Integer> {

    private Spinner<Integer> spinner;

    public CartSpinnerEditingCell() {
        // Inicializa el Spinner
        spinner = new Spinner<>();
        // Establece el valor mínimo, máximo y el valor inicial (en este caso 1)
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spinner.setValueFactory(valueFactory);  // Asigna la fábrica de valores al spinner
        spinner.setEditable(true); // Si quieres que el valor sea editable
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                // Durante la edición, mostrar el Spinner
                setText(null);
                setGraphic(spinner);
            } else {
                // Cuando no estamos editando, mostrar el valor como texto
                setText(item != null ? item.toString() : "");
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();

            // Cambiar el valor del spinner al valor actual del item
            Integer previousValue = getItem();
            spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                // Confirmar el valor cuando se cambia
                commitEdit(newValue != null ? newValue : previousValue);
            });

            setText(null);
            setGraphic(spinner);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null); // Cancelar la edición y mostrar el valor original
    }
}
