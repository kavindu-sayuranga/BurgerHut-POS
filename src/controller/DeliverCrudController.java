package controller;

import model.Delivery;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliverCrudController {

    public static ArrayList<String> getDelievryId() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT d_code FROM Delivery");
        ArrayList<String> ids = new ArrayList<>();
        while (result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }
    public static Delivery getDelivery(String id) throws SQLException, ClassNotFoundException {
        ResultSet result= CrudUtil.execute("SELECT * FROM Delivery WHERE d_code=?", id);
        if (result.next()){
            return new Delivery(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getDouble(6),
                    result.getString(7)
            );
        }
        return null;
    }

}
