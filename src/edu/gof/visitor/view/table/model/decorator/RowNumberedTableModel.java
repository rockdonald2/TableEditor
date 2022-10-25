package edu.gof.visitor.view.table.model.decorator;

import edu.gof.visitor.view.table.model.CustomTableModel;

public class RowNumberedTableModel extends TableModelDecorator {

    public RowNumberedTableModel(CustomTableModel tableModel) {
        super(tableModel);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return super.isCellEditable(rowIndex, columnIndex) && columnIndex != 0;
    }

}
