package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Meal;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddMealFormController {
    public JFXTextField txtMealID;
    public JFXTextField txtMealName;
    public JFXTextField txtMealQty;
    public JFXTextField txtMealPrice;
    public AnchorPane HomeContext;
    public JFXButton btnAddMeal;


    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    Pattern mealCodePattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Z\\d]{4,}$");
    Pattern mealNamePattern = Pattern.compile("^[A-z ]{3,}$");
    Pattern mealPricePattern = Pattern.compile("^\\d+(,\\d{1,2})?$");
    Pattern mealQtyPattern = Pattern.compile("^[0-9]{2}$");


    public void initialize(){
        btnAddMeal.setDisable(true);
        setAutoIds();
        storeValidations();
    }

    private void storeValidations() {
        btnAddMeal.setDisable(true);
        map.put(txtMealID, mealCodePattern);
        map.put(txtMealName, mealNamePattern);
        map.put(txtMealQty, mealPricePattern);
        map.put(txtMealPrice, mealQtyPattern);

    }


    private void setAutoIds() {
        txtMealID.setText(GenerateAutoId.autoId("SELECT code FROM meal ORDER BY code DESC LIMIT 1", 1, 3));
    }

    public void AddMealOnAction(ActionEvent actionEvent) {

        Meal m = new Meal(txtMealID.getText(),txtMealName.getText(),Integer.parseInt(txtMealQty.getText()),Double.parseDouble(txtMealPrice.getText()));

        try {
            if(CrudUtil.execute("INSERT INTO meal VALUES (?,?,?,?)",m.getmCode(),m.getmName(),m.getmQty(),m.getmPrice())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        clearAllTexts();
        setAutoIds();
    }

    public void getAllData(AnchorPane homeContext) {
        this.HomeContext = homeContext;
    }

    private void clearAllTexts() {

        txtMealID.clear();
        txtMealName.clear();
        txtMealQty.clear();
        txtMealPrice.clear();

    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(map, btnAddMeal);
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
