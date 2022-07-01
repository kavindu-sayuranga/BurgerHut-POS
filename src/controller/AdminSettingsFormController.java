package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminSettingsFormController {
    public AnchorPane HomeContext;
    public JFXTextField txtNewUserName;
    public JFXTextField txtOldPassword;
    public JFXTextField txtNewPassword;
    public JFXTextField txtConformPassword;
    public JFXButton btnUpdate;
    public Label lblTime;
    public Label lblDate;

    public void dashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminHomeForm");
    }

    public void melasOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminMealForm");
    }

    public void customersOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminCustomerForm");
    }

    public void deliveryOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminDeliveryForm");
    }

    public void supplierOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminSupplierForm");
    }

    public void ingredientsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminIngredientForm");
    }

    public void employeeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("EmployeeForm");
    }

    public void reportOnAction(ActionEvent actionEvent) throws IOException {
        setUi("IncomeReportForm");
    }

    public void settingsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminSettingsForm");
    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        lordWidow("../views/LogoutForm.fxml","Log out");

    }

    public void ordersOnAction(ActionEvent actionEvent) throws IOException {
        setUi("OrderForm");
    }

    private void setUi(String URI) throws IOException {
        Stage stage = (Stage) HomeContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/"+URI+".fxml"))));
    }

    public void lordWidow(String location,String title) throws IOException {

        FXMLLoader loader =new FXMLLoader(getClass().getResource(location));
        Parent root=loader.load();
        Scene scene =new Scene(root);
        LogoutFormController controller =loader.getController();

        controller.getAllData(HomeContext);

        Stage stage =new Stage(StageStyle.UTILITY);

        stage.setScene(scene);
        stage.show();


    }


    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    Pattern newUserNamePattern = Pattern.compile("^[A-z0-9]{6,10}$");
    Pattern oldPasswordPattern = Pattern.compile("[A-z0-9]{8,}");
    Pattern newPasswordPattern = Pattern.compile("[A-z0-9]{8,}");
    Pattern confirmPasswordPattern = Pattern.compile("[A-z0-9]{8,}");


    public void initialize(){
        loadDateAndTime();
        storeValidations();
    }

    private void storeValidations() {
        map.put(txtNewUserName, newUserNamePattern);
        map.put(txtOldPassword, oldPasswordPattern);
        map.put(txtNewPassword, newPasswordPattern);
        map.put(txtConformPassword, confirmPasswordPattern);
    }

    private void loadDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try{
            boolean isUpdated = CrudUtil.execute("UPDATE users set Username=?, passwordId=?  WHERE passwordId=?",txtNewUserName.getText(), txtNewPassword.getText(), txtOldPassword.getText() );

            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated.").showAndWait();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").showAndWait();
            }


        }catch (SQLException | ClassNotFoundException e){
            txtNewUserName.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConformPassword.clear();

        }


    }

    public void btnClearOnAction(ActionEvent actionEvent) {

    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(map, btnUpdate);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
            }
        }
    }

}
