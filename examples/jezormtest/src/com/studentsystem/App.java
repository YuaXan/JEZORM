package com.studentsystem;

import org.jezorm.core.controller.DataBase;
import org.jezorm.core.controller.SettingSingleton;
import org.jezorm.core.model.Model;

import java.util.ArrayList;
import java.util.Map;

import com.studentsystem.student.StudentModel;

public class App {
    public static void main(String[] args) throws Exception {

        SettingSingleton.setSettingsClass(Settings.class);
        new DataBase();
        DataBase.createDbTable(DataBase.getModelSQL());
        StudentModel.Student stu = create();
        update(stu.id.value);
        retrieve();
        delete(stu.id.value);
    }

    public static StudentModel.Student create() {
        StudentModel.Student stu = new StudentModel.Student();
        stu.account.value = "yuaxan";
        stu.password.value = "yuaxanpassword";
        stu.superUser.value = false;
        stu.firstName.value = "Yua";
        stu.lastName.value = "Xan";
        stu.enrollmentDate.value = "2021-01-01";
        System.out.println("Print stu whether save:" + stu.save());
        return stu;
    }

    public static void update(String id) {
        StudentModel.Student stu = new StudentModel.Student();
        System.out.println("Print stu whether updata:" + stu.update("id", id, Map.of("enrollmentDate", "2021-12-30")));
    }

    public static void retrieve() {
        StudentModel.Student stu = new StudentModel.Student();
        ArrayList<? extends Model> objList1 = stu.get("account", "yuaxan"); // first way
        ArrayList<? extends Model> objList2 = stu.get(Map.of("account", "yuaxan")); // second way
        StudentModel.Student obj1 = (StudentModel.Student) objList1.get(0);
        StudentModel.Student obj2 = (StudentModel.Student) objList2.get(0);
        System.out.println("Print first way first name:" + obj1.firstName.value);
        System.out.println("Print second way first name:" + obj2.firstName.value);

    }

    public static void delete(String id) {
        StudentModel.Student stu = new StudentModel.Student();

        // first way will successful delete
        System.out.println("Print first way whether save:" + stu.delete("account", "yuaxan"));

        // second way will fail, because first way delete data, but this way can delete
        // too
        System.out.println("Print second way whether save:" + stu.delete(Map.of("account", "yuaxan")));
    }
}
