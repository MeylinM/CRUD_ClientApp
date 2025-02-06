package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.app.CRUD_ClientApp;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Cart;
import java.net.MalformedURLException;
import static java.rmi.Naming.lookup;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import org.testfx.api.FxRobot;

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
    public void test1_AddProduct() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");
        clickOn("#shopButton");
        productView = lookup("#productTable").query();
        clickOn(".table-row-cell").type(KeyCode.ENTER);
        clickOn("#btnAddToCart");
        type(KeyCode.ENTER);
        
        

    }

    //@Test
    public void test2_DeleteOneProduct() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

    }

    //@Test
    public void test3_DeleteAll() {
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
        if(cartView.getItems().isEmpty()){
            done = true;
        }
        assertTrue(done);
    }

    //@Test
    public void test4_Update() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

    }

    //@Test
    public void test5_Buy() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

    }

    //@Test
    public void test6_FilterByArtist() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

    }

    //@Test
    public void test7_FilterByDate() {
        clickOn("#tfEmail");
        write("client@gmail.com");
        clickOn("#pfPassword");
        write("abcd*1234");
        clickOn("#btnAccept");

    }

}
