/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.encrypt.AsymmetricalClient;
import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.EncryptException;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import eus.tartanga.crud.exception.MaxCityCharacterException;
import eus.tartanga.crud.exception.MaxStreetCharacterException;
import eus.tartanga.crud.exception.PasswdsDontMatchException;
import eus.tartanga.crud.exception.PatternEmailIncorrectException;
import eus.tartanga.crud.exception.PatternFullNameIncorrectException;
import eus.tartanga.crud.exception.PatternMobileIncorrectException;
import eus.tartanga.crud.exception.PatternPasswordIncorrectException;
import eus.tartanga.crud.exception.PatternZipIncorrectException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.TextEmptyException;
import eus.tartanga.crud.exception.UserExistErrorException;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.FanetixClient;
import java.util.Base64;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import static jxl.biff.BaseCellFeatures.logger;

/**
 * @author Meylin, Elbire
 */
public class SignUpViewController {

    /**
     * AnchorPane for the layout of the SignUp view.
     */
    @FXML
    private AnchorPane signUpView;

    /**
     * ContextMenu that provides options when right-clicking in the SignUp view.
     */
    @FXML
    private ContextMenu contextMenu;

    /**
     * TextField for entering the user's full name.
     */
    @FXML
    private TextField tfFullName;

    /**
     * ToggleButton for showing or hiding the password.
     */
    @FXML
    private ToggleButton tgbEyePasswd;

    /**
     * ToggleButton for showing or hiding the confirm password.
     */
    @FXML
    private ToggleButton tgbEyeConfirmPasswd;

    /**
     * TextField for entering the user's password.
     */
    @FXML
    private TextField tfShowPassword;

    /**
     * TextField for confirming the user's password.
     */
    @FXML
    private TextField tfShowConfirmPassword;

    /**
     * CheckBox for marking whether the user is active or not.
     */
    @FXML
    private CheckBox cbxStatus;

    /**
     * TextField for entering the user's email address.
     */
    @FXML
    private TextField tfEmail;

    /**
     * TextField for entering the user's street address.
     */
    @FXML
    private TextField tfStreet;

    /**
     * TextField for entering the user's ZIP code.
     */
    @FXML
    private TextField tfZip;

    /**
     * TextField for entering the user's city.
     */
    @FXML
    private TextField tfCity;

    /**
     * TextField for entering the user's mobile phone number.
     */
    @FXML
    private TextField tfMobile;

    /**
     * PasswordField for entering the user's password.
     */
    @FXML
    private PasswordField pfHiddenPassword;

    /**
     * PasswordField for confirming the user's password.
     */
    @FXML
    private PasswordField pfHiddenConfirmPassword;

    /**
     * Button to trigger the sign-up process.
     */
    @FXML
    private Button btnSignUp;

    /**
     * Label for displaying a general error message when the form is incomplete.
     */
    @FXML
    private Label labelErrorEmpty;

    /**
     * Label for displaying validation errors for the full name.
     */
    @FXML
    private Label labelErrorFullName;

    /**
     * Label for displaying validation errors for the email.
     */
    @FXML
    private Label labelErrorEmail;

    /**
     * Label for displaying validation errors for the format of the password.
     */
    @FXML
    private Label labelErrorPasswd;

    /**
     * Label for displaying validation errors for the password confirmation.
     */
    @FXML
    private Label labelErrorConfirmPasswd;

    /**
     * Label for displaying validation errors for the street address.
     */
    @FXML
    private Label labelErrorStreet;

    /**
     * Label for displaying validation errors for the ZIP code.
     */
    @FXML
    private Label labelErrorZip;

    /**
     * Label for displaying validation errors for the city.
     */
    @FXML
    private Label labelErrorCity;

    /**
     * Label for displaying validation errors for the mobile phone number.
     */
    @FXML
    private Label labelErrorMobile;

    /**
     * Hyperlink to navigate to the SignIn view.
     */
    @FXML
    private Hyperlink hypSignUp;

    /**
     * ImageView for toggling the visibility of the password field.
     */
    @FXML
    private ImageView imgEyePasswd;

    /**
     * ImageView for toggling the visibility of the confirm password field.
     */
    @FXML
    private ImageView imgEyeConfirmPasswd;

    /**
     * The stage for displaying the SignUp view.
     */
    private Stage stage;

    /**
     * Interface for the sign-up business logic.
     */
    //private Signable signable;
    private static final Logger LOGGER = Logger.getLogger(SignUpViewController.class.getName());

    /**
     * Initializes and configures the registration window (SignUp).
     *
     * @param root The root node to be used for creating the scene of the
     * window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        // Set the title of the window to "SignUp".
        stage.setTitle("SignUp");
        //Añadir a la ventana el ícono “FanetixLogo.png”.
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
        // Make the window non-resizable.
        stage.setResizable(false);
        // Set the window to be modal.
        stage.initModality(Modality.APPLICATION_MODAL);
        clearForm();
        // Set focus on the FullName field (tfFullName).
        tfFullName.isFocused();
        // Set "btnSignUp" as the default button.
        btnSignUp.setDefaultButton(true);
        // Make the "tfShowPassword" field hidden.
        tfShowPassword.setVisible(false);
        // Make the "tfShowConfirmPassword" field hidden.
        tfShowConfirmPassword.setVisible(false);
        // Set the CheckBox to be selected 
//        cbxStatus.setSelected(true);
        // Hide all error labels.
        clearErrorLabels();
        setTooltips();
        setPromptText();
        // Create a ContextMenu with options: Reset Form, Help, and About App.
        setUpContextMenu();

        tfFullName.textProperty().addListener(this::textPropertyChange);
        tfEmail.textProperty().addListener(this::textPropertyChange);
        tfShowPassword.textProperty().addListener(this::textPropertyChange);
        tfShowConfirmPassword.textProperty().addListener(this::textPropertyChange);
        tfStreet.textProperty().addListener(this::textPropertyChange);
        tfZip.textProperty().addListener(this::textPropertyChange);
        tfCity.textProperty().addListener(this::textPropertyChange);
        tfMobile.textProperty().addListener(this::textPropertyChange);

        // Add listener for password visibility toggle between pfHiddenPassword and tfShowPassword.
        pfHiddenPassword.textProperty().addListener(this::passwrdIsVisible);
        tfShowPassword.textProperty().addListener(this::passwrdIsVisible);

        // Add listener for confirm password visibility toggle between pfHiddenConfirmPassword and tfShowConfirmPassword.
        pfHiddenConfirmPassword.textProperty().addListener(this::passwrdIsVisible);
        tfShowConfirmPassword.textProperty().addListener(this::passwrdIsVisible);

        // Add handler for eye toggle buttons (tgbEyePasswd and tgbEyeConfirmPasswd).
        tgbEyePasswd.setOnAction(this::handelEyeIconToggleButtonAction);
        tgbEyeConfirmPasswd.setOnAction(this::handelEyeIconToggleButtonAction);

        // Add handler for the hyperlink to SignIn view (hypSignUp).
        hypSignUp.setOnAction(this::handleHyperLinkSignIn);
        // Add handler to manage the window close request.
        stage.setOnCloseRequest(this::handleCloseRequest);
        //signable = ClientFactory.getSignable();
        stage.showAndWait();

    }

    /**
     * Synchronizes the text between the password fields and their visible
     * counterparts.
     *
     * This method updates the text of the hidden password fields based on the
     * visibility of the corresponding visible password fields and vice versa.
     *
     * @param observable The observable value that has changed (not used in this
     * method).
     * @param oldValue The old value of the observable (not used in this
     * method).
     * @param newValue The new value of the observable (not used in this
     * method).
     */
    public void passwrdIsVisible(ObservableValue observable, String oldValue, String newValue) {

        // The text from "pfHiddenPassword" is copied to "tfShowPassword" and vice versa.
        if (pfHiddenPassword.isVisible()) {
            tfShowPassword.setText(pfHiddenPassword.getText());
        } else if (tfShowPassword.isVisible()) {
            pfHiddenPassword.setText(tfShowPassword.getText());
        }

        // The text from "pfHiddenConfirmPassword" is copied to "tfShowConfirmPassword" and vice versa.
        if (pfHiddenConfirmPassword.isVisible()) {
            tfShowConfirmPassword.setText(pfHiddenConfirmPassword.getText());

        } else if (tfShowConfirmPassword.isVisible()) {
            pfHiddenConfirmPassword.setText(tfShowConfirmPassword.getText());
        }
    }

    /**
     * Handles the toggle action for the eye icon buttons to show or hide the
     * password fields.
     *
     * This method updates the visibility of the password fields and the
     * corresponding eye icons based on whether the toggle buttons for showing
     * or hiding the passwords are selected.
     *
     * @param event The action event triggered by the toggle button.
     */
    @FXML
    private void handelEyeIconToggleButtonAction(ActionEvent event) {

        Image hiddenEyeIcon = new Image(getClass().getResourceAsStream("eus/tartanga/crud/app/resources/HidePasswdSignUp.png"));
        Image visibleEyeIcon = new Image(getClass().getResourceAsStream("eus/tartanga/crud/app/resources/ShowPasswdSignUp.png"));

        if (tgbEyePasswd.isSelected()) {
            // PasswordField “pfHiddenPassword” will become invisible.
            pfHiddenPassword.setVisible(false);
            // TextField “tfShowPassword” will become visible.
            tfShowPassword.setVisible(true);
            // The imgEyePasswd icon changes to HidePasswdOrange.png.
            imgEyePasswd.setImage(hiddenEyeIcon);
        } else {
            // TextField “tfShowPassword” will become invisible.
            tfShowPassword.setVisible(false);
            // PasswordField “pfHiddenPassword” will become visible.
            pfHiddenPassword.setVisible(true);
            // The imgEyePasswd icon changes to ShowPasswdOrange.png.
            imgEyePasswd.setImage(visibleEyeIcon);
        }

        if (tgbEyeConfirmPasswd.isSelected()) {
            // PasswordField “pfHiddenConfirmPassword” will become invisible.
            pfHiddenConfirmPassword.setVisible(false);
            // TextField “tfShowConfirmPassword” will become visible.
            tfShowConfirmPassword.setVisible(true);
            // The imgEyeConfirmPasswd icon changes to HidePasswdOrange.png.
            imgEyeConfirmPasswd.setImage(hiddenEyeIcon);
        } else {
            // TextField “tfShowConfirmPassword” will become invisible.
            tfShowConfirmPassword.setVisible(false);
            // PasswordField “pfHiddenConfirmPassword” will become visible.
            pfHiddenConfirmPassword.setVisible(true);
            // The imgEyeConfirmPasswd icon changes to ShowPasswdOrange.png.
            imgEyeConfirmPasswd.setImage(visibleEyeIcon);
        }
    }

    /**
     * Handles the sign-up action when the user clicks the "Sign Up" button.
     *
     * This method validates the user input in each field, displays error
     * messages for any validation failures, and attempts to register a new user
     * by creating a User object and calling the sign-up method from the
     * signable interface.
     *
     * @param event The action event triggered by the button click.
     * @throws ServerErrorException If there is an issue with server
     * availability.
     * @throws UserExistErrorException If the email address already exists in
     * the system.
     */
    @FXML
    private void handleSignUp(ActionEvent event) {
        // Clear error messages each time btnSignUp is clicked.
        clearErrorLabels();
        FanetixClient newUser;
        FanetixClient newUserValidate;
        // Variables to create a new user.
        String name = null, street = null, city = null, password = null, email = null;
        int zip = 0, mobile = 0;
        boolean active;
        boolean focused = false;

        try {
            // Validate that all required fields are filled out.
            if (tfFullName.getText().isEmpty() || tfEmail.getText().isEmpty() || pfHiddenPassword.getText().isEmpty()
                    || pfHiddenConfirmPassword.getText().isEmpty() || tfStreet.getText().isEmpty() || tfCity.getText().isEmpty()
                    || tfZip.getText().isEmpty() || tfMobile.getText().isEmpty()) {

                // Throw exception if any field is empty
                throw new TextEmptyException("You must fill all the parameters");
            }
            // Validate that the "tfFullName" field does not contain any numbers.
            if (!Pattern.matches("^[A-Za-zÀ-ÿ'\\s]+$", tfFullName.getText())) {
                // Throw the exception if the full name contains numbers or invalid characters
                throw new PatternFullNameIncorrectException("The full name can't contain numbers");
            } else {
                name = tfFullName.getText();
                clearErrorStyle(tfFullName, labelErrorFullName);
            }

            // Validate that the "tfEmail" field follows correct email format and has a maximum of 320 characters.
            if (!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", tfEmail.getText())
                    || tfEmail.getText().length() > 320) {

                // Throw the exception if the email format is invalid or too long
                throw new PatternEmailIncorrectException("The email must have a valid format");
            } else {
                email = tfEmail.getText();
                clearErrorStyle(tfEmail, labelErrorEmail);
            }

            // Validate that the password format in "tfShowPassword" meets criteria.
            String passwordValidate = tfShowPassword.getText();

            // Check if password has at least 8 characters
            if (passwordValidate.length() < 8) {
                throw new PatternPasswordIncorrectException("Password must be at least 8 characters long");
            }

            // Check if password contains at least one uppercase letter
            if (!passwordValidate.matches(".*[A-Z].*")) {
                throw new PatternPasswordIncorrectException("Password must contain at least one uppercase letter");
            }

            // Check if password contains at least one lowercase letter
            if (!passwordValidate.matches(".*[a-z].*")) {
                throw new PatternPasswordIncorrectException("Password must contain at least one lowercase letter");
            }

            // Check if password contains at least one digit
            if (!passwordValidate.matches(".*\\d.*")) {
                throw new PatternPasswordIncorrectException("Password must contain at least one digit");
            }

            // Check if password contains at least one special character
            if (!passwordValidate.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                throw new PatternPasswordIncorrectException("Password must contain at least one special character");
            }
            if (tfShowPassword.isVisible()) {
                clearErrorStyle(tfShowPassword, labelErrorPasswd);
            } else {
                clearErrorStylePassword(pfHiddenPassword, labelErrorPasswd);
            }
            // Validate that "pfHiddenConfirmPassword" matches "pfHiddenPassword".

            if (!pfHiddenPassword.getText().equals(pfHiddenConfirmPassword.getText())) {
                // Throw exception if the passwords don't match
                throw new PasswdsDontMatchException("The passwords don't match");
            } else {
                password = pfHiddenPassword.getText();
            }
            if (pfHiddenConfirmPassword.isVisible()) {
                clearErrorStylePassword(pfHiddenConfirmPassword, labelErrorConfirmPasswd);
            } else {
                clearErrorStyle(tfShowConfirmPassword, labelErrorConfirmPasswd);
            }

            // Validate that "tfStreet" does not exceed 255 characters.
            if (tfStreet.getText().length() > 255) {
                // Throw exception if the street length exceeds 255 characters
                throw new MaxStreetCharacterException("Street must be shorter");
            } else {
                street = tfStreet.getText();
                clearErrorStyle(tfStreet, labelErrorStreet);
            }
            // Validate that "tfZip" contains only digits and is a maximum of 5 numbers.
            if (!tfZip.getText().matches("\\d{5}$")) {
                // Throw exception if the ZIP code is not a valid 5-digit number
                throw new PatternZipIncorrectException("ZIP must be 5 digits.");
            } else {
                zip = Integer.parseInt(tfZip.getText());
                clearErrorStyle(tfZip, labelErrorZip);
            }
            // Validate that "tfCity" does not exceed 58 characters.
            if (tfCity.getText().length() > 58) {
                // Throw exception if the city name length exceeds 58 characters
                throw new MaxCityCharacterException("City must be shorter");
            } else {
                city = tfCity.getText();
                clearErrorStyle(tfCity, labelErrorCity);
            }
            // Validate that "tfMobile" contains only digits and has a maximum of 9 characters.
            if (!tfMobile.getText().matches("\\d{9}$")) {
                // Throw exception if the mobile number is not 9 digits
                throw new PatternMobileIncorrectException("Mobile must be 9 digits.");
            } else {
                mobile = Integer.parseInt(tfMobile.getText());
                clearErrorStyle(tfMobile, labelErrorMobile);
            }
            /*
            //LO QUE TENIAIS ANTES
            if (!(email == null || password == null || name == null || street == null || mobile == 0 || city == null || zip == 0)) {
                newUser = new FanetixClient(email, password, name, street, mobile, city, zip);

                // Get an implementation of the "Signable" interface from the "ClientFactory".
                FanetixClientManager clientManager = FanetixClientFactory.getFanetixClientManager();
                clientManager.createClient_XML(newUser);
                try {
                    newUserValidate = clientManager.findClient_XML(new GenericType<FanetixClient>() {
                    }, email);
                    if (newUserValidate != null) {
                    // Show an INFORMATION alert with the message "Registration successful."
                    new Alert(Alert.AlertType.CONFIRMATION, "You have successfully registered.", ButtonType.OK).showAndWait();
                    // After accepting the message, close the SignUp window and return control to the SignIn window.
                    stage.close();
                }
            }*/

            //Encriptacion
            if (!(email == null || password == null || name == null || street == null || mobile == 0 || city == null || zip == 0)) {
                // Encriptar la contraseña antes de enviarla al servidor
                AsymmetricalClient asymmetricalClient = new AsymmetricalClient();
                byte[] encryptedPassword;

                try {
                    encryptedPassword = asymmetricalClient.encryptedData(password);
                } catch (EncryptException ex) {
                    LOGGER.severe("Encryption error");
                    new Alert(Alert.AlertType.ERROR, "Error encrypting the password. Please try again.", ButtonType.OK).showAndWait();
                    return; // Detiene el proceso si la encriptación falla
                }

                // Convertimos la contraseña encriptada a Base64
                String encryptedPasswordBase64 = Base64.getEncoder().encodeToString(encryptedPassword);

                // Crear usuario con la contraseña encriptada
                newUser = new FanetixClient(email, encryptedPasswordBase64, name, street, mobile, city, zip);

                FanetixClientManager clientManager = FanetixClientFactory.getFanetixClientManager();
                clientManager.createClient_XML(newUser);

                // Validar si se creó correctamente en el servidor
                newUserValidate = clientManager.findClient_XML(new GenericType<FanetixClient>() {
                }, email);
                if (newUserValidate != null) {
                    new Alert(Alert.AlertType.CONFIRMATION, "You have successfully registered.", ButtonType.OK).showAndWait();
                    stage.close(); // Cierra la ventana de SignUp
                }
            }


        } catch (ReadException | AddException e) {

            // Handle server-related errors with an alert message.
            new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage(), ButtonType.OK).showAndWait();

        } catch (PatternMobileIncorrectException e) {
            // If invalid, show "PatternMobileIncorrectException" in labelErrorMobile.
            setErrorStyle(tfMobile, labelErrorMobile, e.getMessage());
            if (!focused) {
                tfMobile.requestFocus();
                focused = true;
            }
        } catch (MaxCityCharacterException e) {
            // If exceeded, show "MaxCityCharacterException" in labelErrorCity.
            setErrorStyle(tfCity, labelErrorCity, e.getMessage());
            if (!focused) {
                tfCity.requestFocus();
                focused = true;
            }
        } catch (PatternZipIncorrectException e) {
            // If invalid, show "PatternZipIncorrectException" in labelErrorZip.
            setErrorStyle(tfZip, labelErrorZip, e.getMessage());
            if (!focused) {
                tfZip.requestFocus();
                focused = true;
            }
        } catch (MaxStreetCharacterException e) {
            // If exceeded, show "MaxStreetCharacterException" in labelErrorStreet.
            setErrorStyle(tfStreet, labelErrorStreet, e.getMessage());
            if (!focused) {
                tfStreet.requestFocus();
                focused = true;
            }
        } catch (PasswdsDontMatchException e) {
            // If not matching, show "PasswdsDontMatchException" in labelErrorConfirmPasswd.
            if (tfShowConfirmPassword.isVisible()) {
                setErrorStyle(tfShowConfirmPassword, labelErrorConfirmPasswd, e.getMessage());
            } else {
                setErrorStylePassword(pfHiddenConfirmPassword, labelErrorConfirmPasswd, e.getMessage());
            }
            if (!focused) {
                if (tfShowConfirmPassword.isVisible()) {
                    tfShowConfirmPassword.requestFocus();
                } else {
                    pfHiddenConfirmPassword.requestFocus();
                }
                focused = true;
            }
        } catch (PatternPasswordIncorrectException e) {
            if (tfShowPassword.isVisible()) {
                setErrorStyle(tfShowPassword, labelErrorPasswd, e.getMessage());
            } else {
                setErrorStylePassword(pfHiddenPassword, labelErrorPasswd, e.getMessage());
            }
            if (!focused) {
                if (tfShowPassword.isVisible()) {
                    tfShowPassword.requestFocus();
                } else {
                    pfHiddenPassword.requestFocus();
                }
                focused = true;
            }
        } catch (PatternEmailIncorrectException e) {
            // If invalid, show "PatternEmailIncorrectException" in labelErrorEmail.
            setErrorStyle(tfEmail, labelErrorEmail, e.getMessage());
            if (!focused) {
                tfEmail.requestFocus();
                focused = true;
            }
        } catch (PatternFullNameIncorrectException e) {
            // If it contains numbers, show "PatternFullNameIncorrectException" in labelErrorFullName.
            setErrorStyle(tfFullName, labelErrorFullName, e.getMessage());
            if (!focused) {
                tfFullName.requestFocus();
                focused = true;
            }
        } catch (TextEmptyException e) {
            // If fields are missing, trigger "TextEmptyException" in labelErrorEmpty.
            labelErrorEmpty.setText(e.getMessage());
        }
    }

    private void textPropertyChange(ObservableValue observable,
            String oldValue,
            String newValue) {

        if (observable == tfFullName.textProperty()) {
            if (labelErrorFullName.getText().equals("The full name can't contain numbers")) {
                clearErrorStyle(tfFullName, labelErrorFullName);
            }
        }
        if (observable == tfEmail.textProperty()) {
            if (labelErrorEmail.getText().equals("The email must have a valid format")) {
                clearErrorStyle(tfEmail, labelErrorEmail);
            }
        }
        if (observable == tfShowPassword.textProperty()) {
            if (labelErrorPasswd.getText().equals("Password must be at least 8 characters long")) {
                clearErrorStyle(tfShowPassword, labelErrorPasswd);
            } else if (labelErrorPasswd.getText().equals("Password must contain at least one uppercase letter")) {
                clearErrorStyle(tfShowPassword, labelErrorPasswd);
            } else if (labelErrorPasswd.getText().equals("Password must contain at least one lowercase letter")) {
                clearErrorStyle(tfShowPassword, labelErrorPasswd);
            } else if (labelErrorPasswd.getText().equals("Password must contain at least one digit")) {
                clearErrorStyle(tfShowPassword, labelErrorPasswd);
            } else if (labelErrorPasswd.getText().equals("Password must contain at least one special character")) {
                clearErrorStyle(tfShowPassword, labelErrorPasswd);
            }
        }

        if (observable == tfShowConfirmPassword.textProperty()) {
            if (labelErrorConfirmPasswd.getText().equals("The passwords don't match")) {
                clearErrorStyle(tfShowConfirmPassword, labelErrorConfirmPasswd);
            }
        }
        if (observable == tfStreet.textProperty()) {
            if (labelErrorStreet.getText().equals("Street must be shorter")) {
                clearErrorStyle(tfStreet, labelErrorStreet);
            }
        }
        if (observable == tfZip.textProperty()) {
            if (labelErrorZip.getText().equals("ZIP must be 5 digits.")) {
                clearErrorStyle(tfZip, labelErrorZip);
            }
        }
        if (observable == tfCity.textProperty()) {
            if (labelErrorCity.getText().equals("City must be shorter")) {
                clearErrorStyle(tfCity, labelErrorCity);
            }
        }
        if (observable == tfMobile.textProperty()) {
            if (labelErrorMobile.getText().equals("Mobile must be 9 digits.")) {
                clearErrorStyle(tfMobile, labelErrorMobile);
            }
        }
    }

    /**
     * Handles the action when the user clicks on the hyperlink to sign in.
     *
     * This method closes the current SignUp stage to navigate back to the
     * SignIn view. If an error occurs during the process, it logs the
     * exception.
     *
     * @param event The action event triggered by the hyperlink click.
     */
    @FXML
    private void handleHyperLinkSignIn(ActionEvent event) {
        stage.close();
    }

    /**
     * Sets tooltips for the input fields in the SignUp form.
     *
     * Each tooltip provides a hint to the user about what to enter in the
     * corresponding field.
     */
    private void setTooltips() {
        Tooltip tooltipFN = new Tooltip("Enter your full name");
        Tooltip.install(tfFullName, tooltipFN);

        Tooltip tooltipE = new Tooltip("Enter your email address");
        Tooltip.install(tfEmail, tooltipE);

        Tooltip tooltipP = new Tooltip("Enter your password");
        Tooltip.install(tfShowPassword, tooltipP);

        Tooltip tooltipRP = new Tooltip("Repeat the password");
        Tooltip.install(tfShowConfirmPassword, tooltipRP);

        Tooltip tooltipS = new Tooltip("Enter your street address");
        Tooltip.install(tfStreet, tooltipS);

        Tooltip tooltipZ = new Tooltip("Enter your ZIP code");
        Tooltip.install(tfZip, tooltipZ); // Corrected to apply ZIP code tooltip to tfZip

        Tooltip tooltipC = new Tooltip("Enter your city");
        Tooltip.install(tfCity, tooltipC);

        Tooltip tooltipM = new Tooltip("Enter your phone number");
        Tooltip.install(tfMobile, tooltipM);
    }

    /**
     * Sets the prompt text for the input fields in the SignUp form.
     *
     * This method configures the prompt text that appears in each input field,
     * providing users with a hint about the expected input.
     */
    private void setPromptText() {
        tfFullName.setPromptText("Enter your full name");
        tfEmail.setPromptText("Enter your email (e.g., example@domain.com)");
        pfHiddenPassword.setPromptText("Enter your password");
        pfHiddenConfirmPassword.setPromptText("Repeat your password");
        tfShowPassword.setPromptText("Enter your password");
        tfShowConfirmPassword.setPromptText("Repeat your password");
        tfStreet.setPromptText("Enter your street");
        tfZip.setPromptText("Enter your ZIP code");
        tfCity.setPromptText("Enter your city");
        tfMobile.setPromptText("Enter your mobile number");
    }

    /**
     * Clears the error messages displayed in the SignUp form.
     *
     * This method resets the text of all error label components to an empty
     * string, effectively removing any error messages that may have been
     * displayed to the user.
     */
    private void clearErrorLabels() {
        labelErrorFullName.setText("");
        labelErrorEmail.setText("");
        labelErrorConfirmPasswd.setText("");
        labelErrorStreet.setText("");
        labelErrorZip.setText("");
        labelErrorCity.setText("");
        labelErrorMobile.setText("");
        labelErrorEmpty.setText("");
        labelErrorPasswd.setText("");
    }

    /**
     * Clears all input fields in the SignUp form.
     *
     * This method resets the values of all form fields to their default state.
     * Specifically, it clears the text from the following input fields and sets
     * the status checkbox to unchecked.
     */
    private void clearForm() {
        tfFullName.clear();
        tfEmail.clear();
        pfHiddenPassword.clear();
        pfHiddenConfirmPassword.clear();
        tfStreet.clear();
        tfZip.clear();
        tfCity.clear();
        tfMobile.clear();
       // cbxStatus.setSelected(true);
    }

    /**
     * Sets up the context menu for the SignUp form.
     *
     * This method creates a context menu that provides users with options to
     * reset the form, access help, and view information about the application.
     *
     * Each menu item has an associated action handler that executes when the
     * item is selected. The context menu is displayed when the user
     * right-clicks anywhere in the SignUp window.
     */
    private void setUpContextMenu() {
        contextMenu = new ContextMenu();

        // Create the menu items
        MenuItem resetFormItem = new MenuItem("Reset Form");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutAppItem = new MenuItem("About App");
        contextMenu.getItems().addAll(resetFormItem, helpItem, aboutAppItem);

        // Handle the menu options
        resetFormItem.setOnAction(this::handleResetForm);
        helpItem.setOnAction(this::handleHelp);
        aboutAppItem.setOnAction(this::handleAboutApp);
        // Associate the Context Menu with the entire window
        signUpView.setOnMouseClicked(this::showContextMenu);
    }

    /**
     * Displays the context menu when the user right-clicks on the SignUp form.
     *
     * This method checks if the right mouse button (secondary button) was
     * clicked. If so, it shows the context menu at the position of the mouse
     * cursor. If any other mouse button is clicked, the context menu is hidden.
     *
     * @param event The mouse event that triggered this method.
     */
    private void showContextMenu(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            contextMenu.show(signUpView, event.getScreenX(), event.getScreenY());
        } else {
            contextMenu.hide();
        }
    }

    /**
     * Resets the SignUp form to its default values.
     *
     * This method clears all input fields in the form, setting them back to
     * their default (empty) state. It also clears any error messages that may
     * be displayed to the user.
     *
     * @param event The action event that triggered this method.
     */
    private void handleResetForm(ActionEvent event) {
        // All fields in the form are reset to their default (empty) values.
        clearForm();
        clearErrorLabels();
    }

    /**
     * Displays a dialog with common errors and security recommendations for
     * creating an account.
     *
     * This method shows an information alert dialog that provides users with
     * tips on creating a secure password.
     *
     * @param event The action event that triggered this method.
     */
    private void handleHelp(ActionEvent event) {
        // An alert dialog is displayed with common errors and security recommendations for creating the account.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Security Recommendations for Your Password");
        alert.setHeaderText("Security Recommendations");
        alert.setContentText("- Use a combination of uppercase and lowercase letters, numbers, and symbols.\n"
                + "- Avoid using personal information, such as names or birth dates.\n"
                + "- Change your password regularly to keep your account secure."
        );
        alert.showAndWait();
    }

    /**
     * Displays a dialog with information about the application, including its
     * name, version, authors, and purpose.
     *
     * This method shows an information alert dialog that provides users with
     * details about the application.
     *
     * @param event The action event that triggered this method.
     */
    private void handleAboutApp(ActionEvent event) {
        // An alert dialog is displayed with information about the application: name, version, authors, and purpose of the app.

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About the Application");
        alert.setHeaderText("SignUp Window Information");
        alert.setContentText(
                "Name: SignUp\n"
                + "Version: 2.0\n"
                + "Authors: Elbire Haro, Meylin Gutierrez\n"
                + "Purpose: This window helps the user create an account in the database for the second term challenge."
        );

        // Show the dialog and wait until it is closed
        alert.showAndWait();
    }

    /**
     * Handles the window close request event by showing a confirmation dialog
     * before closing the application.
     *
     * This method consumes the close request event and invokes a confirmation
     * dialog to ask the user if they really want to exit the application.
     *
     * @param event The window event that triggered this method.
     */
    private void handleCloseRequest(WindowEvent event) {
        event.consume();
        showExitConfirmation();
    }

    /**
     * Displays a confirmation dialog asking the user if they are sure they want
     * to exit the application.
     */
    private void showExitConfirmation() {
        // Show a CONFIRMATION Alert with the message: "Are you sure you want to exit?".
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.showAndWait();
        ButtonType response = alert.getResult();
        handleCloseConfirmation(response);
    }

    /**
     * Handles the confirmation response when the user attempts to close the
     * application.
     *
     *
     * @param response The user's response to the exit confirmation dialog.
     *
     */
    public void handleCloseConfirmation(ButtonType response) {
        if (response == ButtonType.OK) {
            stage.close();
        }
    }

    /**
     * Sets an error style on the specified TextField and updates the associated
     * Label with an error message.
     *
     * @param textfield the TextField to apply the error style to
     * @param errorLabel the Label to display the error message
     * @param message the error message to display
     */
    private void setErrorStyle(TextField textfield, Label errorLabel, String message) {
        textfield.setStyle("-fx-border-color: red;");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(message);
    }

    /**
     * Sets an error style on the specified PasswordField and updates the
     * associated Label with an error message.
     *
     * @param textfield the PasswordField to apply the error style to
     * @param errorLabel the Label to display the error message
     * @param message the error message to display
     */
    private void setErrorStylePassword(PasswordField textfield, Label errorLabel, String message) {
        textfield.setStyle("-fx-border-color: red;");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(message);
    }

    /**
     * Clears the error style from the specified TextField and removes any error
     * message from the associated Label.
     *
     * @param textfield the TextField to clear the error style from
     * @param errorLabel the Label to clear the error message from
     */
    private void clearErrorStyle(TextField textfield, Label errorLabel) {
        textfield.setStyle("");
        errorLabel.setText("");
    }

    /**
     * Clears the error style from the specified PasswordField and removes any
     * error message from the associated Label.
     *
     * @param textfield the PasswordField to clear the error style from
     * @param errorLabel the Label to clear the error message from
     */
    private void clearErrorStylePassword(PasswordField textfield, Label errorLabel) {
        textfield.setStyle("");
        errorLabel.setText("");
    }

}
