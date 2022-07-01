package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Customer;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;
import views.tm.CustomerTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class UserCustomerFormController {
    public AnchorPane HomeContext;
    public TableView tblCustomer;
    public TableColumn colCustomerId;
    public TableColumn colCustomerName;
    public TableColumn colCustomerAddress;
    public TableColumn colCustomerNIC;
    public TableColumn colCustomerNumber;
    public TableColumn colOperate;
    public JFXButton btnAddCustomer;
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCustomerNIC;
    public JFXTextField txtCustomerNumber;
    public JFXTextField txtCustomerIdUpdate;
    public JFXTextField txtCustomerNameUpdate;
    public JFXTextField txtCustomerAddressUpdate;
    public JFXTextField txtCustomerNICUpdate;
    public JFXTextField txtCustomerPhoneNumberUpdate;
    public JFXButton btnUpdate;
    public Label Date;
    public Label Time;

    public void dashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserHomeForm");
    }

    public void melasOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserMealForm");
    }

    public void customersOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserCustomerForm");
    }

    public void deliveryOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserDeliveryForm");
    }

    public void ordersOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserOrderForm");
    }

    public void settingsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserSettingsForm");
    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        lordWidow("../views/LogoutForm.fxml","Log out");
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


    LinkedHashMap<JFXTextField, Pattern> cus = new LinkedHashMap<>();
    Pattern customerIdPattern = Pattern.compile("^(C)[0-9]{3}$");
    Pattern customerNamePattern = Pattern.compile("^[A-z ]{4,25}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
    Pattern nicPattern = Pattern.compile("^[0-9]{12}$");
    Pattern phonePattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");


    public void initialize(){

        btnAddCustomer.setDisable(true);

        storeValidations();
        loadDateAndTime();



        colCustomerId.setCellValueFactory(new PropertyValueFactory("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory("customerName"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory("customerAddress"));
        colCustomerNIC.setCellValueFactory(new PropertyValueFactory("customerNIC"));
        colCustomerNumber.setCellValueFactory(new PropertyValueFactory("customerNumber"));
        colOperate.setCellValueFactory(new PropertyValueFactory("btn"));

        try {
            loadAllCustomers();
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        setAutoIds();
    }

    private void storeValidations() {
        btnAddCustomer.setDisable(true);

        cus.put(txtCustomerId, customerIdPattern);
        cus.put(txtCustomerName, customerNamePattern);
        cus.put(txtCustomerAddress, addressPattern);
        cus.put(txtCustomerNIC, nicPattern);
        cus.put(txtCustomerNumber, phonePattern );

    }

    private void loadDateAndTime() {
        Date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime = LocalTime.now();
            Time.setText(currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    private void setAutoIds() {
        txtCustomerId.setText(GenerateAutoId.autoId("SELECT cus_id FROM customer ORDER BY cus_id DESC LIMIT 1", 1, 3));
    }


    public void btnAddCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Customer c = new Customer(txtCustomerId.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),txtCustomerNIC.getText(),txtCustomerNumber.getText());

        try {
            if(CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?,?)",c.getCustomerId(),c.getCustomerName(),c.getCustomerAddress(),c.getCustomerNIC(),c.getCustomerNumber())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        clearAllTexts();
        loadAllCustomers();
        setAutoIds();

    }

    private void loadAllCustomers() throws ClassNotFoundException, SQLException {

        ResultSet result = CrudUtil.execute("SELECT * FROM Customer");
        ObservableList<CustomerTM> obList = FXCollections.observableArrayList();

        while (result.next()){
            Button btn=new Button("Delete");
            btn.setOnAction(event -> {
                CustomerTM selectedItem = (CustomerTM) tblCustomer.getSelectionModel().getSelectedItem();
                txtCustomerId.setText(selectedItem.getCustomerId());


                deleteCustomer();

                try {
                    loadAllCustomers();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            obList.add(
                    new CustomerTM(
                            result.getString("Cus_id"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("NIC"),
                            result.getString("phone_number"),
                            btn
                    )
            );
        }
        tblCustomer.setItems(obList);
        clearAllTexts();

    }



    public void txtCustomerIdOnAction(ActionEvent actionEvent) {

        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE Cus_id = ?",txtCustomerIdUpdate.getText());
            if (result.next()) {
                txtCustomerNameUpdate.setText(result.getString(2));
                txtCustomerAddressUpdate.setText(result.getString(3));
                txtCustomerNICUpdate.setText(result.getString(4));
                txtCustomerPhoneNumberUpdate.setText(result.getString(5));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result!..").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {


        Customer c = new Customer(txtCustomerIdUpdate.getText(),txtCustomerNameUpdate.getText(),txtCustomerAddressUpdate.getText(),txtCustomerNICUpdate.getText(),txtCustomerPhoneNumberUpdate.getText());


        try{
            boolean isUpdated = CrudUtil.execute("UPDATE  Customer SET name = ?, address = ?, NIC = ?, phone_number = ? WHERE Cus_id = ?",c.getCustomerName(),c.getCustomerAddress(),c.getCustomerNIC(),c.getCustomerNumber(),c.getCustomerId());

            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated.").show();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }


        }catch (SQLException | ClassNotFoundException e){

        }

        clearAllTexts2();
        loadAllCustomers();
    }

    private void deleteCustomer() {
        try{

            if(CrudUtil.execute("DELETE FROM Customer WHERE cus_id=?",txtCustomerId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").showAndWait();
                loadAllCustomers();

            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){

        }

    }

    private void clearAllTexts() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerNIC.clear();
        txtCustomerNumber.clear();
        txtCustomerId.requestFocus();

    }

    private void clearAllTexts2() {
        txtCustomerIdUpdate.clear();
        txtCustomerNameUpdate.clear();
        txtCustomerAddressUpdate.clear();
        txtCustomerNICUpdate.clear();
        txtCustomerPhoneNumberUpdate.clear();
        txtCustomerIdUpdate.requestFocus();

    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {

        Object response = ValidationUtil.validateJFXTextField(cus, btnAddCustomer);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
//            Notifications confirmation = NotificationBuilder.notifyMassage("CONFIRMATION", "All the text fields are filled successfully.");
//            confirmation.showConfirm();
            }

        }
    }
}
