package controller;

import db.DBConnection;
import model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserCrudController {

    public static boolean signupUser(User user) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Users` VALUES (?,?,?,?,?)");
        stm.setObject(1, user.getName());
        stm.setObject(2, user.getUserName());
        stm.setObject(3, user.getEmail());
        stm.setObject(4, user.getPassword());
        stm.setObject(5, user.getUserType());
        return stm.executeUpdate() > 0;
    }

}
