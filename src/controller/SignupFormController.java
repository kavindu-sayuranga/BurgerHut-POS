package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.User;
import util.ValidationUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SignupFormController {

    public JFXTextField txtUserName;
    public JFXTextField txtEmail;
    public JFXPasswordField pwdPassword;
    public JFXPasswordField pwdConformPassword;
    public JFXComboBox<String> cmbSelectType;
    public JFXButton btnSignup;
    public JFXTextField txtName;
    public AnchorPane SignupContext;
    public Label lblBack;


    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    Pattern NamePattern = Pattern.compile("^[A-z ]{3,}$");
    Pattern userNamePattern = Pattern.compile("^[A-z0-9]{6,15}$");
    Pattern emailPattern = Pattern.compile("[a-z0-9]{3,}@(gmail|yahoo).com");

    LinkedHashMap<JFXPasswordField, Pattern> map1 = new LinkedHashMap<>();
    Pattern passwordPattern = Pattern.compile("[A-z0-9]{8,}");
    Pattern confirmPasswordPattern = Pattern.compile("[A-z0-9]{8,}");


    public void initialize() {
        storeValidations();
        btnSignup.setDisable(true);
        cmbSelectType.getItems().addAll("Admin","User");

    }

    private void storeValidations() {
        map.put(txtName, NamePattern);
        map.put(txtUserName, userNamePattern);
        map.put(txtEmail, emailPattern);
        map1.put(pwdPassword, passwordPattern);
        map1.put(pwdConformPassword, confirmPasswordPattern);
    }




    public void signupOnAction(ActionEvent actionEvent) {

        if (pwdPassword.getText().equals(pwdConformPassword.getText())) {
            if (!cmbSelectType.getSelectionModel().isEmpty()) {
                User user = new User(txtName.getText(), txtUserName.getText(),txtEmail.getText(),pwdPassword.getText(), cmbSelectType.getSelectionModel().getSelectedItem());
                try {
                    if (UserCrudController.signupUser(user)) {
                        txtName.clear();
                        txtUserName.clear();
                        txtEmail.clear();
                        pwdPassword.clear();
                        pwdConformPassword.clear();
                        cmbSelectType.getSelectionModel().clearSelection();


                    } else {
                        new Alert(Alert.AlertType.ERROR,"Error !...").show();
                    }

                } catch (SQLIntegrityConstraintViolationException e) {

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.ERROR,"Error !...").show();            }
        } else {
            new Alert(Alert.AlertType.ERROR,"Error !...").show();        }



    }

    public void lblBack(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../views/LoginForm.fxml");
        Parent load = FXMLLoader.load(resource);
        SignupContext.getChildren().clear();
        SignupContext.getChildren().add(load);

    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(map, btnSignup);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }
    }

    public void passwordFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXPasswordField(map1, btnSignup);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXPasswordField) {
                JFXPasswordField errorText = (JFXPasswordField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }
    }


}
