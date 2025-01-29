package eus.tartanga.crud.userInterface.factories;

import eus.tartanga.crud.model.Cart;
import eus.tartanga.crud.model.Product;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;

public class CartSpinnerEditingCell extends TableCell<Cart, Integer> {

    private Spinner<Integer> spinner;
    private SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory;
    private int maxStock = 100;  // Valor máximo inicial, puedes ajustarlo según lo necesites.

    public CartSpinnerEditingCell() {
        // Inicializa el Spinner y la fábrica de valores
        spinner = new Spinner<>();
        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxStock, 1); // Valor máximo inicial
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
                // Durante la edición, actualizar el valor máximo dinámicamente desde el stock
                Cart cartItem = (Cart) getTableRow().getItem();
                if (cartItem != null) {
                    // Obtener el Product desde Cart
                    Product product = cartItem.getProduct(); // Método que te da el Product relacionado
                    if (product != null) {
                        int stock = Integer.parseInt(product.getStock());  // Obtener el stock del producto

                        // Solo actualizamos el máximo si es necesario
                        if (maxStock != stock) {
                            maxStock = stock;
                            valueFactory.setMax(maxStock); // Actualiza el valor máximo
                        }
                    }
                }

                // Configurar el valor actual del Spinner
                valueFactory.setValue(item != null ? item : 1); // Valor inicial

                setText(null);
                setGraphic(spinner);
            } else {
                // Fuera de edición, mostrar el valor como texto
                setText(item != null ? item.toString() : "");
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            Integer previousValue = getItem();
            // Configurar el listener para que actualice el valor en el modelo de datos
            spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                commitEdit(newValue != null ? newValue : previousValue); // Confirmar el valor
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
