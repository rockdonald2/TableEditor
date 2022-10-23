package edu.gof.visitor.view.table.model;

import edu.gof.visitor.command.EditCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;

import javax.swing.*;

public class SimpleTableModel extends CustomTableModel {

    private final JTable table;

    public SimpleTableModel(JTable table, String[][] data, String[] headers) {
        super(data, headers);
        this.table = table;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        MainController.instance().executeCommand(new EditCommand(MainController.instance(), this.table, aValue.toString(), new Position(rowIndex, columnIndex)));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

}
