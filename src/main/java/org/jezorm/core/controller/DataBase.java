package org.jezorm.core.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jezorm.core.Tools;
import org.jezorm.core.model.*;
import org.jezorm.core.model.field.*;

// TODO 检查数据库中，所有表的表结构与模型是否匹配
// TODO 未能实现索引创建

public class DataBase {
    /**
     * 可能有数据库连接或null
     */

    public static Connection connection = null;

    static {
        Object settings = SettingSingleton.getSettings();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + ((Setting) settings).getDbPath());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 所有继承Model的子类
     */
    public static ModelFeflection mFeflection = new ModelFeflection();

    /**
     * 获得所有继承Model的子类SQL语句
     * 
     * @return Map<String, String> 返回 Map(key:模型类名, value:SQL)
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public static <K extends Field & InterFaceField> Map<String, String> getModelSQL()
            throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, InstantiationException {

        Map<String, String> sqlMap = new HashMap<>(); // 保存所有模型的SQL Map
        for (Class<? extends Model> model : mFeflection.modelLists) {
            if (model.getName() == "org.jezorm.core.model.user.User")
                continue;
            Map<String, String> sqlItemMap = new LinkedHashMap<>(); // 单表字段SQL Map
            String sqls = new String(); // 保存SQL语句
            Class<?> clazz = Class.forName(model.getName()); // 加载model.class
            ArrayList<Class<?>> clazzList = Tools.getInheritanceClasses(clazz); // 保存包含自身以及父类的ArrayList

            // 使用Constructor 构造可 newInstance()的类
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);

            // 通过反射获取 Model类中 getTableName(),返回该类的tablename
            Class<Model> modelClazz = (Class<Model>) Class.forName(Model.class.getName());
            Method method = modelClazz.getDeclaredMethod("getTableName");
            method.setAccessible(true);
            String tableName = (String) method.invoke(clazzConstructor.newInstance());

            // 生成当前类的SQL语句
            for (Class<?> cls : clazzList) {
                java.lang.reflect.Field[] fields = cls.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    field.setAccessible(true);
                    Object fieldObject = field.get(clazzConstructor.newInstance());
                    if (!(fieldObject instanceof Field)) // 判断是否是 Field 类型
                        continue;
                    String name = field.getName().toLowerCase();
                    String fieldSQL = (((K) fieldObject).makeSQL());
                    sqlItemMap.merge(name, " " + tableName + "_" + name + " " + fieldSQL,
                            (oldValue, newValue) -> newValue);
                }
            }

            // 组合模型SQL语句
            Set<String> fieldNameSet = sqlItemMap.keySet();
            Iterator<String> fieldIteraor = fieldNameSet.iterator();
            while (fieldIteraor.hasNext()) {
                sqls += sqlItemMap.get(fieldIteraor.next());
            }
            sqlMap.put(model.getName(), makeModelSQL(tableName, sqls));

        }
        return sqlMap;
    }

    /**
     * 封装SQL CREATE 语句
     * 
     * @param tableName 模型类名
     * @param sqls      SQL语句
     * @return String 返回完整的SQL语句
     */
    static String makeModelSQL(String tableName, String sqls) {
        sqls = Model.sql + tableName + " (\n" + sqls;
        return sqls.substring(0, sqls.lastIndexOf(",")) + "\n);";
    }

    public static void cheackDbTables() {
        // TODO 没时间写，以后再写
    }

    /**
     * 创建数据表
     * 
     * @param sqlMap SQL语句
     */
    public static void createDbTable(Map<String, String> sqlMap) {
        Set<String> sqlKey = sqlMap.keySet();
        Iterator<String> sqlIterator = sqlKey.iterator();
        try {
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            while (sqlIterator.hasNext()) {
                statement.addBatch(sqlMap.get(sqlIterator.next()));
            }
            int[] result = statement.executeBatch();
            connection.commit();
            statement.close();

            // TODO 数据表创建成功后所需要的操作
            System.out.println(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
