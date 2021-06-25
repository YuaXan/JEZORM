package org.jezorm.core.model.field;

import java.util.Map;

public class AutoField extends Field {

    public AutoField(Map<String, Object> agrs) {
        super(Map.of("editable", false, "primary_key", true, "db_index", true));
        if (!agrs.isEmpty())
            verbose_name = (String) agrs.get("verbose_name");

    }

    @Override
    public Boolean valueCheck() {
        return (this.value instanceof Integer) ? true : false;

    }

    @Override
    public String makeSQL() {
        return "INTEGER PRIMARY KEY AUTOINCREMENT,\n";
    }
}
