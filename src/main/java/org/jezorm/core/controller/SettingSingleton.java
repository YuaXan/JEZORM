package org.jezorm.core.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SettingSingleton {
    private volatile static Object settings;
    private static Class<?> settingsClass;

    public static Object getSettings() {
        if (settingsClass == null)
            return null;
        try {
            if (settings == null) {
                Constructor<?> clazzConstructor = settingsClass.getDeclaredConstructor();
                clazzConstructor.setAccessible(true);
                settings = clazzConstructor.newInstance();
                return settings;
            }
        } catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return settings;
    }

    public static void setSettingsClass(Class<?> settingsClass) {
        SettingSingleton.settingsClass = settingsClass;

    }

    public static Class<?> getSettingsClass() {
        if (settingsClass == null)
            return null;
        return settingsClass;
    }
}
