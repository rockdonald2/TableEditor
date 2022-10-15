package edu.gof.visitor.panel.tabel;

import edu.gof.visitor.service.export.ExportVisitor;

public class TextField extends Field {

    private String value;

    public TextField(String key, String value) {
        super(key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String accept(ExportVisitor visitor) {
        return visitor.visit(this);
    }

}
