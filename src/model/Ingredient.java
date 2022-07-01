package model;

public class Ingredient {
    private String ingId;
    private String ingName;
    private String supId;
    private int qty;
    private double unitPrice;

    public Ingredient() {
    }

    public Ingredient(String ingId, String ingName, String supId, int qty, double unitPrice) {
        this.ingId = ingId;
        this.ingName = ingName;
        this.supId = supId;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getIngId() {
        return ingId;
    }

    public void setIngId(String ingId) {
        this.ingId = ingId;
    }

    public String getIngName() {
        return ingName;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingId='" + ingId + '\'' +
                ", ingName='" + ingName + '\'' +
                ", supId='" + supId + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
