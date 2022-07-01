package model;

public class Customer {

    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerNIC;
    private String customerNumber;

    public Customer() {
    }

    public Customer(String customerId, String customerName, String customerAddress, String customerNIC, String customerNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerNIC = customerNIC;
        this.customerNumber = customerNumber;
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

    public String getCustomerNIC() {
        return customerNIC;
    }

    public void setCustomerNIC(String customerNIC) {
        this.customerNIC = customerNIC;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerNIC='" + customerNIC + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerNumber='" + customerNumber + '\'' +
                '}';
    }
}
