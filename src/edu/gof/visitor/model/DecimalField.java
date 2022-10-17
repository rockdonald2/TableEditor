package edu.gof.visitor.model;

import edu.gof.visitor.service.export.ExportVisitor;

public class DecimalField extends Field {

    private double value;

    public DecimalField(String key, double value) {
        super(key);
        this.value = value;
    }

    public double getValue() {
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
