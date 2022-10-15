package edu.gof.visitor.service.export.json;

import edu.gof.visitor.panel.tabel.NumberField;
import edu.gof.visitor.panel.tabel.TextField;
import edu.gof.visitor.service.export.ExportVisitor;

public class JsonExportVisitor implements ExportVisitor {

    @Override
    public String visit(NumberField number) {
        return String.format("\"%s\": %s", number.getKey(), number.getValue());
    }

    @Override
    public String visit(TextField text) {
        return String.format("\"%s\": \"%s\"", text.getKey(), text.getValue());
    }

}
