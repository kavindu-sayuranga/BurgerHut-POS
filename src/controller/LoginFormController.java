package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.ValidationUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class LoginFormController {
    public AnchorPane LoginContext;
    public JFXPasswordField pwdPassword;
    public JFXTextField txtUsername;
    public JFXComboBox<String> cmbSelectType;
    public Label lblSignUp;
    public Label lblSignup;
    public Label lblSignupForm;
    public JFXButton btnLogin;

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    Pattern usernamePattern = Pattern.compile("^[A-z0-9]{6,10}$");
    LinkedHashMap<JFXPasswordField, Pattern> map1 = new LinkedHashMap<>();
    Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");


    public void initialize() {

        storeValidation();

        cmbSelectType.getItems().addAll("Admin", "User");
        btnLogin.setDisable(true);

        try {
            ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Users").executeQuery();
            while (rst.next()) {
                if (rst.getString(5).equalsIgnoreCase("Admin")) {
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void storeValidation() {
        map.put(txtUsername, usernamePattern);
        map1.put(pwdPassword, passwordPattern);

    }



    public void btnloginOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {

        String uType = cmbSelectType.getSelectionModel().getSelectedItem();
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Users  WHERE UserType=?");
        stm.setObject(1, uType);
        ResultSet rst = stm.executeQuery();

        if (!cmbSelectType.getSelectionModel().isEmpty()) {
            while (rst.next()) {
                if (cmbSelectType.getValue().equalsIgnoreCase("Admin")) {
                    if (rst.getString(5).equals(cmbSelectType.getSelectionModel().getSelectedItem())) {
                        if (pwdPassword.getText().equals(rst.getString(4)) && txtUsername.getText().equals(rst.getString(2))) {
                            Stage stage = (Stage) LoginContext.getScene().getWindow();
                            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/AdminHomeForm.fxml"))));
                            stage.centerOnScreen();
                            return;
                        }
                    }
                }

                if (cmbSelectType.getValue().equalsIgnoreCase("User")) {
                    if (rst.getString(5).equals(cmbSelectType.getSelectionModel().getSelectedItem())) {
                        if (pwdPassword.getText().equals(rst.getString(4)) && txtUsername.getText().equals(rst.getString(2))) {
                            Stage stage = (Stage) LoginContext.getScene().getWindow();
                            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/UserHomeForm.fxml"))));
                            stage.centerOnScreen();
                            return;
                        }
                    }
                }

            }
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

    }

    public void lblSignupForm (MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../views/SignupForm.fxml");
        Parent load = FXMLLoader.load(resource);
        LoginContext.getChildren().clear();
        LoginContext.getChildren().add(load);
    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(map, btnLogin);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
            }
        }
    }

    public void passwordFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXPasswordField(map1, btnLogin);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXPasswordField) {
                JFXPasswordField errorText = (JFXPasswordField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
            }
        }
    }
}
