package edu.gof.visitor.view.table.decorator;

import edu.gof.visitor.view.table.Table;
import edu.gof.visitor.view.table.model.CustomTableModel;

import javax.swing.*;

public abstract class TableDecorator implements Table {

    private final Table table;

    protected TableDecorator(Table table) {
        this.table = table;
    }

    @Override
    public final JTable getComponent() {
        return table.getComponent();
    }

    @Override
    public void displayData(CustomTableModel tableModel) {
        table.displayData(tableModel);
    }

    @Override
    public CustomTableModel constructModel(String[][] data, String[] headers) {
        return table.constructModel(data, headers);
    }

}
