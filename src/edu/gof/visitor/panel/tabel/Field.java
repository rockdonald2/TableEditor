package edu.gof.visitor.panel.tabel;

import edu.gof.visitor.service.export.ExportVisitor;

public abstract class Field {

    public Field(String key) {
        this.key = key;
    }

    protected String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public abstract String accept(ExportVisitor visitor);

}
