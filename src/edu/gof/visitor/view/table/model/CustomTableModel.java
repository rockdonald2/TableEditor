package edu.gof.visitor.view.table.model;

import javax.swing.table.DefaultTableModel;

public abstract class CustomTableModel extends DefaultTableModel {

    protected CustomTableModel() {
    }

    protected CustomTableModel(String[][] data, String[] headers) {
        super(data, headers);
    }

    @Override
    public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex);

    @Override
    public abstract boolean isCellEditable(int rowIndex, int columnIndex);

}
