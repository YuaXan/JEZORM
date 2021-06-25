package org.jezorm.core.model.field;

import java.util.Map;

public class IntField extends Field {
    public IntField(Map<String, Object> agrs) {
        super(agrs);
    }

    @Override
    public Boolean valueCheck() {
        return (this.value instanceof Integer) ? true : false;

    }

    @Override
    public String makeSQL() {

        return "INTEGER" + super.makeSQL() + ",\n";
    }
}
