package org.jezorm.core.model.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.jezorm.core.controller.DataBase;
import org.jezorm.core.model.Model;
import org.jezorm.core.model.field.*;

public class User extends Model {

    public CharField account = new CharField(Map.of());
    public CharField password = new CharField(Map.of());
    public BooleanField superUser = new BooleanField(Map.of());

    public Boolean LoninValidator(String account, String password) {
        String getSQL = "SELECT * FROM " + getTableName() + " WHERE " + getTableName() + "_account=? AND "
                + getTableName() + "_password=?";
        try {
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(getSQL);
            dbStatement.setString(1, account);
            dbStatement.setString(2, password);
            ResultSet result = dbStatement.executeQuery();
            return result.next();

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

    }
}
