package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import model.Employee;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;
import views.tm.EmployeeTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class EmployeeFormController {
    public AnchorPane HomeContext;
    public TableView<EmployeeTM> tblEmployee;
    public TableColumn colEmId;
    public TableColumn colEmName;
    public TableColumn colEmAddress;
    public TableColumn colEmNIC;
    public TableColumn colEmNumber;
    public TableColumn colPosition;
    public TableColumn colOperate;
    public JFXTextField txtEmId;
    public JFXComboBox<String> cmbPosition;
    public JFXTextField txtEmName;
    public JFXTextField txtEmAddress;
    public JFXTextField txtEmNIC;
    public JFXTextField txtEmNumber;
    public JFXTextField txtEEmIdUpdate;
    public JFXTextField txtEmAddressUpdate;
    public JFXTextField txtEmNameUpdate;
    public JFXComboBox cmbPositionUpdate;
    public JFXTextField txtEmNICUpdate;
    public JFXTextField txtEmNumberUpdate;
    public Label lblDate;
    public Label lblTime;
    public JFXButton btnAddEmployee;
    public JFXButton btnUpdate;


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

    LinkedHashMap<JFXTextField, Pattern> emp = new LinkedHashMap<>();
    Pattern employeeIdPattern = Pattern.compile("^(E)[0-9]{3}$");
    Pattern employeeNamePattern = Pattern.compile("^[A-z ]{4,25}$");
    Pattern employeeAddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
    Pattern employeeNicPattern = Pattern.compile("^[0-9]{12}$");
    Pattern employeePhonePattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");
    LinkedHashMap<JFXTextField, Pattern> emp1 = new LinkedHashMap<>();
    Pattern empIdPattern = Pattern.compile("^(E)[0-9]{3}$");
    Pattern empNamePattern = Pattern.compile("^[A-z ]{4,25}$");
    Pattern empAddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
    Pattern empNicPattern = Pattern.compile("^[0-9]{12}$");
    Pattern empPhonePattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");


    public void initialize(){

        btnAddEmployee.setDisable(true);
        btnUpdate.setDisable(true);
        storeValidations();

        loadDateAndTime();

        cmbPosition.getItems().add("Driver");
        cmbPosition.getItems().add("User");
        cmbPosition.getItems().add("Cashier");

        cmbPositionUpdate.getItems().add("Driver");
        cmbPositionUpdate.getItems().add("User");
        cmbPositionUpdate.getItems().add("Cashier");



        colEmId.setCellValueFactory(new PropertyValueFactory("EmployeeId"));
        colEmName.setCellValueFactory(new PropertyValueFactory("EmployeeName"));
        colEmAddress.setCellValueFactory(new PropertyValueFactory("EmployeeAddress"));
        colEmNIC.setCellValueFactory(new PropertyValueFactory("EmployeeNic"));
        colEmNumber.setCellValueFactory(new PropertyValueFactory("EmployeePhone"));
        colPosition.setCellValueFactory(new PropertyValueFactory("EmployeePosition"));
        colOperate.setCellValueFactory(new PropertyValueFactory("btn"));

        try {
            loadAllEmployee();
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        setAutoIds();
    }

    private void storeValidations() {
        btnAddEmployee.setDisable(true);
        btnUpdate.setDisable(true);
        emp.put(txtEmId, employeeIdPattern);
        emp.put(txtEmName, employeeNamePattern);
        emp.put(txtEmAddress, employeeAddressPattern);
        emp.put(txtEmNIC, employeeNicPattern);
        emp.put(txtEmNumber, employeePhonePattern );
        emp1.put(txtEEmIdUpdate, empIdPattern);
        emp1.put(txtEmNameUpdate, empNamePattern);
        emp1.put(txtEmAddressUpdate, empAddressPattern);
        emp1.put(txtEmNICUpdate, empNicPattern);
        emp1.put(txtEmNumberUpdate, empPhonePattern);
    }


    private void setAutoIds() {
        txtEmId.setText(GenerateAutoId.autoId("SELECT Emp_id FROM employee ORDER BY Emp_id DESC LIMIT 1", 1, 3));
    }

    private void loadAllEmployee() throws SQLException, ClassNotFoundException {


        ResultSet result = CrudUtil.execute("SELECT * FROM Employee");
        ObservableList<EmployeeTM> obList = FXCollections.observableArrayList();

        while (result.next()){
            Button btn=new Button("Delete");
            btn.setOnAction(event -> {
                EmployeeTM selectedItem = (EmployeeTM) tblEmployee.getSelectionModel().getSelectedItem();
                txtEmId.setText(selectedItem.getEmployeeId());


                deleteEmployee();

                try {
                    loadAllEmployee();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            obList.add(
                    new EmployeeTM(
                            result.getString("Emp_id"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("NIC"),
                            result.getString("phone_number"),
                            result.getString("role_type"),
                            btn
                    )
            );
        }
        tblEmployee.setItems(obList);
        clearAllTexts();
    }



    private void deleteEmployee() {

        try{

            if(CrudUtil.execute("DELETE FROM Employee WHERE Emp_id=?",txtEmId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").showAndWait();
                loadAllEmployee();

            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){

        }

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


    public void btnAddEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Employee em = new Employee(txtEmId.getText(),txtEmName.getText(),txtEmAddress.getText(),txtEmNIC.getText(),txtEmNumber.getText(),cmbPosition.getValue());

        try {
            if(CrudUtil.execute("INSERT INTO Employee VALUES (?,?,?,?,?,?)",em.getEmployeeId(),em.getEmployeeName(),em.getEmployeeAddress(),em.getEmployeeNic(),em.getEmployeePhone(),em.getEmployeePosition())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        clearAllTexts();
        loadAllEmployee();
        setAutoIds();
    }

    public void txtEEmIdUpdateOnAction(ActionEvent actionEvent) {

        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Employee WHERE Emp_id = ?",txtEEmIdUpdate.getText());
            if (result.next()) {
                txtEmNameUpdate.setText(result.getString(2));
                txtEmAddressUpdate.setText(result.getString(3));
                txtEmNICUpdate.setText(result.getString(4));
                txtEmNumberUpdate.setText(result.getString(5));
                cmbPositionUpdate.setValue(result.getString(6));


            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result!..").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Employee em = new Employee(txtEEmIdUpdate.getText(),txtEmNameUpdate.getText(),txtEmAddressUpdate.getText(),txtEmNICUpdate.getText(),txtEmNumberUpdate.getText(), (String) cmbPositionUpdate.getValue());


        try{
            boolean isUpdated = CrudUtil.execute("UPDATE  Employee SET name = ?, address = ?, NIC = ?, phone_number = ?, role_type = ? WHERE Emp_id = ?",em.getEmployeeName(),em.getEmployeeAddress(),em.getEmployeeNic(),em.getEmployeePhone(),em.getEmployeePosition(),em.getEmployeeId());

            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated.").show();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }


        }catch (SQLException | ClassNotFoundException e){

        }

        clearAllTexts2();
        loadAllEmployee();
    }

    private void clearAllTexts() {
        txtEmId.clear();
        txtEmName.clear();
        txtEmAddress.clear();
        txtEmNIC.clear();
        txtEmNumber.clear();

        txtEmId.requestFocus();
    }

    private void clearAllTexts2() {
        txtEEmIdUpdate.clear();
        txtEmNameUpdate.clear();
        txtEmAddressUpdate.clear();
        txtEmNICUpdate.clear();
        txtEmNumberUpdate.clear();

        txtEEmIdUpdate.requestFocus();
    }

    public void textFieldOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(emp, btnAddEmployee);
        if (keyEvent.getCode() == KeyCode.ENTER){
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                System.out.println(errorText);

                errorText.requestFocus();
            } else if (response instanceof Boolean) {
            }
        }

    }

    public void textFiledsOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(emp1,btnUpdate);
        if (keyEvent.getCode() == KeyCode.ENTER){
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                System.out.println(errorText);
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }

    }


}
