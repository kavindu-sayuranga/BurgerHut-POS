package controller;

import com.jfoenix.controls.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Delivery;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;
import views.tm.DeliveryTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminDeliveryFormController {
    public AnchorPane HomeContext;
    public Label Date;
    public Label Time;
    public TableView<Delivery> tblDelivery;
    public TableColumn colDeliverId;
    public TableColumn colDriverId;
    public TableColumn colOrderId;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colPrice;
    public TableColumn colStatus;
    public JFXTextField txtDeliveryId;
    public JFXComboBox<String> cmbOrderId;
    public JFXComboBox<String> cmbDriverId;
    public JFXDatePicker txtDate;
    public JFXTimePicker txtTime;
    public JFXTextField txtPrice;
    public JFXComboBox<String> cmbStatus;
    public JFXTextField txtDeliveryIdUpdate;
    public JFXTextField txtDriverIdUpdate;
    public JFXTextField txtOrderIdUpdate;
    public JFXComboBox<String> cmbStatusUpdate;
    public JFXTextField txtDateUpdate;
    public JFXTextField txtTimeUpdate;
    public JFXTextField txtPriceUpdate;
    public JFXComboBox<String> cmbDriverIdUpdate;
    public JFXComboBox<String> cmbOrderIdUpdate;
    public JFXButton btnAddDelivery;
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

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    Pattern deliveryIdPattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Z\\d]{4,}$");
    Pattern deliveryPricePattern = Pattern.compile("^\\d+(,\\d{1,2})?$");
    LinkedHashMap<JFXTextField, Pattern> map1 = new LinkedHashMap<>();
    Pattern delIdPattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Z\\d]{4,}$");
    Pattern delPricePattern = Pattern.compile("^\\d+(,\\d{1,2})?$");

    public void initialize() throws SQLException, ClassNotFoundException {


        storeValidations();
        btnAddDelivery.setDisable(true);
        btnUpdate.setDisable(true);
        cmbStatus.getItems().add("On Delivery");
        cmbStatus.getItems().add("Complete");
        cmbStatus.getItems().add("Cancel");

        cmbStatusUpdate.getItems().add("On Delivery");
        cmbStatusUpdate.getItems().add("Complete");
        cmbStatusUpdate.getItems().add("Cancel");

        setDriverIds1();
        setDriverIds2();
        setOrderIds1();
        setOrderIds2();
        loadDateAndTime();

        colDeliverId.setCellValueFactory(new PropertyValueFactory("deliveryId"));
        colDriverId.setCellValueFactory(new PropertyValueFactory("driverId"));
        colOrderId.setCellValueFactory(new PropertyValueFactory("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colTime.setCellValueFactory(new PropertyValueFactory("time"));
        colPrice.setCellValueFactory(new PropertyValueFactory("deliveryPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory("status"));




        loadAllDelivery();
        setAutoIds();
    }

    private void storeValidations() {

        btnAddDelivery.setDisable(true);
        btnUpdate.setDisable(true);
        map.put(txtDeliveryId, deliveryIdPattern);
        map.put(txtPrice, deliveryPricePattern);
        map1.put(txtDeliveryIdUpdate, delIdPattern);
        map1.put(txtPriceUpdate, delPricePattern);
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
        txtDeliveryId.setText(GenerateAutoId.autoId("SELECT d_code FROM delivery ORDER BY d_code DESC LIMIT 1", 1, 3));
    }

    private void setOrderIds1() {
        try {
            ObservableList<String> sIdObList = FXCollections.observableArrayList(
                    OrderCrudController.getOrderId()
            );
            cmbOrderId.setItems(sIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setOrderIds2() {
        try {
            ObservableList<String> sIdObList = FXCollections.observableArrayList(
                    OrderCrudController.getOrderId()
            );
            cmbOrderIdUpdate.setItems(sIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setDriverIds1() {
        try {
            ObservableList<String> sIdObList = FXCollections.observableArrayList(
                    DriverCrudController.getDriverId()
            );
            cmbDriverId.setItems(sIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void setDriverIds2() {
        try {
            ObservableList<String> sIdObList = FXCollections.observableArrayList(
                    DriverCrudController.getDriverId()
            );
            cmbDriverIdUpdate.setItems(sIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void loadAllDelivery() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM Delivery");
        ObservableList<Delivery> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Delivery(
                            result.getString("d_code"),
                            result.getString("driver_id"),
                            result.getString("order_id"),
                            result.getString("date"),
                            result.getString("time"),
                            result.getDouble("price"),
                            result.getString("status")
                    )
            );
        }
        tblDelivery.setItems(obList);



    }

    public void btnAddDeliveryOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Delivery d = new Delivery(txtDeliveryId.getText(),cmbDriverId.getValue(),cmbOrderId.getValue(),String.valueOf(txtDate.getValue()),String.valueOf(txtTime.getValue()),Double.parseDouble(txtPrice.getText()),cmbStatus.getValue());

        try {
            if(CrudUtil.execute("INSERT INTO delivery VALUES (?,?,?,?,?,?,?)",d.getDeliveryId(),d.getDriverId(),d.getOrderId(),d.getDate(),d.getTime(),d.getDeliveryPrice(),d.getStatus())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").show();

            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        clearAllTexts();
        loadAllDelivery();
        setAutoIds();

    }

    public void txtDeliveryIdUpdateOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM delivery WHERE d_code = ?",txtDeliveryIdUpdate.getText());
            if (result.next()) {

                cmbDriverIdUpdate.setValue(result.getString(2));
                cmbOrderIdUpdate.setValue(result.getString(3));
                txtDateUpdate.setText(result.getString(4));
                txtTimeUpdate.setText(result.getString(5));
                txtPriceUpdate.setText(result.getString(6));
                cmbStatusUpdate.setValue(result.getString(7));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result!..").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Delivery d = new Delivery(txtDeliveryIdUpdate.getText(),cmbDriverIdUpdate.getValue(),cmbOrderIdUpdate.getValue(),txtDateUpdate.getText(),txtTimeUpdate.getText(),Double.parseDouble(txtPriceUpdate.getText()),cmbStatusUpdate.getValue());


        try{
            boolean isUpdated = CrudUtil.execute("UPDATE Delivery SET driver_id = ?, order_id = ?, date = ?, time = ?, price = ?, status = ? WHERE d_code = ?",d.getDriverId(),d.getOrderId(),d.getDate(),d.getTime(),d.getDeliveryPrice(),d.getStatus(),d.getDeliveryId());

            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated.").show();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }


        }catch (SQLException | ClassNotFoundException e){

        }
        clearTextFieldsOnUpdate();
        loadAllDelivery();

    }

    private void clearAllTexts() {
        txtDeliveryId.clear();
        txtPrice.clear();
        cmbStatus.setValue(null);
        cmbOrderId.setValue(null);
        cmbDriverId.setValue(null);
    }

    private void clearTextFieldsOnUpdate() {
        cmbDriverIdUpdate.setValue(null);
        cmbOrderIdUpdate.setValue(null);
        cmbStatusUpdate.setValue(null);
        txtDeliveryIdUpdate.clear();
        txtPriceUpdate.clear();
    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException {

        String deliveryId = txtDeliveryId.getText();
        String driverId = cmbDriverId.getValue();
        String orderId = cmbOrderId.getValue();
        double deliveryPrice = Double.parseDouble(txtPrice.getText());

        HashMap paramMap = new HashMap();
        paramMap.put("deliveryId", deliveryId);
        paramMap.put("driverId", orderId);
        paramMap.put("orderId", driverId);
        paramMap.put("deliveryPrice", deliveryPrice);

        JasperReport compileReport= (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/views/reports/DeliveryBills.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,paramMap,new JREmptyDataSource(1));
        JasperViewer.viewReport(jasperPrint,false);


    }


    public void textFieldValidationOnAction(KeyEvent keyEvent) {

        Object response = ValidationUtil.validateJFXTextField(map, btnAddDelivery);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }

    }

    public void textFieldsOnAction(KeyEvent keyEvent) {

        Object response = ValidationUtil.validateJFXTextField(map1,btnUpdate);
        if (keyEvent.getCode() == KeyCode.ENTER){
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                System.out.println(errorText);
//                System.out.println(keyEvent.getCode());
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }



    }
}
