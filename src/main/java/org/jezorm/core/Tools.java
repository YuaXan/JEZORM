package org.jezorm.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;

import org.jezorm.core.model.field.Field;

public class Tools {
    /**
     * 获取包含自身以及父类的{@code ArrayList}
     * 
     * @param clazz
     * @return {@code ArrayList<Class<?>>} 返回一个包含自身以及父类的{@code ArrayList}
     * @throws ClassNotFoundException
     */
    public static ArrayList<Class<?>> getInheritanceClasses(Class<?> clazz) {
        ArrayList<Class<?>> clazzList = new ArrayList<>();
        try {
            while (clazz.getSuperclass() != null) {
                if (clazz != Object.class) {
                    clazzList.add(Class.forName(clazz.getName()));
                    clazz = clazz.getSuperclass();
                }
            }
            Collections.reverse(clazzList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazzList;
    }

    public static HashSet<String> getModelFieldsSet(Class<?> clazz) {
        HashSet<String> fieldName = new HashSet<>();
        java.lang.reflect.Field[] fields;
        try {
            ArrayList<Class<?>> clazzList = getInheritanceClasses(clazz);
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);

            // 生成当前类以及父类所有字段名
            for (Class<?> cls : clazzList) {
                fields = cls.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    field.setAccessible(true);
                    Object fieldObject = field.get(clazzConstructor.newInstance());
                    if (!(fieldObject instanceof Field)) // 判断是否是 Field 类型
                        continue;
                    fieldName.add(field.getName());
                }
            }
            return fieldName;
        } catch (InvocationTargetException | SecurityException | NoSuchMethodException | ClassCastException
                | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
        }
        return fieldName;
    }

    public static HashSet<Object> getModelFields(Class<?> clazz) {
        HashSet<Object> fieldObjects = new HashSet<>();
        java.lang.reflect.Field[] fields;
        try {
            ArrayList<Class<?>> clazzList = getInheritanceClasses(clazz);
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);

            // 生成当前类以及父类所有字段名
            for (Class<?> cls : clazzList) {
                fields = cls.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    field.setAccessible(true);
                    Object fieldObject = field.get(clazzConstructor.newInstance());
                    if (!(fieldObject instanceof Field)) // 判断是否是 Field 类型
                        continue;
                    fieldObjects.add(fieldObject);
                }
            }
            return fieldObjects;
        } catch (InvocationTargetException | SecurityException | NoSuchMethodException | ClassCastException
                | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
        }
        return fieldObjects;
    }

    public static LinkedHashMap<String, Object> getModelFieldMap(Class<?> clazz) {
        LinkedHashMap<String, Object> fieldObjects = new LinkedHashMap<>();
        java.lang.reflect.Field[] fields;
        try {
            ArrayList<Class<?>> clazzList = getInheritanceClasses(clazz);
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);

            // 生成当前类以及父类所有字段名
            for (Class<?> cls : clazzList) {
                fields = cls.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldObject = field.get(clazzConstructor.newInstance());
                    if (!(fieldObject instanceof Field)) // 判断是否是 Field 类型
                        continue;
                    fieldObjects.put(fieldName, fieldObject);
                }
            }
            return fieldObjects;
        } catch (InvocationTargetException | SecurityException | NoSuchMethodException | ClassCastException
                | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
        }
        return fieldObjects;
    }

    public static LinkedHashMap<String, Object> getModelObjectFieldMap(Object object) {
        Class<?> clazz = object.getClass();
        LinkedHashMap<String, Object> fieldObjects = new LinkedHashMap<>();
        java.lang.reflect.Field[] fields;
        try {
            ArrayList<Class<?>> clazzList = getInheritanceClasses(clazz);
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);

            // 生成当前类以及父类所有字段名
            for (Class<?> cls : clazzList) {
                fields = cls.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldObject = field.get(object);
                    if (!(fieldObject instanceof Field)) // 判断是否是 Field 类型
                        continue;
                    fieldObjects.put(fieldName, fieldObject);
                }
            }
            return fieldObjects;
        } catch (SecurityException | NoSuchMethodException | ClassCastException | IllegalArgumentException
                | IllegalAccessException e) {
        }
        return fieldObjects;
    }

    public static Object getClassField(String name, Class<?> clazz) {
        java.lang.reflect.Field[] fields;
        try {
            Constructor<?> clazzConstructor = clazz.getDeclaredConstructor();
            clazzConstructor.setAccessible(true);
            fields = clazz.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldObject = field.get(clazzConstructor.newInstance());
                if (fieldName != name) // 判断是否是同名
                    continue;
                return fieldObject;
            }
        } catch (SecurityException | NoSuchMethodException | ClassCastException | IllegalArgumentException
                | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }
}