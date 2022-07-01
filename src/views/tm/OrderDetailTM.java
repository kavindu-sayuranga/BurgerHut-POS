package views.tm;

public class OrderDetailTM {

    private String orderDetail_id;
    private String meal_code;
    private double total;

    public OrderDetailTM() {
    }

    public OrderDetailTM(String orderDetail_id, String meal_code, double total) {
        this.orderDetail_id = orderDetail_id;
        this.meal_code = meal_code;
        this.total = total;
    }

    public String getOrderDetail_id() {
        return orderDetail_id;
    }

    public void setOrderDetail_id(String orderDetail_id) {
        this.orderDetail_id = orderDetail_id;
    }

    public String getMeal_code() {
        return meal_code;
    }

    public void setMeal_code(String meal_code) {
        this.meal_code = meal_code;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetailTM{" +
                "orderDetail_id='" + orderDetail_id + '\'' +
                ", meal_code='" + meal_code + '\'' +
                ", total=" + total +
                '}';
    }
}
