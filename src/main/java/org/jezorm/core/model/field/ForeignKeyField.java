package org.jezorm.core.model.field;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.jezorm.core.controller.DataBase;
import org.jezorm.core.model.Model;

@SuppressWarnings("unchecked")
public class ForeignKeyField extends Field {

    public Class<? extends Model> clazz;
    public ON_DELETE onDelete;
    public Integer foreinkeyDefault;

    public ForeignKeyField(Map<String, Object> agrs) {
        super(agrs);
        clazz = (Class<? extends Model>) agrs.get("model");
        onDelete = (ON_DELETE) agrs.get("on_delete");
        if (onDelete == ON_DELETE.SET_DEFAULT)
            foreinkeyDefault = (Integer) agrs.get("default");
    }

    public static enum ON_DELETE {
        CASCADE, PROTECT, SET_NULL, SET_DEFAULT, DO_NOTHING;

        public String on_delete = " ON DELETE " + this.toString() + " ON UPDATE " + this.toString() + ",\n";

        public String makeSQL() {
            return on_delete;
        }
    }

    public ArrayList<? extends Model> getObject() {
        if (this.value == null)
            return null;
        try {
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            Class<Model> modelClazz = (Class<Model>) Class.forName(Model.class.getName());
            Method method = modelClazz.getDeclaredMethod("getTableName");
            method.setAccessible(true);
            String tableName = (String) method.invoke(clazzConstructor.newInstance());
            String getSQL = "SELECT * FROM " + tableName + " WHERE " + tableName + "_id" + "=?";
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(getSQL);
            dbStatement.setObject(1, this.value);
            ResultSet dataSet = dbStatement.executeQuery();
            return ((Model) (clazzConstructor.newInstance())).makeObject(clazz, dataSet);

        } catch (SQLException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException
                | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return null;

    }

    @Override
    public Boolean valueCheck() {
        return (this.value instanceof String) ? true : false;
    }

    @Override
    public String makeSQL() {
        String sql = new String();
        if (onDelete == ON_DELETE.SET_DEFAULT)
            sql = " DEFAULT " + foreinkeyDefault;

        try {
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);
            Class<Model> modelClazz = (Class<Model>) Class.forName(Model.class.getName());
            Method method = modelClazz.getDeclaredMethod("getTableName");
            method.setAccessible(true);
            String tableName = (String) method.invoke(clazzConstructor.newInstance());
            return "INTEGER" + sql + " REFERENCES " + tableName + " (" + tableName + "_id" + ") " + onDelete.makeSQL();

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
        }

        return null;
    }
}
