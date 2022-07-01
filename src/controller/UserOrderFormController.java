package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
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
import model.Meal;
import model.Order;
import model.OrderDetail;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.GenerateAutoId;
import util.ValidationUtil;
import views.tm.OrderTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

public class UserOrderFormController {
    public AnchorPane HomeContext;
    public Label lblDate;
    public Label lblTime;
    public JFXComboBox<String> cmbCusId;
    public TableView<OrderTM> tblOrders;
    public TableColumn colOrderId;
    public TableColumn colCusId;
    public TableColumn colCusName;
    public TableColumn colMeal;
    public TableColumn colQty;
    public TableColumn colMealPrice;
    public TableColumn colOperate;
    public JFXComboBox<String> cmbMeals;
    public TextField txtMealPrice;
    public TextField txtMealQty;
    public TextField txtOrderId;
    public TextField txtCuName;
    public TextField txtTotal;
    public JFXTextField txtGetAmounts;
    public TextField txtExchangeCost;
    public JFXButton btnAddedCart;

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

    LinkedHashMap<TextField, Pattern> odr = new LinkedHashMap<>();
    Pattern orderIdPattern = Pattern.compile("^(O)[0-9]{3}$");
    Pattern orderQtyPattern = Pattern.compile("^[0-9]{1,}$");



    public void initialize() throws SQLException, ClassNotFoundException {


        storeValidations();


        colOrderId.setCellValueFactory(new PropertyValueFactory("OrderId"));
        colCusId.setCellValueFactory(new PropertyValueFactory("orderCustomerId"));
        colCusName.setCellValueFactory(new PropertyValueFactory("orderCustomerName"));
        colMeal.setCellValueFactory(new PropertyValueFactory("description"));
        colQty.setCellValueFactory(new PropertyValueFactory("orderQty"));
        colMealPrice.setCellValueFactory(new PropertyValueFactory("orderUnitPrice"));
        colOperate.setCellValueFactory(new PropertyValueFactory("btn"));

        loadDateAndTime();
        setCustomerIds();
        setMealNames();

        cmbCusId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerDetails(newValue);
        });

        cmbMeals.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setMealDetails(newValue);

            ResultSet result = null;
            try {
                result = CrudUtil.execute("SELECT * FROM meal WHERE name=?", newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                if (result.next()) {
                    txtMealPrice.setText(String.valueOf(result.getDouble(4)));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        setAutoIds();


        txtGetAmounts.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtGetAmounts.getText().equals("")) {

                if (!txtTotal.getText().equals("") && !txtGetAmounts.getText().equals("")) {
                    double tC = Double.parseDouble(txtTotal.getText());
                    int amount = Integer.parseInt(txtGetAmounts.getText());

                    txtExchangeCost.setText(String.valueOf(amount - tC));
                }


            }
        });

        loadDateAndTime();
        setAutoIds();
    }

    private void storeValidations() {

        odr.put(txtOrderId, orderIdPattern);
        odr.put(txtMealQty, orderQtyPattern);

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
        txtOrderId.setText(GenerateAutoId.autoId("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1", 1, 3));
    }


    private void setMealDetails(Object selectedItemName) {

        try {
            Meal m = MealCrudController.getMeal((String) selectedItemName);
            if (m != null) {
                txtMealPrice.setText(String.valueOf(m.getmPrice()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setCustomerDetails(Object selectedCustomerId) {

        try {
            Customer c = CustomerCrudController.getCustomer((String) selectedCustomerId);
            if (c != null) {
                txtCuName.setText(c.getCustomerName());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setMealNames() {

        try {
            ObservableList<String> cIdObList = FXCollections.observableArrayList(
                    MealCrudController.getMealCode()
            );
            cmbMeals.setItems(cIdObList);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setCustomerIds() {

        try {
            ObservableList<String> cIdObList = FXCollections.observableArrayList(
                    CustomerCrudController.getCustomerID()
            );
            cmbCusId.setItems(cIdObList);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void AddCustomerOnAction(ActionEvent actionEvent) throws IOException {
        lordWidow2("../views/AddCustomerForm.fxml","Add Customer");
    }

    public void AddMealOnAction(ActionEvent actionEvent) throws IOException {
        lordWidow3("../views/AddMealForm.fxml","Add Meal");
    }
    public void lordWidow2(String location, String title) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AddCustomerFormController controller = loader.getController();

        controller.getAllData(HomeContext);

        Stage stage = new Stage(StageStyle.UTILITY);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();


    }

    public void lordWidow3(String location, String title) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AddMealFormController controller = loader.getController();

        controller.getAllData(HomeContext);

        Stage stage = new Stage(StageStyle.UTILITY);

        stage.setScene(scene);
        stage.show();

    }


    public void btnPlaceOrderOnAction(ActionEvent actionEvent) throws ParseException {

        ArrayList<OrderDetail> items = new ArrayList<>();
        for (OrderTM temp : tmList) {
            items.add(new OrderDetail(txtOrderId.getText(),temp.getDescription(),Double.valueOf(temp.getTotal())));
        }

        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());


        Order or = new Order(txtOrderId.getText(),cmbCusId.getValue(),txtCuName.getText(),cmbMeals.getValue(),Integer.parseInt(txtMealQty.getText()),Double.parseDouble(txtMealPrice.getText()),date,items);
        if (placeOrder(or)){
            new Alert(Alert.AlertType.CONFIRMATION,"Your Order is Succesfull!").show();
            setAutoIds();

        }else {
            new Alert(Alert.AlertType.WARNING,"Your Order Fail").show();
        }
        clearAllTexts();
        setAutoIds();
    }


    ObservableList<OrderTM> tmList = FXCollections.observableArrayList();
    public void btnAddedCartOnAction(ActionEvent actionEvent) {

        double unitPrice = Double.parseDouble(txtMealPrice.getText());
        int qty = Integer.parseInt(txtMealQty.getText());
        double totalCost = unitPrice * qty;

        Button btn = new Button("Delete");
        OrderTM tm = new OrderTM(
                txtOrderId.getText(),
                cmbCusId.getValue(),
                txtCuName.getText(),
                cmbMeals.getValue(),
                qty,
                unitPrice,
                totalCost,
                btn
        );
        int isExists = isExists(tm);
        if (isExists == -1) {
            btn.setOnAction(e -> {
                tmList.remove(tm);
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!..").show();

                calculateTotal();
                //clearAllTexts();
            });
            tmList.add(tm);
            calculateTotal();

        } else {
            OrderTM temp = tmList.get(isExists);
            int nq = (temp.getOrderQty()) + Integer.parseInt(txtMealQty.getText());
            double pr = nq * Double.parseDouble(txtMealPrice.getText());

            OrderTM newTm = new OrderTM(temp.getOrderId(), temp.getOrderCustomerId(), temp.getOrderCustomerName(), temp.getDescription(), nq, pr, pr, temp.getBtn());
            tmList.remove(isExists);
            tmList.add(newTm);
        }
        tblOrders.setItems(tmList);
        //tblOrders.refresh();
        calculateTotal();

    }

    private int isExists(OrderTM tm) {
        for (int i = 0; i < tmList.size(); i++) {
            if (tm.getDescription().equalsIgnoreCase(tmList.get(i).getDescription())) {
                return i;
            }
        }
        return -1;
    }


    private void calculateTotal() {
        double total = 0;
        for (OrderTM tm : tmList
        ) {
            total += tm.getTotal();
        }
        txtTotal.setText(String.valueOf(total));

    }

    private void clearAllTexts() {


        txtOrderId.clear();
        cmbCusId.setValue(null);
        txtCuName.clear();
        cmbMeals.setValue(null);
        txtMealQty.clear();
        txtMealPrice.clear();
    }

    public void textFieldValidationOnAction(KeyEvent keyEvent) {
        Object response = ValidationUtil.validateTextField(odr, btnAddedCart);
        if (keyEvent.getCode() == KeyCode.ENTER){
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                System.out.println(errorText);
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }
    }


    private boolean placeOrder(Order od) {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement("INSERT INTO `Orders` VALUES(?,?,?,?,?,?,?) ");
            stm.setObject(1, od.getOrderId());
            stm.setObject(2, od.getOrderCustomerId());
            stm.setObject(3, od.getOrderCustomerName());
            stm.setObject(4, od.getDescription());
            stm.setObject(5, od.getOrderQty());
            stm.setObject(6, od.getOrderUnitPrice());
            stm.setObject(7, od.getDate());

            if (stm.executeUpdate() > 0) {
                if (saveOrderDetails(od.getOrderId(), od.getItems())) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;

    }

    private boolean saveOrderDetails(String orderId, ArrayList<OrderDetail> details) throws SQLException, ClassNotFoundException {
        for (OrderDetail temp : details
        ) {
            if (CrudUtil.execute("INSERT INTO OrderDetail VALUES(?,?,?)", orderId, temp.getMeal_code(), temp.getTotal())) {

            } else {
                return false;
            }
        }
        return true;

    }

    public void btnPayOnAction(ActionEvent actionEvent) {

        txtOrderId.setText(null);
        setAutoIds();
        String orderId = txtOrderId.getText();
        double total = Double.parseDouble(txtTotal.getText());
        double exchange = Double.parseDouble(txtExchangeCost.getText());

        HashMap paramMap = new HashMap();

        paramMap.put("orderId", orderId);// id = report param name // customerID = UI typed value
        paramMap.put("amount", total);
        paramMap.put("bal", exchange);


        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/OrderBills.jasper"));
            ObservableList<OrderTM> tableRecords = tblOrders.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JRBeanArrayDataSource(tblOrders.getItems().toArray()));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }

        clearAllTexts();
        setAutoIds();

    }


}
