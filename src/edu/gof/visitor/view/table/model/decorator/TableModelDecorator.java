package edu.gof.visitor.view.table.model.decorator;

import edu.gof.visitor.view.table.model.CustomTableModel;

import javax.swing.event.TableModelListener;

public abstract class TableModelDecorator extends CustomTableModel {

    private final CustomTableModel tableModel;

    protected TableModelDecorator(CustomTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.tableModel.setValueAt(aValue, rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.tableModel.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public final int getRowCount() {
        return super.getRowCount();
    }

    @Override
    public final int getColumnCount() {
        return super.getColumnCount();
    }

    @Override
    public final String getColumnName(int columnIndex) {
        return super.getColumnName(columnIndex);
    }

    @Override
    public final Class<?> getColumnClass(int columnIndex) {
        return super.getColumnClass(columnIndex);
    }

    @Override
    public final Object getValueAt(int rowIndex, int columnIndex) {
        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public final void addTableModelListener(TableModelListener l) {
        super.addTableModelListener(l);
    }

    @Override
    public final void removeTableModelListener(TableModelListener l) {
        super.removeTableModelListener(l);
    }

}
