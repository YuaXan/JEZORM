package org.jezorm.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jezorm.core.controller.Setting;
import org.jezorm.core.controller.SettingSingleton;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ModelFeflection {
    /**
     * 所有Model类的子类
     */

    public List<Class<? extends Model>> modelLists = new ArrayList<Class<? extends Model>>();
    {
        Object settings = SettingSingleton.getSettings();
        String[] apps  =  ((Setting) settings).getinstallApp();
        for (String app :apps) {
            Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner(false))
                    .setUrls(ClasspathHelper.forPackage(((Setting) settings).getPorjectName())).filterInputsBy(
                            new FilterBuilder().includePackage(((Setting) settings).getPorjectName() + "." + app)));
            Set<Class<? extends Model>> classSet = reflections.getSubTypesOf(Model.class);
            modelLists.addAll(classSet);
        }
    }
}