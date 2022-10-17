package edu.gof.visitor.service.export;

import edu.gof.visitor.model.DecimalField;
import edu.gof.visitor.model.NumberField;
import edu.gof.visitor.model.TextField;

public interface ExportVisitor {

    String visit(NumberField number);

    String visit(TextField text);

    String visit(DecimalField decimal);

}
