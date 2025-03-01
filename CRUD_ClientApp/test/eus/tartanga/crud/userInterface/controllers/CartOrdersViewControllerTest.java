package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.app.CRUD_ClientApp;
import eus.tartanga.crud.model.Cart;
import eus.tartanga.crud.model.Product;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

/**
 *
 * @author meylin
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartOrdersViewControllerTest extends ApplicationTest {

    private TableView<Cart> cartView;
    private TableView<Cart> productView;

    @Override
    public void start(Stage stage) throws Exception {
        new CRUD_ClientApp().start(stage);;
    }

    public CartOrdersViewControllerTest() {
    }

    //@Test
    public void test5_FilterByArtist() {
        clickOn("#tfEmail");
        write("elbire@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#profile");
        clickOn("#itemMyOrders");
        cartView = lookup("#tbCart").query();
        clickOn("#cbxArtist");

        // Seleccionar "Fito" en la lista desplegable
        clickOn("Fito");

        // Obtener los elementos filtrados en la tabla
        List<Cart> filteredItems = new ArrayList<>(cartView.getItems());

        // Verificar que los productos filtrados tienen "Fito" como artista
        for (Cart cartItem : filteredItems) {
            // Suponiendo que Cart tiene un método getArtist() que devuelve el artista
            String artist = cartItem.getProduct().getArtist().getName();
            // Verificar que el artista contiene "Fito"
            assertNotNull("El artista no debería ser nulo", artist);
            assertTrue("El artista debería ser 'Fito'", artist.contains("Fito"));
        }

    }

    //@Test
    public void test7_FilterByDate() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#profile");
        clickOn("#itemMyOrders");
        cartView = lookup("#tbCart").query();

        clickOn("#dpFrom");
        write("01/01/2025");
        push(KeyCode.ENTER);

        clickOn("#dpTo");
        write("31/12/2025");
        push(KeyCode.ENTER);

        ObservableList<Cart> carts = cartView.getItems();
        // Crear las fechas de comparación
        java.util.Date fromDate = java.sql.Date.valueOf("2025-01-01");
        java.util.Date toDate = java.sql.Date.valueOf("2025-12-31");

        for (Cart cart : carts) {
            java.util.Date releaseDate = cart.getOrderDate();

            // Verificar que la fecha esté dentro del rango esperado
            assertTrue(releaseDate.equals(fromDate) || releaseDate.after(fromDate));
            assertTrue(releaseDate.equals(toDate) || releaseDate.before(toDate));
        }
    }

    //@Test
    public void test1_DeleteAll() {
        clickOn("#tfEmail");
        write("elbire@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#menuMyCart");
        cartView = lookup("#tbCart").query();
        clickOn("#btnClearCart");
        push(KeyCode.ENTER);
        boolean done = false;
        if (cartView.getItems().isEmpty()) {
            done = true;
        }
        assertTrue(done);
    }

    //@Test
    public void test2_AddProduct() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

        clickOn("#menuMyCart");
        TableView<?> cartTable = lookup("#tbCart").query();
        int initialSize = cartTable.getItems().size() - 1;

        clickOn("#menuArtist");
        clickOn("#shopButton");

        productView = lookup("#productTable").query();
        clickOn(".table-row-cell").type(KeyCode.ENTER);
        clickOn("#btnAddToCart");
        type(KeyCode.ENTER);

        clickOn("#menuMyCart");
        int finalSize = cartTable.getItems().size();
        assertEquals(initialSize + 1, finalSize);

    }

    //@Test
    public void test3_Update() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

        clickOn("#menuMyCart");
        cartView = lookup("#tbCart").query();

        // Esperar para asegurarnos de que la tabla se ha cargado
        WaitForAsyncUtils.waitForFxEvents();

        // Verificar que la tabla contiene elementos
        assertNotNull("La tabla no tiene elementos", cartView.getItems());
        assertTrue("La tabla está vacía", !cartView.getItems().isEmpty());

        // Seleccionar la primera fila (comprobamos que haya al menos una fila)
        Node row = lookup(".table-row-cell").nth(0).query();  // Seleccionamos la primera fila que contiene un valor, no el encabezado
        clickOn(row);

        // Obtener el índice de la fila seleccionada
        Integer tablerow = cartView.getSelectionModel().getSelectedIndex();
        assertNotNull("El índice de la fila seleccionada es nulo", tablerow);

        // Obtener las celdas correspondientes a la fecha y cantidad de la fila seleccionada
        Node tableColumnDate = lookup("#tbcOrderDate").nth(tablerow + 1).query();
        Node tableColumnQuantity = lookup("#tbcQuantity").nth(tablerow + 1).query();

        // Verificar que las celdas existen antes de interactuar con ellas
        assertNotNull("La celda de la fecha no existe", tableColumnDate);
        assertNotNull("La celda de la cantidad no existe", tableColumnQuantity);

        // Realizar doble clic y editar los valores
        doubleClickOn(tableColumnDate);
        clickOn(tableColumnDate);
        write("01/11/2027");  // Cambiar la fecha
        push(KeyCode.ENTER);

        doubleClickOn(tableColumnQuantity);
        clickOn(tableColumnQuantity);
        write("4");  // Cambiar la cantidad
        push(KeyCode.ENTER);

        Cart modifiedCart = (Cart) cartView.getSelectionModel().getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String modifiedDateString = sdf.format(modifiedCart.getOrderDate());
        assertEquals("4", modifiedCart.getQuantity().toString());
        assertEquals("01/11/2027", modifiedDateString);
    }

    //@Test
    public void test4_Buy() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#menuMyCart");
        cartView = lookup("#tbCart").query();
        clickOn("#btnBuy");
        type(KeyCode.ENTER);

        boolean done = false;
        if (cartView.getItems().isEmpty()) {
            done = true;
        }
        assertTrue(done);
    }

}
