package org.jezorm.core.model.field;

import java.util.Map;

public class BooleanField extends Field{

    public BooleanField(Map<String, Object> agrs) {
        super(agrs);
    }

    @Override
    public Boolean valueCheck() {
        return (this.value instanceof Boolean) ? true : false;
    }

    @Override
    public String makeSQL() {

        return "BOOLEAN" + super.makeSQL() + ",\n";
    }
}
