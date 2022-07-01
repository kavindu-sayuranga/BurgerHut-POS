package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogoutFormController {
    public AnchorPane HomeContext;
    public JFXButton btnYes;
    public JFXButton btnNo;

    public void btnYesOnAction(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) HomeContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/LoginForm.fxml"))));
        stage.centerOnScreen();

        Stage stage1 = (Stage) btnYes.getScene().getWindow();
        stage1.close();

    }

    public void btnNoOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();

    }

    public void getAllData(AnchorPane homeContext) {
        this.HomeContext = homeContext;

    }
}
