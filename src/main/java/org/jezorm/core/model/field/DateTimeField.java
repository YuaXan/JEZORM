package org.jezorm.core.model.field;

import java.util.Map;

public class DateTimeField extends Field{
    public DateTimeField(Map<String, Object> agrs) {
        super(agrs);
    }

    @Override
    public Boolean valueCheck() {
        return (this.value instanceof String) ? true : false;
    }

    @Override
    public String makeSQL() {

        return "DATETIME" + super.makeSQL() + ",\n";
    }
}
