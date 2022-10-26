package edu.gof.visitor.view.table.decorator;

import edu.gof.visitor.view.table.Table;

public abstract class ResettableTableDecorator extends TableDecorator {

    protected ResettableTableDecorator(Table table) {
        super(table);
    }

    public abstract void resetModel();

}
