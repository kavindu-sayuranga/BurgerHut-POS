package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddCustomerFormController {
    public JFXTextField txtCusID;
    public JFXTextField txtCusName;
    public JFXTextField txtCusAddress;
    public JFXTextField txtCusNIC;
    public JFXTextField txtCusPhone;
    public AnchorPane HomeContext;
    public JFXButton btnAddCustomer;

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    Pattern customerIdPattern = Pattern.compile("^(C)[0-9]{3}$");
    Pattern customerNamePattern = Pattern.compile("^[A-z ]{3,}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9, ]{3,}$");
    Pattern nicPattern = Pattern.compile("^[0-9]{12}$");
    Pattern phonePattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");


    public void initialize(){
        setAutoIds();
        storeValidations();

    }

    private void storeValidations() {
        map.put(txtCusID, customerIdPattern);
        map.put(txtCusName, customerNamePattern);
        map.put(txtCusAddress, addressPattern);
        map.put(txtCusNIC, nicPattern);
        map.put(txtCusPhone, phonePattern );

    }


    private void setAutoIds() {
        txtCusID.setText(GenerateAutoId.autoId("SELECT cus_id FROM customer ORDER BY cus_id DESC LIMIT 1", 1, 3));
    }


    public void AddCustomerOnAction(ActionEvent actionEvent) {

        Customer c = new Customer(txtCusID.getText(),txtCusName.getText(),txtCusAddress.getText(),txtCusNIC.getText(),txtCusPhone.getText());

        try {
            if(CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?,?)",c.getCustomerId(),c.getCustomerName(),c.getCustomerAddress(),c.getCustomerNIC(),c.getCustomerNumber())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved !...").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        clearAllTexts();
        setAutoIds();
    }

    private void clearAllTexts() {
        txtCusID.clear();
        txtCusName.clear();
        txtCusAddress.clear();
        txtCusNIC.clear();
        txtCusPhone.clear();
        txtCusID.requestFocus();

    }

    public void getAllData(AnchorPane homeContext) {
        this.HomeContext = homeContext;
    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateJFXTextField(map, btnAddCustomer);
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
