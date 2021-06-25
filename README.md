# JEZORM
`JEZORM's` full name is `Java Easy Object Relational Mapping`. The design concept from [Django](https://www.djangoproject.com/). Because the traditional way was to write `DAO` for every DataBase Table. The `JEZORM` does not to write `DAO` for every table, just like [Django](https://www.djangoproject.com/), you should write `data models` and then `JEZORM` will scan all `data models` to auto-create DataBase Table.

---

### Examples Porject Directories Overview
```
.
├── lib
|   └── jezorm-1.0.jar                   # JEZORM jar
└── src
    └── com
        └── studentsystem
            ├── App.java                 # main code
            ├── Settings.java            # porject settings
            └── student                  # App name
                └── StudentModel.java    # student infomtions models
```

---
## Quick start guide

### For Model Example
```java
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
        public CharField nation = new CharField(Map.of("verbose_name", "Nation"));
        public CharField nativePlace = new CharField(Map.of("verbose_name", "Native Place"));
        public DateField enrollmentDate = new DateField(Map.of("verbose_name", "Enrollment Date"));

    }

    public static class StudentInformation extends Model {
        public CharField email = new CharField(Map.of("verbose_name", "Email", "unique", true));
        public CharField phoneNum = new CharField(Map.of("verbose_name", "Phone Num.", "unique", true));
        public ForeignKeyField student = new ForeignKeyField(
                Map.of("model", Student.class, "on_delete", ForeignKeyField.ON_DELETE.CASCADE));
    }
}
```
At this point, we write 2 models about the student. After we should make a `Settings` Class, the `Settings` Class have some information for `JEZORM`.

---

### For Settings Example
```java
package com.studentsystem;

import org.jezorm.core.controller.Setting;

public class Settings extends Setting {
	public final String PORJECT_NAME = "com.studentsystem"; // your porject name
	public final String[] INSTALL_APP = { "student" }; // your App name 
	public final String DB_PATH = "studensystem.db"; // your DataBase Path

}
```
`JEZORM` temporary only support [SQLite3](https://sqlite.org/index.html), so just need the DataBase file name.

---

### For Main Example
```java
package com.studentsystem;

import org.jezorm.core.controller.DataBase;
import org.jezorm.core.controller.SettingSingleton;

public class App {
    public static void main(String[] args) throws Exception {
        
        SettingSingleton.setSettingsClass(Settings.class); // add the Setting.class 
        new DataBase(); // init DataBase
        DataBase.createDbTable(DataBase.getModelSQL()); // create all table for models
    }
}
```

---

### For CURD Example
We can write CURD examples in Main.
```java
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

```

---

## Field Type
>**Field(Map<String, Object> agrs)**
>
>All types of fields extend in this class, and you can require some arguments to field.
>
>>You can require following arguments.
>>```java
>>    /**
>>     * if true, Field will store empty values as null in the database.
>>     * Default is false.
>>     */
>>    Boolean none = false;
>>
>>    /**
>>     * If true, the field is allowed to be blank. Default is false.
>>     */
>>    Boolean blank = false;
>>
>>    /**
>>     * If false, the field will not be displayed. Default is true.
>>     */
>>    Boolean editable = true;
>>
>>    /**
>>     * If true, a database index will be created for this field.
>>     */
>>    Boolean db_index = false;
>>
>>    /**
>>     * If true, this field must be unique throughout the table.
>>     */
>>    Boolean unique = false;
>>
>>    /**
>>     * The default value for the field. This can be a value or a callable object. If
>>     * callable it will be called every time a new object is created.
>>     */
>>    Object filed_default = null;
>>
>>    /**
>>     * If true, this field is the primary key for the model.
>>     */
>>    Boolean primary_key = false;
>>
>>    /**
>>     * A human-readable name for the field.
>>     */
>>    String verbose_name = "";
>>```
>>

>**AutoField(Map<String, Object> agrs)**
>
>>You just only requires `verbose_name`.
>>
>>An IntField that automatically increments according to available IDs. You usually won’t need to use this directly; a primary key field will automatically be added to your model if you don’t specify otherwise.
>>
>>```java
>>public AutoField id = new AutoField(Map.of());
>>```
>>

>**IntField(Map<String, Object> agrs)**
>
>>An integer. You can requires all arguments.

>**CharField(Map<String, Object> agrs)**
>
>>An string. You can requires all arguments.

>**BooleanField(Map<String, Object> agrs)**
>
>>An boolean. You can requires all arguments.

>**DateField(Map<String, Object> agrs)**
>
>>An data. You can requires all arguments.
>>
>>`Warning: Not test whit Date Class or Calendar Class`

>**DateTimeField(Map<String, Object> agrs)**
>
>>An datatime. You can requires all arguments.
>>
>>`Warning: Not test whit Date Class or Calendar Class`

>**ForeignKeyField(Map<String, Object> agrs)**
>
>>A many-to-one relationship. Requires two necessary arguments: the class to which the model is related and the `ON_DELETE` option. And a optional arguments: if `ON_DELETE` choise `SET_DEFAULT`, need requires default value.
>>
>>```java
>>public ForeignKeyField field = new ForeignKeyField(
>>        Map.of("model", Student.class, "on_delete", ForeignKeyField.ON_DELETE.CASCADE));
>>```
>>
>
>>### Functions
>>```java
>>public ArrayList<? extends Model> getObject();
>>```
>>This function will get objects about the span relationship.

---

## License

Licensed under either of

- Apache License, Version 2.0 ([LICENSE-APACHE](LICENSE-APACHE))
- MIT license ([LICENSE-MIT](LICENSE-MIT))

at your option.

---

### Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted
for inclusion in the work by you, as defined in the Apache-2.0 license, shall be
dual licensed as above, without any additional terms or conditions.