package views.tm;


import javafx.scene.control.Button;

public class IngredientTM {

    private String ingId;
    private String ingName;
    private String supId;
    private int qty;
    private double unitPrice;
    private Button btn;

    public IngredientTM() {
    }

    public IngredientTM(String ingId, String ingName, String supId, int qty, double unitPrice, Button btn) {
        this.ingId = ingId;
        this.ingName = ingName;
        this.supId = supId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "IngredientTM{" +
                "ingId='" + ingId + '\'' +
                ", ingName='" + ingName + '\'' +
                ", supId='" + supId + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", btn=" + btn +
                '}';
    }
}
