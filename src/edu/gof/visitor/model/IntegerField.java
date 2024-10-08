package edu.gof.visitor.model;

import edu.gof.visitor.service.export.ExportVisitor;

public class IntegerField extends Field {

    private int value;

    public IntegerField(String key, int value) {
        super(key);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String accept(ExportVisitor visitor) {
        return visitor.visit(this);
    }

}
