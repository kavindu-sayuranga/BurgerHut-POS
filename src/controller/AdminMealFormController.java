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
import model.Meal;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;
import views.tm.MealTM;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminMealFormController {
    public AnchorPane HomeContext;
    public JFXTextField txtMealId;
    public JFXTextField txtMealName;
    public JFXTextField txtDesc;
    public TableColumn colMId;
    public TableColumn colMName;
    public TableColumn colMPrice;
    public JFXTextField txtPrice;
    public TableColumn colMQty;
    public Label lblDate;
    public Label lblTime;
    public TableView<MealTM> tblMeal;
    public TableColumn colMOperate;
    public JFXButton btnAddMeal;

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

    LinkedHashMap<JFXTextField, Pattern> meal = new LinkedHashMap<>();
    Pattern mealCodePattern = Pattern.compile("^(M)[0-9]{3}$");
    Pattern mealNamePattern = Pattern.compile("^[A-z ]{4,25}$");
    Pattern mealQtyPattern = Pattern.compile("^[0-9]{1,}$");
    Pattern mealPricePattern = Pattern.compile("^\\d+(,\\d{1,2})?$");


    public void initialize(){

        btnAddMeal.setDisable(true);
        storeValidations();

        colMId.setCellValueFactory(new PropertyValueFactory("meal_code"));
        colMName.setCellValueFactory(new PropertyValueFactory("meal_name"));
        colMQty.setCellValueFactory(new PropertyValueFactory("meal_qty"));
        colMPrice.setCellValueFactory(new PropertyValueFactory("meal_price"));
        colMOperate.setCellValueFactory(new PropertyValueFactory("remove"));


        try {
            loadAllMeals();
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        loadDateAndTime();
        setAutoIds();
    }

    private void storeValidations() {
        btnAddMeal.setDisable(true);

        meal.put(txtMealId, mealCodePattern);
        meal.put(txtMealName, mealNamePattern);
        meal.put(txtDesc, mealQtyPattern);
        meal.put(txtPrice, mealPricePattern);


    }

    private void setAutoIds() {
        txtMealId.setText(GenerateAutoId.autoId("SELECT code FROM meal ORDER BY code DESC LIMIT 1", 1, 3));
    }

    private void loadAllMeals() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM meal");
        ObservableList<MealTM> obList = FXCollections.observableArrayList();

        while (result.next()){
            Button btn=new Button("Delete");
            btn.setOnAction(event -> {
                MealTM selectedItem = (MealTM) tblMeal.getSelectionModel().getSelectedItem();
                txtMealId.setText(selectedItem.getMeal_code());


                deleteCustomer();

                try {
                    loadAllMeals();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            obList.add(
                    new MealTM(
                            result.getString("code"),
                            result.getString("name"),
                            result.getInt("qty"),
                            result.getDouble("price"),
                            btn
                    )
            );
        }
        tblMeal.setItems(obList);
        clearAllTexts();
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

    public void btnAddMealOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Meal m = new Meal(txtMealId.getText(),txtMealName.getText(),Integer.parseInt(txtDesc.getText()),Double.parseDouble(txtPrice.getText()));

        try {
            if(CrudUtil.execute("INSERT INTO meal VALUES (?,?,?,?)",m.getmCode(),m.getmName(),m.getmQty(),m.getmPrice())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        loadAllMeals();
        clearAllTexts();
        setAutoIds();

    }

    private void deleteCustomer() {
        try{

            if(CrudUtil.execute("DELETE FROM meal WHERE code=?",txtMealId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
                loadAllMeals();

            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){

        }

    }

    private void clearAllTexts() {
        txtMealId.clear();
        txtMealName.clear();
        txtDesc.clear();
        txtPrice.clear();
        txtMealId.requestFocus();


    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {

        Object response = ValidationUtil.validateJFXTextField(meal, btnAddMeal);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof JFXTextField) {
                JFXTextField errorText = (JFXTextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }

    }
}
