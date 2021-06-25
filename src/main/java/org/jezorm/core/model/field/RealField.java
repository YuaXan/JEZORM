package org.jezorm.core.model.field;

import java.util.Map;

public class RealField extends Field {
    public RealField(Map<String, Object> agrs) {
        super(agrs);
    }

    @Override
    public Boolean valueCheck() {

        return (this.value instanceof Float || this.value instanceof Double) ? true : false;

    }

    @Override
    public String makeSQL() {

        return "REAL" + super.makeSQL() + ",\n";
    }
}
