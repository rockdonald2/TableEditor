package edu.gof.visitor.service.export;

import edu.gof.visitor.panel.tabel.NumberField;
import edu.gof.visitor.panel.tabel.TextField;

public interface ExportVisitor {

    String visit(NumberField number);
    String visit(TextField text);

}
