package org.jezorm.core.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jezorm.core.Tools;
import org.jezorm.core.controller.DataBase;
import org.jezorm.core.controller.Setting;
import org.jezorm.core.controller.SettingSingleton;
import org.jezorm.core.model.field.*;


@SuppressWarnings("unchecked")
public class Model {

    public AutoField id = new AutoField(Map.of());

    public static String sql = "CREATE TABLE IF NOT EXISTS ";

    public ArrayList<? extends Model> get(String key, Object value) {
        Class<? extends Model> clazz = this.getClass();
        String getSQL = "SELECT * FROM " + getTableName() + " WHERE " + getTableName() + "_" + key.toLowerCase() + "=?";
        try {
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(getSQL);
            dbStatement.setObject(1, value);
            ResultSet dataSet = dbStatement.executeQuery();
            return makeObject(clazz, dataSet);

        } catch (SQLException | IllegalArgumentException | IllegalAccessException | InstantiationException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public ArrayList<? extends Model> get(Map<String, Object> agrs) {
        Class<? extends Model> clazz = this.getClass();
        Set<Entry<String, Object>> agrsNameSet = agrs.entrySet();
        Iterator<Entry<String, Object>> agrsIterator = agrsNameSet.iterator();
        String getSQL = "SELECT * FROM " + getTableName() + " WHERE ";
        ArrayList<Object> value = new ArrayList<>();

        while (agrsIterator.hasNext()) {
            String key = agrsIterator.next().getKey();
            value.add(agrsIterator.next().getValue());
            getSQL += key + "=?";
            if (agrsIterator.hasNext())
                getSQL += " AND ";
        }
        try {
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(getSQL);
            for (int i = 1; i > value.size() + 1; i++) {
                dbStatement.setObject(i, value.get(i - 1));
            }
            ResultSet dataSet = dbStatement.executeQuery();

            return makeObject(clazz, dataSet);
        } catch (SQLException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException
                | InstantiationException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<? extends Model> all() {
        Class<? extends Model> clazz = this.getClass();
        String getSQL = "SELECT * FROM " + getTableName();
        try {
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(getSQL);
            ResultSet dataSet = dbStatement.executeQuery();
            return makeObject(clazz, dataSet);

        } catch (SQLException | IllegalArgumentException | IllegalAccessException | InstantiationException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public Boolean save() {
        String sql = "INSERT INTO " + getTableName() + " (";
        String sqlValue = "";
        Class<? extends Model> clazz = this.getClass();
        LinkedHashMap<String, Object> fieldMap = Tools.getModelObjectFieldMap(this);
        Set<String> fieldSet = fieldMap.keySet();
        Iterator<String> fieldIterator = fieldSet.iterator();
        while (fieldIterator.hasNext()) {
            String fieldName = fieldIterator.next();
            sql += getTableName() + "_" + fieldName + ", ";
            sqlValue += "?, ";
            Field field = (Field) fieldMap.get(fieldName);
            if (field.value == null && !field.blank && !(field instanceof AutoField))
                return false;
        }
        sql = sql.substring(0, sql.lastIndexOf(",")) + ") VALUES (" + sqlValue.substring(0, sqlValue.lastIndexOf(","))
                + ");";

        fieldIterator = fieldSet.iterator();
        try {
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(sql);
            int i = 1;
            while (fieldIterator.hasNext()) {
                Field field = (Field) fieldMap.get(fieldIterator.next());
                dbStatement.setObject(i, field.value);
                i++;
            }
            i = dbStatement.executeUpdate();
            return i != 0;
        } catch (SQLException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String key, Object value) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getTableName() + "_" + key + "=?";
        try {
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(sql);
            dbStatement.setObject(1, value);
            int i = dbStatement.executeUpdate();
            return i != 0;
        } catch (SQLException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String key, Set<Object> valueSet) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getTableName() + "_" + key + " IN(";
        Iterator<Object> valueIterator = valueSet.iterator();
        while (valueIterator.hasNext()) {
            sql += valueIterator.next() + ", ";
        }
        sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
        try {
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(sql);
            int i = dbStatement.executeUpdate();
            return i != 0;
        } catch (SQLException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(String key, Object value, Map<String, Object> fieldMap) {
        String getSQl = " WHERE " + getTableName() + "_" + key.toLowerCase() + "=" + value;
        String sql = "UPDATE " + getTableName() + " SET ";
        Set<String> fieldSet = fieldMap.keySet();
        Iterator<String> fieldIterator = fieldSet.iterator();
        ArrayList<String> fieldList = new ArrayList<>();
        while (fieldIterator.hasNext()) {
            String fieldName = fieldIterator.next();
            fieldList.add(fieldName);
            sql += getTableName() + "_" + fieldName.toLowerCase() + "=?, ";
        }
        sql = sql.substring(0, sql.lastIndexOf(",")) + getSQl;
        try {
            PreparedStatement dbStatement = DataBase.connection.prepareStatement(sql);
            int i = 1;
            for (String name : fieldList) {
                dbStatement.setObject(i, fieldMap.get(name));
                i++;
            }
            i = dbStatement.executeUpdate();
            return i != 0;
        } catch (SQLException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer count() {
        String sql = "SELECT COUNT(*) FROM " + getTableName();
        try {
            PreparedStatement statement = DataBase.connection.prepareStatement(sql);
            ResultSet dataSet;

            dataSet = statement.executeQuery();

            dataSet.next();

            return (Integer) dataSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成拥有数据库所有返回结果的数据模型，并添加到 {@code ArrayList}
     * 
     * @param <K>     {@code <K>} 继承{@code Field} 类与 {@code InterFaceField} 的泛型
     * @param <T>     {@code <T>} 继承{@code Model} 类泛型
     * @param clazz   模型类
     * @param dataSet 数据库返回的{@code ResultSet}
     * @return {@code ArrayList<? extends Model>} 返回拥有数据库所有返回结果的数据模型
     *         {@code ArrayList}
     * @throws SQLException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    public <K extends Field & InterFaceField, T extends Model> ArrayList<T> makeObject(Class<?> clazz, ResultSet dataSet)
            throws SQLException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException,
            InstantiationException {

        HashSet<String> fieldName = new HashSet<>();
        ArrayList<Map<String, Object>> valueList = new ArrayList<>();
        ArrayList<T> classes = new ArrayList<T>();
        ArrayList<Class<?>> clazzList = Tools.getInheritanceClasses(clazz);
        java.lang.reflect.Field[] fields;

        try {
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);

            // 生成当前类以及父类所有字段名
            fieldName = Tools.getModelFieldsSet(clazz);

            // 处理JDBC传回的值

            while (dataSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (String name : fieldName) {
                    map.put(name, dataSet.getObject(getTableName() + "_" + name));
                }
                valueList.add(map);
            }

            // 将字段值填入模型类属性中的Field.value中，之后返回所有结果的模型类
            for (int i = 0; i < valueList.size(); i++) {
                Object object = clazzConstructor.newInstance();
                for (Class<?> cls : clazzList) {
                    fields = cls.getDeclaredFields();
                    for (java.lang.reflect.Field field : fields) {
                        field.setAccessible(true);
                        Object fieldObject = field.get(object);
                        if (!(fieldObject instanceof Field))
                            continue;
                        ((K) fieldObject).setValue(valueList.get(i).get(field.getName()));
                        field.set(object, fieldObject);
                    }
                }
                classes.add((T) object);
            }
            return classes;
        } catch (InvocationTargetException | SecurityException | NoSuchMethodException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getTableName() {
        Object settings = SettingSingleton.getSettings();
        Class<? extends Model> clazz = this.getClass();
        return clazz.getPackageName().substring(((Setting)settings).getPorjectName().length() + 1).replace(".", "_") + "_"
                + clazz.getSimpleName().toLowerCase();
    }

}
