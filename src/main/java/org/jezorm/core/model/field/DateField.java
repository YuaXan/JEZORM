package org.jezorm.core.model.field;

import java.util.Map;

public class DateField extends Field {
    public DateField(Map<String, Object> agrs) {
        super(agrs);
    }

    @Override
    public Boolean valueCheck() {
        return (this.value instanceof String) ? true : false;
    }

    @Override
    public String makeSQL() {

        return "DATE" + super.makeSQL() + ",\n";
    }
}
