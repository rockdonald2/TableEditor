package edu.gof.visitor.view.table.decorator;

import edu.gof.visitor.view.table.Table;

import javax.swing.table.TableModel;
import java.awt.*;

public abstract class TableDecorator implements Table {

    private final Table table;

    public TableDecorator(Table table) {
        this.table = table;
    }

    @Override
    public final Component getComponent() {
        return table.getComponent();
    }

    @Override
    public void displayData(TableModel tableModel) {
        table.displayData(tableModel);
    }

    @Override
    public TableModel constructModel(String[][] data, String[] headers) {
        return table.constructModel(data, headers);
    }
}
