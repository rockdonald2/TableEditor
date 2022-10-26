package edu.gof.visitor.view.table.decorator;

import edu.gof.visitor.model.Position;
import edu.gof.visitor.view.table.Table;

public abstract class ProtectedFieldTableDecorator extends ResettableTableDecorator {

    protected ProtectedFieldTableDecorator(Table table) {
        super(table);
    }

    public abstract Position getProtectedPosition();
}
