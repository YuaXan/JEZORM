package org.jezorm.core.model.field;

import java.util.Map;

public class CharField extends Field {

    public CharField(Map<String, Object> agrs) {
        super(agrs);
    }

    @Override
    public Boolean valueCheck() {
        return (this.value instanceof String) ? true : false;
    }

    @Override
    public String makeSQL() {

        return "VARCHAR" + super.makeSQL() + ",\n";
    }
}
