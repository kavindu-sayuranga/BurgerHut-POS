package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Meal;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MealCrudController {

    public static ArrayList<String> getMealCode() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT code FROM meal");
        ArrayList<String> ids = new ArrayList<>();
        while (result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }
    public static Meal getMeal(String id) throws SQLException, ClassNotFoundException {
        ResultSet result= CrudUtil.execute("SELECT * FROM meal WHERE code=?", id);
        if (result.next()){
            return new Meal(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getDouble(4)
            );
        }
        return null;
    }

    public static ObservableList<String> getMealName() throws SQLException, ClassNotFoundException {

        ResultSet result =CrudUtil.execute("SELECT DISTINCT name  FROM meal");
        ObservableList<String> ptObList = FXCollections.observableArrayList();

        while (result.next()) {
            ptObList.add(result.getString(1));
        }
        return ptObList;
    }

}
