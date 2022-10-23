package edu.gof.visitor.view.table.model.decorator;

import edu.gof.visitor.view.table.model.CustomTableModel;

public class RowNumberedTableModel extends TableModelDecorator {

    public RowNumberedTableModel(CustomTableModel tableModel) {
        super(tableModel);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex - 1);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return super.isCellEditable(rowIndex, columnIndex) && columnIndex != 0;
    }

}
