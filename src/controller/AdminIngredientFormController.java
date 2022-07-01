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
import model.Ingredient;
import model.Supplier;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;
import views.tm.EmployeeTM;
import views.tm.IngredientTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminIngredientFormController {
    public AnchorPane HomeContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<IngredientTM> tblIngredient;
    public TableColumn colIngId;
    public TableColumn colIngName;
    public TableColumn colSupId;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colOperate;
    public JFXTextField txtIngId;
    public JFXComboBox<String> cmbSupId;
    public JFXTextField txtIngName;
    public JFXTextField txtIngIdUpdate;
    public JFXTextField ttQtyUpdate;
    public JFXTextField txtUnitPriceUpdate;
    public JFXComboBox<String> cmbSupIdUpdate;
    public JFXTextField txtIngNameUpdate;
    public JFXTextField txtIngQty;
    public JFXTextField txtIngUnitPrice;
    public JFXButton btnUpdate;
    public JFXButton btnAddIngredient;

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

    LinkedHashMap<JFXTextField, Pattern> itm = new LinkedHashMap<>();
    Pattern ItemIdPattern = Pattern.compile("^(I)[0-9]{3}$");
    Pattern ItemNamePattern = Pattern.compile("^[A-z ]{4,50}$");
    LinkedHashMap<TextField, Pattern> itm1 = new LinkedHashMap<>();
    Pattern ItemPricePattern = Pattern.compile("^\\d+(,\\d{1,2})?$");
    Pattern ItemQtyPattern = Pattern.compile("^[0-9]{2}$");
    LinkedHashMap<JFXTextField, Pattern> itm2 = new LinkedHashMap<>();
    Pattern ItmIdPattern = Pattern.compile("^(I)[0-9]{3}$");
    Pattern ItmNamePattern = Pattern.compile("^[A-z ]{4,25}$");
    Pattern ItmPricePattern = Pattern.compile("^\\d+(,\\d{1,2})?$");
    Pattern ItmQtyPattern = Pattern.compile("^[0-9]{2}$");


    public void initialize() throws SQLException, ClassNotFoundException {

        btnAddIngredient.setDisable(true);
        btnUpdate.setDisable(true);
        storeValidations();

        setSupplierIds();
        setSupplierIds2();

        colIngId.setCellValueFactory(new PropertyValueFactory("ingId"));
        colIngName.setCellValueFactory(new PropertyValueFactory("ingName"));
        colSupId.setCellValueFactory(new PropertyValueFactory("supId"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colOperate.setCellValueFactory(new PropertyValueFactory("btn"));

        try {
            loadAllIngredient();
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        setAutoIds();
        loadDateAndTime();
    }

    private void storeValidations() {
        btnAddIngredient.setDisable(true);
        btnUpdate.setDisable(true);
        itm.put(txtIngId, ItemIdPattern);
        itm.put(txtIngName, ItemNamePattern);
        itm1.put(txtIngUnitPrice, ItemPricePattern);
        itm1.put(txtIngQty, ItemQtyPattern);
        itm2.put(txtIngIdUpdate, ItmIdPattern);
        itm2.put(txtIngNameUpdate, ItmNamePattern);
        itm2.put(txtUnitPriceUpdate, ItmPricePattern);
        itm2.put(ttQtyUpdate, ItmQtyPattern);
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

    private void setAutoIds() {
        txtIngId.setText(GenerateAutoId.autoId("SELECT ing_id FROM ingredient ORDER BY ing_id DESC LIMIT 1", 1, 3));
    }

    private void loadAllIngredient() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM Ingredient");
        ObservableList<IngredientTM> obList = FXCollections.observableArrayList();

        while (result.next()){
            Button btn=new Button("Delete");
            btn.setOnAction(event -> {
                IngredientTM selectedItem = (IngredientTM) tblIngredient.getSelectionModel().getSelectedItem();
                txtIngId.setText(selectedItem.getIngId());


                deleteEmployee();

                try {
                    loadAllIngredient();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            obList.add(
                    new IngredientTM(
                            result.getString("ing_id"),
                            result.getString("name"),
                            result.getString("sup_id"),
                            result.getInt("qty"),
                            result.getDouble("unit_price"),
                            btn
                    )
            );
        }
        tblIngredient.setItems(obList);
        //clearAllTexts();
    }


    private void setSupplierIds() {
        try {

            ObservableList<String> sIdObList = FXCollections.observableArrayList(
                    SupplierCrudController.getSupplierId()
            );
            cmbSupId.setItems(sIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setSupplierIds2() {
        try {

            ObservableList<String> sIdObList = FXCollections.observableArrayList(
                    SupplierCrudController.getSupplierId()
            );
            cmbSupIdUpdate.setItems(sIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void btnAddIngredientOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Ingredient in = new Ingredient(txtIngId.getText(),txtIngName.getText(),cmbSupId.getValue(),Integer.parseInt(txtIngQty.getText()),Double.parseDouble(txtIngUnitPrice.getText()));

        try {
            if(CrudUtil.execute("INSERT INTO Ingredient VALUES (?,?,?,?,?)",in.getIngId(),in.getIngName(),in.getSupId(),in.getQty(),in.getUnitPrice())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        clearAllTexts();
        loadAllIngredient();
        setAutoIds();
    }




    public void txtIngIdUpdateOnAction(ActionEvent actionEvent) {

        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Ingredient WHERE ing_id = ?",txtIngIdUpdate.getText());
            if (result.next()) {
                txtIngNameUpdate.setText(result.getString(2));
                cmbSupIdUpdate.setValue(result.getString(3));
                ttQtyUpdate.setText(result.getString(4));
                txtUnitPriceUpdate.setText(result.getString(5));


            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result!..").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Ingredient in = new Ingredient(txtIngIdUpdate.getText(),txtIngNameUpdate.getText(),cmbSupIdUpdate.getValue(),Integer.parseInt(ttQtyUpdate.getText()),Double.parseDouble(txtUnitPriceUpdate.getText()));


        try{
            boolean isUpdated = CrudUtil.execute("UPDATE  Ingredient SET name = ?, sup_id = ?, qty = ?, unit_price = ? WHERE ing_id = ?",in.getIngName(),in.getSupId(),in.getQty(),in.getUnitPrice(),in.getIngId());

            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated.").show();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }


        }catch (SQLException | ClassNotFoundException e){

        }

        clearAllTexts2();
        loadAllIngredient();

    }



    private void deleteEmployee() {

        try{

            if(CrudUtil.execute("DELETE FROM Ingredient WHERE ing_id=?",txtIngId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").showAndWait();
                loadAllIngredient();

            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){

        }
    }

    private void clearAllTexts() {

        txtIngId.clear();
        txtIngName.clear();
        txtIngQty.clear();
        txtIngUnitPrice.clear();


        txtIngId.requestFocus();
    }

    private void clearAllTexts2() {

        txtIngIdUpdate.clear();
        txtIngNameUpdate.clear();
        ttQtyUpdate.clear();
        txtUnitPriceUpdate.clear();


        txtIngIdUpdate.requestFocus();
    }

    public void textNormalOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateTextField(itm1, btnAddIngredient);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                System.out.println(errorText);
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }
    }

    public void textFieldsOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(itm2,btnUpdate);
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
