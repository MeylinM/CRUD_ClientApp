/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.app.CRUD_ClientApp;
import eus.tartanga.crud.model.Concert;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.util.WaitForAsyncUtils;

/**
 *
 * @author Irati
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConcertViewControllerTest extends ApplicationTest {

    private TableView<Concert> tableView;

    @Override
    public void start(Stage stage) throws Exception {
        new CRUD_ClientApp().start(stage);;
    }

    public ConcertViewControllerTest() {
    }

    @Test
    public void test_write() {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
        clickOn("#btnAccept");
        clickOn("#concertsButton");
        tableView = lookup("#concertTable").query();
        Integer count = tableView.getItems().size();
        clickOn("#btnAddConcert");
        ObservableList<Concert> items = tableView.getItems();
        assertEquals(tableView.getItems().size(), count + 1);
        int rowIndex = items.size() - 1;
        Concert insertedConcert = items.get(rowIndex);

        assertEquals(insertedConcert.getConcertName(), "Name of the concert or tour");
        //assertEquals(insertedConcert.getArtistNames(), "");
        assertEquals(insertedConcert.getLocation(), "Concert location");
        assertEquals(insertedConcert.getCity(), "Concert location city");
        assertEquals(insertedConcert.getConcertDate(), new Date());
        assertEquals(insertedConcert.getConcertTime(), new Date());
    }
    
    //@Test
    public void test_delete() {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
        clickOn("#btnAccept");
        clickOn("#concertsButton");
        Node tableColumnConcertName = lookup("#concertColumn").nth(0).query();
        clickOn(tableColumnConcertName);
        tableView = lookup("#concertTable").query();
        Node row = lookup(".table-row-cell").nth(0).query();
        //Selecciona para que se habilite el botón de borrado.
        clickOn(row);
        Concert concertDelete = (Concert) tableView.getSelectionModel().getSelectedItem();
        String name = concertDelete.getConcertName();
        clickOn("#btnDeleteConcert");

        verifyThat("Do you want to delete the selected concert(s)?", isVisible());
        press(KeyCode.ENTER);
        Node row2 = lookup(".table-row-cell").nth(0).query();
        clickOn(row2);
        Concert concertDeleteAfter = (Concert) tableView.getSelectionModel().getSelectedItem();
        assertNotEquals(concertDelete, concertDeleteAfter);

        List<Concert> dataConcert = new ArrayList<>(tableView.getItems());
        Boolean notFound = true;
        for (Concert concert : dataConcert) {
            if (concert.getConcertName().equalsIgnoreCase(name)) {
                notFound = false;
            }
        }
        assertTrue(notFound);
    }

    //@Test
    public void test_update() {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
        clickOn("#btnAccept");
        clickOn("#concertsButton");
        tableView = lookup("#concertTable").query();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        //Seleccionar el nodo de todas las filas para poder hacer click en ella.
        Integer tablerow = tableView.getSelectionModel().getSelectedIndex();
        Node tableColumnName = lookup("#concertColumn").nth(tablerow + 1).query();
        // Node tableColumArtist = lookup("#artistColumn").nth(tablerow + 1).query();
        Node tableColumnLocation = lookup("#locationColumn").nth(tablerow + 1).query();
        Node tableColumnCity = lookup("#cityColumn").nth(tablerow + 1).query();
        //Node tableColumnDate = lookup("#dateColumn").nth(tablerow + 1).query();
        Node tableColumnTime = lookup("#timeColumn").nth(tablerow + 1).query();

        //Coger los valores del concierto seleccionado antes de ser modificada.
        Concert concertValues = (Concert) tableView.getSelectionModel().getSelectedItem();

        String name = concertValues.getConcertName();
        String location = concertValues.getLocation();

        //Click en las diferentes celdas para ir modificando los datos
        clickOn(tableColumnName);
        write("CONCERT");
        push(KeyCode.ENTER);

        clickOn(tableColumnLocation);
        write("LOCATION");
        push(KeyCode.ENTER);

        clickOn(tableColumnCity);
        write("CITY");
        push(KeyCode.ENTER);

        clickOn(tableColumnTime);
        write("21:30");
        push(KeyCode.ENTER);

        WaitForAsyncUtils.waitForFxEvents();

        //Coger los valores de la asignatura modificada.
        Concert modifiedConcert = (Concert) tableView.getSelectionModel().getSelectedItem();

        //Verificar que los valores no son iguales que los de la asignatura selecciona anteriormente.
        assertEquals("CONCERT", modifiedConcert.getConcertName());
        assertEquals("LOCATION", modifiedConcert.getLocation());
        assertEquals("CITY", modifiedConcert.getCity());
        assertEquals("21:30", modifiedConcert.getConcertTime());
    }
}
