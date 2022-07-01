package model;

public class Meal {

    private String mCode;
    private String mName;
    private int mQty;
    private double mPrice;

    public Meal() {
    }

    public Meal(String mCode, String mName, int mQty,double mPrice ) {
        this.mCode = mCode;
        this.mName = mName;
        this.mQty = mQty;
        this.mPrice = mPrice;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmQty() {
        return mQty;
    }

    public void setmQty(int mQty) {
        this.mQty = mQty;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }


    @Override
    public String toString() {
        return "Meal{" +
                "mCode='" + mCode + '\'' +
                ", mName='" + mName + '\'' +
                ", mQty=" + mQty +
                ", mPrice=" + mPrice +
                '}';
    }
}
