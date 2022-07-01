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
import model.Supplier;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;
import views.tm.SupplierTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminSupplierFormController {
    public AnchorPane HomeContext;
    public TableView<SupplierTM> tblSupplier;
    public TableColumn colSupId;
    public TableColumn colSupName;
    public TableColumn colSupNIC;
    public TableColumn colSupAddress;
    public TableColumn colSupNumber;
    public TableColumn colSupOperate;
    public JFXTextField txtSupId;
    public JFXTextField txtSupName;
    public JFXTextField txtSupNIC;
    public JFXTextField txtSupAddress;
    public JFXTextField txtSupNumber;
    public JFXTextField txtSupIdUpdate;
    public JFXTextField txtSupNameUpdate;
    public JFXTextField txtSupNICUpdate;
    public JFXTextField txtSupAddressUpdate;
    public JFXTextField txtSupNumberUpdate;
    public Label lblDate;
    public Label lblTime;
    public JFXButton btnAddSupplier;
    public JFXButton btnSupUpdate;

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


    LinkedHashMap<JFXTextField, Pattern> sup = new LinkedHashMap<>();
    Pattern supplierIdPattern = Pattern.compile("^(S)[0-9]{3}$");
    Pattern supplierNamePattern = Pattern.compile("^[A-z ]{4,25}$");
    Pattern supplierAddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
    Pattern supplierNicPattern = Pattern.compile("^[0-9]{12}$");
    Pattern supplierPhonePattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");
    LinkedHashMap<JFXTextField, Pattern> sup1 = new LinkedHashMap<>();
    Pattern supIdPattern = Pattern.compile("^(S)[0-9]{3}$");
    Pattern supNamePattern = Pattern.compile("^[A-z ]{4,25}$");
    Pattern supAddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
    Pattern supNicPattern = Pattern.compile("^[0-9]{12}$");
    Pattern supPhonePattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");

    public void initialize(){
        loadDateAndTime();
        btnAddSupplier.setDisable(true);
        btnSupUpdate.setDisable(true);
        storeValidations();


        colSupId.setCellValueFactory(new PropertyValueFactory("SupplierId"));
        colSupName.setCellValueFactory(new PropertyValueFactory("SupplierName"));
        colSupAddress.setCellValueFactory(new PropertyValueFactory("SupplierAddress"));
        colSupNIC.setCellValueFactory(new PropertyValueFactory("SupplierNic"));
        colSupNumber.setCellValueFactory(new PropertyValueFactory("SupplierPhone"));
        colSupOperate.setCellValueFactory(new PropertyValueFactory("btn"));

        try {
            loadAllSupplier();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        setAutoIds();

    }

    private void storeValidations() {
        btnAddSupplier.setDisable(true);
        btnSupUpdate.setDisable(true);

        sup.put(txtSupId, supplierIdPattern);
        sup.put(txtSupName, supplierNamePattern);
        sup.put(txtSupAddress, supplierAddressPattern);
        sup.put(txtSupNIC, supplierNicPattern);
        sup.put(txtSupNumber, supplierPhonePattern );
        sup1.put(txtSupIdUpdate, supIdPattern);
        sup1.put(txtSupNameUpdate, supNamePattern);
        sup1.put(txtSupAddressUpdate, supAddressPattern);
        sup1.put(txtSupNICUpdate, supNicPattern);
        sup1.put(txtSupNumberUpdate, supPhonePattern);
    }


    private void setAutoIds() {
        txtSupId.setText(GenerateAutoId.autoId("SELECT sup_id FROM supplier ORDER BY sup_id DESC LIMIT 1", 1, 3));
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

    public void btnAddSupplierOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Supplier s = new Supplier(txtSupId.getText(),txtSupName.getText(),txtSupAddress.getText(),txtSupNIC.getText(),txtSupNumber.getText());

        try {
            if(CrudUtil.execute("INSERT INTO Supplier VALUES (?,?,?,?,?)",s.getSupplierId(),s.getSupplierName(),s.getSupplierAddress(),s.getSupplierNic(),s.getSupplierPhone())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").showAndWait();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).showAndWait();
        }
        clearAllTexts();
        loadAllSupplier();
        setAutoIds();


    }



    private void loadAllSupplier() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier");
        ObservableList<SupplierTM> obList = FXCollections.observableArrayList();

        while (result.next()){
            Button btn=new Button("Delete");
            btn.setOnAction(event -> {
                SupplierTM selectedItem= (SupplierTM) tblSupplier.getSelectionModel().getSelectedItem();
                txtSupId.setText(selectedItem.getSupplierId());


                deleteSupplier();

                try {
                    loadAllSupplier();
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            });

            obList.add(
                    new SupplierTM(
                            result.getString("Sup_id"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("NIC"),
                            result.getString("phone_number"),
                            btn
                    )
            );
        }
        tblSupplier.setItems(obList);
        clearAllTexts();



    }

    private void deleteSupplier() {

        try{

            if(CrudUtil.execute("DELETE FROM Supplier WHERE Sup_id=?",txtSupId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").showAndWait();
                loadAllSupplier();


            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").showAndWait();
            }

        }catch (SQLException | ClassNotFoundException e){

        }

    }


    public void txtSupIdOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM supplier WHERE sup_id = ?",txtSupIdUpdate.getText());
            if (result.next()) {
                txtSupNameUpdate.setText(result.getString(2));
                txtSupAddressUpdate.setText(result.getString(3));
                txtSupNICUpdate.setText(result.getString(4));
                txtSupNumberUpdate.setText(result.getString(5));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result!..").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }



    }

    public void btnSupUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Supplier s = new Supplier(txtSupIdUpdate.getText(),txtSupNameUpdate.getText(),txtSupAddressUpdate.getText(),txtSupNICUpdate.getText(),txtSupNumberUpdate.getText());


        try{
            boolean isUpdated = CrudUtil.execute("UPDATE  supplier SET name = ?, address = ?, NIC = ?, phone_number = ? WHERE sup_id = ?",s.getSupplierName(),s.getSupplierAddress(),s.getSupplierNic(),s.getSupplierPhone(),s.getSupplierId());

            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated.").show();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }


        }catch (SQLException | ClassNotFoundException e){

        }
        clearAllTexts2();
        loadAllSupplier();


    }

    private void clearAllTexts() {
        txtSupId.clear();
        txtSupName.clear();
        txtSupAddress.clear();
        txtSupNIC.clear();
        txtSupNumber.clear();
        txtSupId.requestFocus();
    }

    private void clearAllTexts2() {
        txtSupIdUpdate.clear();
        txtSupNameUpdate.clear();
        txtSupAddressUpdate.clear();
        txtSupNICUpdate.clear();
        txtSupNumberUpdate.clear();
        txtSupIdUpdate.requestFocus();
    }

    public void textFieldOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(sup, btnAddSupplier);
        if (keyEvent.getCode() == KeyCode.ENTER){
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                System.out.println(errorText);
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }
    }

    public void textFieldsOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(sup1,btnSupUpdate);
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
