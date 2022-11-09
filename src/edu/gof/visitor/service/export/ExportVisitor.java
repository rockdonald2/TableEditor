package edu.gof.visitor.service.export;

import edu.gof.visitor.model.DecimalField;
import edu.gof.visitor.model.IntegerField;
import edu.gof.visitor.model.TextField;

public interface ExportVisitor {

    String visit(IntegerField number);

    String visit(TextField text);

    String visit(DecimalField decimal);

}
