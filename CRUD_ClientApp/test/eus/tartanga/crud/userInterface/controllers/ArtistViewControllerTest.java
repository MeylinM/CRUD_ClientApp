/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.app.CRUD_ClientApp;
import eus.tartanga.crud.model.Artist;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

/**
 *
 * @author Olaia
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArtistViewControllerTest extends ApplicationTest {

    private TableView<Artist> tableView;

    @Override
    public void start(Stage stage) throws Exception {
        new CRUD_ClientApp().start(stage);;
    }

    public ArtistViewControllerTest() {
    }

    @Test
    public void test_write() throws NotBoundException, MalformedURLException {
        clickOn("#tfEmail");
        write("admin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        tableView = lookup("#artistTable").query();
        Integer count = tableView.getItems().size();
        clickOn("#btnAddArtist");
        ObservableList<Artist> items = tableView.getItems();
        assertEquals(tableView.getItems().size(), count + 1);
        int rowIndex = items.size() - 1;
        Artist insertedArtist = items.get(rowIndex);

        assertEquals(insertedArtist.getName(), "Name of the artist");
        assertEquals(insertedArtist.getCompany(), "Company of the artist");
        assertEquals(insertedArtist.getLastAlbum(), "Last album of the artist");

        LocalDate expectedDate = new Artist().getDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate actualDate = insertedArtist.getDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void test_update() {
        clickOn("#tfEmail");
        write("admin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

        tableView = lookup("#artistTable").query();

        // Get the total number of rows in the table
        int rowCount = lookup(".table-row-cell").queryAll().size();

        // Select the second-to-last (penultimate) row
        Node row = lookup(".table-row-cell").nth(rowCount - 2).query();
        clickOn(row);

        // Select the node of all rows to be able to click on them
        Integer tablerow = tableView.getSelectionModel().getSelectedIndex();

        Node tableColumnName = lookup("#nameColumn").nth(tablerow + 1).query();
        Node tableColumnCompany = lookup("#companyColumn").nth(tablerow + 1).query();
        Node tableColumnLastAlbum = lookup("#lastAlbumColumn").nth(tablerow + 1).query();
        Node tableColumnDebut = lookup("#debutColumn").nth(tablerow + 1).query();

        // Get the values of the selected artist before modification
        Artist subjectArtist = (Artist) tableView.getSelectionModel().getSelectedItem();
        String title = subjectArtist.getName();
        String description = subjectArtist.getCompany();

        // Click on the different cells to modify the data
        clickOn(tableColumnName);
        write("ARTISTA");
        push(KeyCode.ENTER);

        clickOn(tableColumnCompany);
        write("COMPAÑIA");
        push(KeyCode.ENTER);

        clickOn(tableColumnLastAlbum);
        write("LAST ALBUM");
        push(KeyCode.ENTER);

        doubleClickOn(tableColumnDebut);
        clickOn(tableColumnDebut);
        write("01/11/2027");
        push(KeyCode.ENTER);

        WaitForAsyncUtils.waitForFxEvents();

        // Get the values of the modified artist
        Artist modifiedArtist = (Artist) tableView.getSelectionModel().getSelectedItem();

        // Verify that the values are different from the values of the selected artist before modification
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String modifiedDateString = sdf.format(modifiedArtist.getDebut());
        assertEquals("ARTISTA", modifiedArtist.getName());
        assertEquals("COMPAÑIA", modifiedArtist.getCompany());
        assertEquals("LAST ALBUM", modifiedArtist.getLastAlbum());
        assertEquals("01/11/2027", modifiedDateString);
    }

    @Test
    public void test_delete() {  // Asegurar que se ejecuta al final
        clickOn("#tfEmail");
        write("admin@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

        tableView = lookup("#artistTable").query();

        // Obtener el número total de filas en la tabla
        int rowCount = lookup(".table-row-cell").queryAll().size();

        if (rowCount < 2) {
            fail("No hay suficientes artistas en la tabla para ejecutar el test.");
        }

        // Seleccionar la penúltima fila
        Node row = lookup(".table-row-cell").nth(rowCount - 2).query();
        clickOn(row);

        // Obtener el artista seleccionado antes de eliminarlo
        Artist artistDelete = tableView.getSelectionModel().getSelectedItem();

        // Intentar eliminar el artista
        clickOn("#btnDeleteArtist");

        // Esperar a que el Alert aparezca y manejarlo
        WaitForAsyncUtils.waitForFxEvents();

        if (lookup(".dialog-pane").tryQuery().isPresent()) {
            Node alertButton = lookup(".button").match(node -> ((javafx.scene.control.Button) node).getText().equals("Aceptar")).query();
            clickOn(alertButton);
            System.out.println("Se detectó una alerta y se aceptó.");
        } else {
            System.out.println("No se detectó ninguna alerta.");
        }

    }

    @Test
    public void testFilterArtistsBySearch() {
        clickOn("#tfEmail");
        write("olaia@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

        // Obtener la tabla de productos
        tableView = lookup("#artistTable").query();
        // Escribir en el TextField para filtrar
        clickOn("#searchField");
        write("JYP Entertaiment");

        // Esperar un poco para que se apliquen los filtros
        sleep(1000);

        // Verificar que los conciertos que contienen "Eminem" en name, compañia o último album estén presentes
        ObservableList<Artist> artists = tableView.getItems();

        for (Artist artist : artists) {
            String name = artist.getName().toLowerCase();
            String company = artist.getCompany().toLowerCase();
            //String lastAlbum = artist.getLastAlbum().toLowerCase();

            assertTrue(name.contains("jyp entertaiment") || company.contains("jyp entertaiment"));
        }
    }

    @Test
    public void testFilterProductsByDate() {
        clickOn("#tfEmail");
        write("olaia@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

        tableView = lookup("#artistTable").query();

        clickOn("#dpFrom");
        write("31/12/2016");
        push(KeyCode.ENTER);

        clickOn("#dpTo");
        write("01/01/2025");
        push(KeyCode.ENTER);

        ObservableList<Artist> artists = tableView.getItems();
        // Crear las fechas de comparación
        java.util.Date fromDate = java.sql.Date.valueOf("2016-12-31");
        java.util.Date toDate = java.sql.Date.valueOf("2025-01-01");

        for (Artist artist : artists) {
            java.util.Date releaseDate = artist.getDebut();

            // Verificar que la fecha esté dentro del rango esperado
            assertTrue(releaseDate.equals(fromDate) || releaseDate.after(fromDate));
            assertTrue(releaseDate.equals(toDate) || releaseDate.before(toDate));
        }
    }

}
