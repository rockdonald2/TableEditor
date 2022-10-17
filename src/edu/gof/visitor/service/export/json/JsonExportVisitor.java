package edu.gof.visitor.service.export.json;

import edu.gof.visitor.model.DecimalField;
import edu.gof.visitor.model.NumberField;
import edu.gof.visitor.model.TextField;
import edu.gof.visitor.service.export.ExportVisitor;

import java.math.BigDecimal;

public class JsonExportVisitor implements ExportVisitor {

    @Override
    public String visit(NumberField number) {
        return String.format("\"%s\": %s", number.getKey(), number.getValue());
    }

    @Override
    public String visit(TextField text) {
        return String.format("\"%s\": \"%s\"", text.getKey(), text.getValue());
    }

    @Override
    public String visit(DecimalField decimal) {
        return String.format("\"%s\": %s", decimal.getKey(), BigDecimal.valueOf(decimal.getValue()).toPlainString());
    }

}
