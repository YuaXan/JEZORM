package org.jezorm.core.model.field;

import java.lang.reflect.Type;

import java.util.Map;

public class Field implements InterFaceField {
    /**
     * value of field
     */
    public Object value;

    /**
     * if true, Field will store empty values as null in the database. Default is
     * false.
     */
    public Boolean none = false;

    /**
     * If true, the field is allowed to be blank. Default is false.
     */
    public Boolean blank = false;

    /**
     * If false, the field will not be displayed. Default is true.
     */
    public Boolean editable = true;

    /**
     * If true, a database index will be created for this field.
     */
    public Boolean db_index = false;

    /**
     * If true, this field must be unique throughout the table.
     */
    public Boolean unique = false;

    /**
     * The default value for the field. This can be a value or a callable object. If
     * callable it will be called every time a new object is created.
     */
    public Object filed_default = null;

    /**
     * If true, this field is the primary key for the model.
     */
    public Boolean primary_key = false;

    /**
     * If True, this field is the primary key for the model.
     */
    public String verbose_name = "";

    public Field(Map<String, Object> agrs) {
        // TODO 返回Result 等待前端弹出
        if (!agrs.isEmpty())
            agrs_handle(agrs);
    }

    void setField(String name, Object value) {
        switch (name) {
            case "none":
                this.none = (Boolean) value;
                break;
            case "blank":
                this.blank = (Boolean) value;
                break;
            case "editable":
                this.editable = (Boolean) value;
                break;
            case "db_index":
                this.db_index = (Boolean) value;
                break;
            case "unique":
                this.unique = (Boolean) value;
                break;
            case "filed_default":
                this.filed_default = value;
                break;
            case "primary_key":
                this.primary_key = (Boolean) value;
                break;
            case "verbose_name":
                this.verbose_name = (String) value;
                break;
            default:
                break;
        }
    }

    void agrs_handle(Map<String, Object> agrs) {
        Class<? extends Field> object = Field.class;
        java.lang.reflect.Field[] fields = object.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Type type = fields[i].getGenericType();
            String name = fields[i].getName();
            Object value = agrs.get(name);
            if (value == null)
                continue;
            if (value.getClass() != type && type != Object.class) {
                try {
                    throw new FieldTypeException();
                } catch (FieldTypeException e) {
                    e.printStackTrace();
                }
            }
            setField(name, value);
        }
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public Boolean valueCheck() {
        return null;
    }

    @Override
    public String makeSQL() {
        String sql = "";
        if (primary_key)
            sql += " PRIMARY KEY";
        if (none == false)
            sql += " NOT NULL";
        if (unique)
            sql += " UNIQUE";
        if (filed_default != null) {
            if (filed_default instanceof String)
                sql += " DEFAILT " + "'" + value + "'";
            if (filed_default instanceof Integer || filed_default instanceof Float || filed_default instanceof Double)
                sql += " DEFAILT " + value;
            if (filed_default instanceof Boolean)
                sql += " DEFAILT " + ((boolean) value ? 1 : 0);

        }
        return sql;
    }
}

class FieldTypeException extends Exception {
    public FieldTypeException() {
        super("Error: Incoming Argument Exception");
    }
}