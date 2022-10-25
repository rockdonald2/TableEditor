package edu.gof.visitor.model;

import edu.gof.visitor.service.export.ExportVisitor;

public abstract class Field {

    protected String key;

    protected Field(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public abstract String accept(ExportVisitor visitor);

}
