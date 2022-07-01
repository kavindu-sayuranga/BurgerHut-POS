package views.tm;

public class DeliveryTM {
    private String deliveryId;
    private String driverId;
    private String orderId;
    private String date;
    private String time;
    private double deliveryPrice;
    private String status;

    public DeliveryTM() {
    }

    public DeliveryTM(String deliveryId, String driverId, String orderId, String date, String time, double deliveryPrice, String status) {
        this.deliveryId = deliveryId;
        this.driverId = driverId;
        this.orderId = orderId;
        this.date = date;
        this.time = time;
        this.deliveryPrice = deliveryPrice;
        this.status = status;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DeliveryTM{" +
                "deliveryId='" + deliveryId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", deliveryPrice=" + deliveryPrice +
                ", status='" + status + '\'' +
                '}';
    }
}
