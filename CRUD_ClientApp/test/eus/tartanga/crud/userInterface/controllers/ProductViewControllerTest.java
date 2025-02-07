/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.app.CRUD_ClientApp;
import eus.tartanga.crud.model.Product;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.util.WaitForAsyncUtils;

/**
 *
 * @author ElbireTM
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductViewControllerTest extends ApplicationTest {

    private TableView<Product> tableView;

    @Override
    public void start(Stage stage) throws Exception {
        new CRUD_ClientApp().start(stage);;
    }

    public ProductViewControllerTest() {

    }

    //@Test
    public void testFilterProductsBySearch() {
        clickOn("#tfEmail");
        write("meylin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#shopButton");

        // Obtener la tabla de productos
        tableView = lookup("#productTable").query();

        // Escribir en el TextField para filtrar
        clickOn("#tfSearch");
        write("Álbum");

        // Esperar un poco para que se apliquen los filtros
        sleep(1000); 

        // Verificar que los productos que contienen "Álbum" en title, description o artist.name estén presentes
        ObservableList<Product> products = tableView.getItems();

        for (Product product : products) {
            String title = product.getTitle().toLowerCase();
            String description = product.getDescription().toLowerCase();
            String artistName = product.getArtist().getName().toLowerCase();

            assertTrue(title.contains("álbum") || description.contains("álbum") || artistName.contains("álbum"));
        }
    }

    //@Test
    public void test_filterAvailableProducts() {
        // Hacer login
        clickOn("#tfEmail");
        write("meylin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#shopButton");

        // Obtener la tabla de productos
        tableView = lookup("#productTable").query();

        // Contar el número de productos antes de marcar el checkbox
        int totalProductsBefore = tableView.getItems().size();

        // Marcar el checkbox "Available products"
        clickOn("#cbxStock");

        // Esperar a que el filtro se aplique
        WaitForAsyncUtils.waitForFxEvents();

        // Obtener la lista de productos visibles en la tabla después de aplicar el filtro
        ObservableList<Product> filteredProducts = tableView.getItems();

        // Verificar que todos los productos visibles tienen stock mayor a 0
        for (Product product : filteredProducts) {
            assertTrue("El producto debería tener stock mayor a 0", Integer.parseInt(product.getStock()) > 0);
        }

        // Verificar que el número de productos en la tabla ha cambiado (esto depende de tu implementación del filtro)
        int totalProductsAfter = filteredProducts.size();
        assertTrue("El número de productos debería haberse reducido al aplicar el filtro", totalProductsAfter <= totalProductsBefore);
    }

    //@Test
    public void testFilterProductsByDate() {
        clickOn("#tfEmail");
        write("meylin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#shopButton");

        // Obtener la tabla de productos
        tableView = lookup("#productTable").query();

        clickOn("#dpFrom");
        write("01/01/2025");
        push(KeyCode.ENTER);

        clickOn("#dpTo");
        write("31/12/2025");
        push(KeyCode.ENTER);

        ObservableList<Product> products = tableView.getItems();
        // Crear las fechas de comparación
        java.util.Date fromDate = java.sql.Date.valueOf("2025-01-01");
        java.util.Date toDate = java.sql.Date.valueOf("2025-12-31");

        for (Product product : products) {
            java.util.Date releaseDate = product.getReleaseDate();

            // Verificar que la fecha esté dentro del rango esperado
            assertTrue(releaseDate.equals(fromDate) || releaseDate.after(fromDate));
            assertTrue(releaseDate.equals(toDate) || releaseDate.before(toDate));
        }
    }

    // @Test
    public void test_write() {
        clickOn("#tfEmail");
        write("meylin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#shopButton");
        tableView = lookup("#productTable").query();
        Integer count = tableView.getItems().size();
        clickOn("#btnAddProduct");
        ObservableList<Product> items = tableView.getItems();
        assertEquals(tableView.getItems().size(), count + 1);
        int rowIndex = items.size() - 1;
        Product insertedProduct = items.get(rowIndex);
        assertEquals(insertedProduct.getTitle(), "Tittle of the product");
        assertEquals(insertedProduct.getArtist().getName(), "Eminem");
        assertEquals(insertedProduct.getDescription(), "Description of the product");
        assertEquals(insertedProduct.getStock(), "1");
        assertEquals(insertedProduct.getPrice(), "0.0");
    }

    //@Test
    public void test_delete() {
        clickOn("#tfEmail");
        write("meylin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#shopButton");
        Node tableColumnTittle = lookup("#titleColumn").nth(0).query();
        clickOn(tableColumnTittle);
        tableView = lookup("#productTable").query();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        Product productDelete = (Product) tableView.getSelectionModel().getSelectedItem();
        String name = productDelete.getTitle();
        clickOn("#btnDeleteProduct");

        verifyThat("Do you want to delete the selected product(s)?", isVisible());
        press(KeyCode.ENTER);
        Node row2 = lookup(".table-row-cell").nth(0).query();
        clickOn(row2);
        Product productDeleteAfter = (Product) tableView.getSelectionModel().getSelectedItem();
        assertNotEquals(productDelete, productDeleteAfter);

        List<Product> dataProduct = new ArrayList<>(tableView.getItems());
        Boolean notFound = true;
        for (Product product : dataProduct) {
            if (product.getTitle().equalsIgnoreCase(name)) {
                notFound = false;
            }
        }

        assertTrue(notFound);
    }

    @Test
    public void test_update() {
        clickOn("#tfEmail");
        write("meylin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#shopButton");
        tableView = lookup("#productTable").query();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        //Seleccionar el nodo de todas las filas para poder hacer click en ella.
        
        Integer tablerow = tableView.getSelectionModel().getSelectedIndex();
        Node tableColumnTitle = lookup("#titleColumn").nth(tablerow + 1).query();
        Node tableColumArtists = lookup("#artistColumn").nth(tablerow + 1).query();
        Node tableColumnDescription = lookup("#descriptionColumn").nth(tablerow + 1).query();
        Node tableColumnReleaseDate = lookup("#releaseDateColumn").nth(tablerow + 1).query();
        Node tableColumnStock = lookup("#stockColumn").nth(tablerow + 1).query();
        Node tableColumnPrice = lookup("#priceColumn").nth(tablerow + 1).query();
        Product subjectProduct = (Product) tableView.getSelectionModel().getSelectedItem();

        String title = subjectProduct.getTitle();
        String description = subjectProduct.getDescription();

        //Click en las diferentes celdas para ir modificando los datos
        clickOn(tableColumnTitle);
        write("TITULO");
        push(KeyCode.ENTER);
        
        // Hacemos clic en la celda de artista para abrir la ComboBox
        doubleClickOn(tableColumArtists);
        clickOn("Fito");
        
        clickOn(tableColumnDescription);
        write("DESCRIPCION");
        push(KeyCode.ENTER);
        
        doubleClickOn(tableColumnReleaseDate);
        clickOn(tableColumnReleaseDate);
        write("01/11/2027");
        push(KeyCode.ENTER);
        
        clickOn(tableColumnStock);
        write("0");
        push(KeyCode.ENTER);

        clickOn(tableColumnPrice);
        write("1");
        push(KeyCode.ENTER);

        WaitForAsyncUtils.waitForFxEvents();

        Product modifiedProduct = (Product) tableView.getSelectionModel().getSelectedItem();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String modifiedDateString = sdf.format(modifiedProduct.getReleaseDate());
        assertEquals("TITULO", modifiedProduct.getTitle());
        assertEquals("DESCRIPCION", modifiedProduct.getDescription());
        assertEquals("01/11/2027", modifiedDateString);
        assertEquals("0", modifiedProduct.getStock());
        assertEquals("1", modifiedProduct.getPrice());
        assertEquals("Fito", modifiedProduct.getArtist().getName());
    }

}
