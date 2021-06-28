# JEZORM

`JEZORM` 的全称是 `Java Easy Object Relational Mapping`。 设计灵感来源于 [Django](https://www.djangoproject.com/)。 在之前的开发方式是为每一个数据表写一个`DAO`，但是 `JEZORM` 不需要为每一个表写 `DAO`，就像 [Django](https://www.djangoproject.com/), 你只需要写一份数据模型之后`JEZORM`会为你自动生成数据表。

---

[English](README.md) click it.

---

### 示例目录
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
## 快速开始教程

### 模型示例
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
我们写了两个关于学生数据模型，之后我们只需要在`Settings`类保存我们有关模型的信息。

---

### Settings 示例
```java
package com.studentsystem;

import org.jezorm.core.controller.Setting;

public class Settings extends Setting {
	public final String PORJECT_NAME = "com.studentsystem"; // 你的项目名
	public final String[] INSTALL_APP = { "student" }; // APP名
	public final String DB_PATH = "studensystem.db"; // 数据库地址

}
```
`JEZORM` 暂时只支持 [SQLite3](https://sqlite.org/index.html)，所以我们只需要数据库文件名即可。

---

### 程序入口示例
```java
package com.studentsystem;

import org.jezorm.core.controller.DataBase;
import org.jezorm.core.controller.SettingSingleton;

public class App {
    public static void main(String[] args) throws Exception {
        
        SettingSingleton.setSettingsClass(Settings.class); // 添加 Settings 类
        new DataBase(); // 初始化数据库模块
        DataBase.createDbTable(DataBase.getModelSQL()); // 创建数据表
    }
}
```

---

### 增删改查示例
我们可以将增删改查示例写入程序入口内。
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
        ArrayList<? extends Model> objList1 = stu.get("account", "yuaxan"); // 第一种方法
        ArrayList<? extends Model> objList2 = stu.get(Map.of("account", "yuaxan")); // 第二种方法
        StudentModel.Student obj1 = (StudentModel.Student) objList1.get(0);
        StudentModel.Student obj2 = (StudentModel.Student) objList2.get(0);
        System.out.println("Print first way first name:" + obj1.firstName.value);
        System.out.println("Print second way first name:" + obj2.firstName.value);

    }

    public static void delete(String id) {
        StudentModel.Student stu = new StudentModel.Student();

        // 第一种方法可以将数据删除
        System.out.println("Print first way whether save:" + stu.delete("account", "yuaxan"));

        // 第二种方法会失败，因为第一种方法已经删除了，但第二种方法也是有效的。
        System.out.println("Print second way whether save:" + stu.delete(Map.of("account", "yuaxan")));
    }
}

```

---

## 字段类型
>**Field(Map<String, Object> agrs)**
>
>所有字段类都是继承这个类，同时你可以传入一些参数
>
>>参数如下：
>>```java
>>    /**
>>     * 如果是true，该字段在数据库中值可以填null
>>     * 默认false
>>     */
>>    Boolean none = false;
>>
>>    /**
>>     * 如果是true，该字段可以不输入值，默认false
>>     */
>>    Boolean blank = false;
>>
>>    /**
>>     * 如果为false，该字段将不会显示，默认为true
>>     */
>>    Boolean editable = true;
>>
>>    /**
>>     *如果为true，将会为该字段在数据库创建索引
>>     */
>>    Boolean db_index = false;
>>
>>    /**
>>     * 如果为true，该字段将在数据表唯一
>>     */
>>    Boolean unique = false;
>>
>>    /**
>>     * 该字段的默认值，每次新创建数据时，字段为空则使用默认值
>>     */
>>    Object filed_default = null;
>>
>>    /**
>>     * 如果为true，该字段是数据表的主键
>>     */
>>    Boolean primary_key = false;
>>
>>    /**
>>     * 可读的字段名
>>     */
>>    String verbose_name = "";
>>```
>>

>**AutoField(Map<String, Object> agrs)**
>
>>你只能填写`verbose_name`.
>>
>>基于 IntField 的可自增的ID字段。您通常不需要直接使用此：如果您没有指定其他操作，则将自动将AutoField字段添加到模型中。
>>
>>```java
>>public AutoField id = new AutoField(Map.of());
>>```
>>

>**IntField(Map<String, Object> agrs)**
>
>>一个整数字段. 你可以传入所有参数。

>**CharField(Map<String, Object> agrs)**
>
>>一个字符串字段. 你可以传入所有参数。

>**BooleanField(Map<String, Object> agrs)**
>
>>一个布尔值字段. 你可以传入所有参数。

>**DateField(Map<String, Object> agrs)**
>
>>一个日期字段. 你可以传入所有参数。
>>
>>`警告: 没有使用 Date 类或者 Calendar 类进行测试`

>**DateTimeField(Map<String, Object> agrs)**
>
>>一个时间字段. 你可以传入所有参数。
>>
>>`警告: 没有使用 Date 类或者 Calendar 类进行测试`

>**ForeignKeyField(Map<String, Object> agrs)**
>
>>外键字段。 需要两个必要的参数：与模型相关的类和`ON_DELETE` 选项。 还有一个可选参数：如果`ON_DELETE`选择`SET_DEFAULT`，需要默认值。
>>
>>```java
>>public ForeignKeyField field = new ForeignKeyField(
>>        Map.of("model", Student.class, "on_delete", ForeignKeyField.ON_DELETE.CASCADE));
>>```
>>
>
>>### 方法
>>```java
>>public ArrayList<? extends Model> getObject();
>>```
>>这个方法可以获取父键的所有数据。

---

## License

许可证如下

- Apache License, Version 2.0 ([LICENSE-APACHE](LICENSE-APACHE))
- MIT license ([LICENSE-MIT](LICENSE-MIT))

随你选择。

---

### Contribution

如果您另有明确说明，否则根据 Apache-2.0 许可证的定义，包含在作品中的您提交的任何贡献均应获得上述双重许可，并且没有任何附加条款或条件。