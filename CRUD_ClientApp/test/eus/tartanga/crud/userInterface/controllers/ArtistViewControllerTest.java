/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.app.CRUD_ClientApp;
import eus.tartanga.crud.model.Artist;
import java.net.MalformedURLException;
import static java.rmi.Naming.lookup;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static net.sf.jasperreports.util.CastorUtil.write;
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

    //@Test
    public void test_write() throws NotBoundException, MalformedURLException {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
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
      
    }

    // @Test
    public void test_delete() {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
        clickOn("#btnAccept");
        Node tableColumnName = lookup("#nameColumn").nth(0).query();
        clickOn(tableColumnName);
        tableView = lookup("#artistTable").query();

        // Get the total number of rows in the table
        int rowCount = lookup(".table-row-cell").queryAll().size();

        // Select the second-to-last row (penultimate row)
        Node row = lookup(".table-row-cell").nth(rowCount - 2).query();

        //Selecciona una asignatura para que se habilite el botón de borrado.
        clickOn(row);
        Artist artistDelete = (Artist) tableView.getSelectionModel().getSelectedItem();
        String name = artistDelete.getName();
        clickOn("#btnDeleteArtist");

        // Re-select the second-to-last row after deletion
        Node row2 = lookup(".table-row-cell").nth(rowCount - 2).query();
        clickOn(row2);
        Artist artistDeleteAfter = (Artist) tableView.getSelectionModel().getSelectedItem();
        assertNotEquals(artistDelete, artistDeleteAfter);

    }

    @Test
    public void test_update() {
        clickOn("#tfEmail");
        write("admin1@fanetix.com");
        clickOn("#pfPassword");
        write("password123");
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

        WaitForAsyncUtils.waitForFxEvents();

        // Get the values of the modified artist
        Artist modifiedArtist = (Artist) tableView.getSelectionModel().getSelectedItem();

        // Verify that the values are different from the values of the selected artist before modification
        assertEquals("ARTISTA", modifiedArtist.getName());
        assertEquals("COMPAÑIA", modifiedArtist.getCompany());
        assertEquals("LAST ALBUM", modifiedArtist.getLastAlbum());
    }

}
