package com.studentsystem.student;

import java.util.Map;

import org.jezorm.core.model.field.*;
import org.jezorm.core.model.Model;
import org.jezorm.core.model.user.User;

public class StudentModel {
    public static class Student extends User {
        public CharField account = new CharField(Map.of("verbose_name", "Account", "unique", true));
        public CharField firstName = new CharField(Map.of("verbose_name", "First Name"));
        public CharField lastName = new CharField(Map.of("verbose_name", "Last Name"));
        public BooleanField sex = new BooleanField(Map.of("verbose_name", "Sex"));
        public DateField enrollmentDate = new DateField(Map.of("verbose_name", "Enrollment Date"));

    }

    public static class StudentInformation extends Model {
        public CharField email = new CharField(Map.of("verbose_name", "Email", "unique", true));
        public CharField phoneNum = new CharField(Map.of("verbose_name", "Phone Num.", "unique", true));
        public ForeignKeyField student = new ForeignKeyField(
                Map.of("model", Student.class, "on_delete", ForeignKeyField.ON_DELETE.CASCADE));
    }
}