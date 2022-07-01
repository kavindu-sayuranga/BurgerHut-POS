package controller;

import model.Employee;
import model.Ingredient;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IngredientCrudController {

    public static ArrayList<String> getIngId() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT ing_id FROM ingredient");
        ArrayList<String> ids = new ArrayList<>();
        while (result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }
    public static Ingredient getIngredient(String id) throws SQLException, ClassNotFoundException {
        ResultSet result= CrudUtil.execute("SELECT * FROM ingredient WHERE ing_id=?", id);
        if (result.next()){
            return new Ingredient(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getDouble(5)
            );
        }
        return null;
    }
}
