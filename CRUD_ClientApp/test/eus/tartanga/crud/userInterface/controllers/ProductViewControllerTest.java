/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.app.CRUD_ClientApp;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Product;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
/*import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.util.WaitForAsyncUtils;

/**
 *
 * @author ElbireTM
 *//*
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductViewControllerTest extends ApplicationTest{
    
     private TableView<Product> tableView;
    
    @Override
    public void start(Stage stage) throws Exception {
        new CRUD_ClientApp().start(stage);;
    }
    
    public ProductViewControllerTest() {
    }
    //@Test
    public void test_write() {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
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
        assertEquals(insertedProduct.getDescription(), "Description of the product");
        /*assertEquals(insertedProduct.getReleaseDate(), new Date());
        //assertEquals(dateInitLocal, LocalDate.now());
        //assertEquals(dateEndLocal, LocalDate.now());
        assertEquals(insertedProduct.getPrice(), "0");
        assertEquals(insertedProduct.getStock(), "1");*/
   /* }
    
    //@Test
    public void test_delete() {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
        clickOn("#btnAccept");
        clickOn("#shopButton");
        Node tableColumnTittle = lookup("#titleColumn").nth(0).query();
        clickOn(tableColumnTittle);
        tableView = lookup("#productTable").query();
        Node row = lookup(".table-row-cell").nth(0).query();
        //Selecciona una asignatura para que se habilite el botón de borrado.
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
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
        clickOn("#btnAccept");
        clickOn("#shopButton");
        tableView = lookup("#productTable").query();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        //Seleccionar el nodo de todas las filas para poder hacer click en ella.
        Integer tablerow = tableView.getSelectionModel().getSelectedIndex();
        Node tableColumnTitle = lookup("#titleColumn").nth(tablerow + 1).query();
        // Node tableColumTeachers = lookup("#artistColumn").nth(tablerow + 1).query();
        Node tableColumnDescription = lookup("#descriptionColumn").nth(tablerow + 1).query();
        Node tableColumnReleaseDate = lookup("#releaseDateColumn").nth(tablerow + 1).query();
        Node tableColumnStock = lookup("#stockColumn").nth(tablerow + 1).query();
        Node tableColumnPrice = lookup("#priceColumn").nth(tablerow + 1).query();

        //Coger los valores de la asignatura seleccionada antes de ser modificada.
        Product subjectProduct = (Product) tableView.getSelectionModel().getSelectedItem();

        String title = subjectProduct.getTitle();
        String description = subjectProduct.getDescription();

        //Click en las diferentes celdas para ir modificando los datos
        clickOn(tableColumnTitle);
        write("TITULO");
        push(KeyCode.ENTER);
        
        clickOn(tableColumnDescription);
        write("DESCRIPCION");
        push(KeyCode.ENTER);
        
        clickOn(tableColumnStock);
        write("0");
        push(KeyCode.ENTER);
        
        clickOn(tableColumnPrice);
        write("1");
        push(KeyCode.ENTER);
        
        WaitForAsyncUtils.waitForFxEvents();

        //Coger los valores de la asignatura modificada.
        Product modifiedProduct = (Product) tableView.getSelectionModel().getSelectedItem();

        //Verificar que los valores no son iguales que los de la asignatura selecciona anteriormente.
        assertEquals("TITULO", modifiedProduct.getTitle());
        assertEquals("DESCRIPCION", modifiedProduct.getDescription());
        assertEquals("0", modifiedProduct.getStock());
        assertEquals("1", modifiedProduct.getPrice());
        
    }
    
    
}
*/