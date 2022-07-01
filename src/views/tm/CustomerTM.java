package views.tm;

import javafx.scene.control.Button;

public class CustomerTM {

    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerNIC;
    private String customerNumber;
    private Button btn;

    public CustomerTM() {
    }

    public CustomerTM(String customerId, String customerName, String customerAddress, String customerNIC, String customerNumber, Button btn) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerNIC = customerNIC;
        this.customerNumber = customerNumber;
        this.btn = btn;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerNIC() {
        return customerNIC;
    }

    public void setCustomerNIC(String customerNIC) {
        this.customerNIC = customerNIC;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "CustomerTM{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerNIC='" + customerNIC + '\'' +
                ", customerNumber='" + customerNumber + '\'' +
                ", btn=" + btn +
                '}';
    }
}
