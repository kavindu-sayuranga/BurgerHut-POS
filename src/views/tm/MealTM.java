package views.tm;

import javafx.scene.control.Button;

public class MealTM {

    private String meal_code;
    private String meal_name;
    private int meal_qty;
    private double meal_price;
    private Button remove;

    public MealTM(String meal_code, String meal_name, int meal_qty, double meal_price, Button remove) {
        this.meal_code = meal_code;
        this.meal_name = meal_name;
        this.meal_qty = meal_qty;
        this.meal_price = meal_price;
        this.remove = remove;
    }

    public MealTM() {
    }


    public String getMeal_code() {
        return meal_code;
    }

    public void setMeal_code(String meal_code) {
        this.meal_code = meal_code;
    }

    public String getMeal_name() {
        return meal_name;
    }

    public void setMeal_name(String meal_name) {
        this.meal_name = meal_name;
    }

    public int getMeal_qty() {
        return meal_qty;
    }

    public void setMeal_qty(int meal_qty) {
        this.meal_qty = meal_qty;
    }

    public double getMeal_price() {
        return meal_price;
    }

    public void setMeal_price(double meal_price) {
        this.meal_price = meal_price;
    }

    public Button getRemove() {
        return remove;
    }

    public void setRemove(Button remove) {
        this.remove = remove;
    }


    @Override
    public String toString() {
        return "MealTM{" +
                "meal_code='" + meal_code + '\'' +
                ", meal_name='" + meal_name + '\'' +
                ", meal_qty=" + meal_qty +
                ", meal_price=" + meal_price +
                ", remove=" + remove +
                '}';
    }
}
