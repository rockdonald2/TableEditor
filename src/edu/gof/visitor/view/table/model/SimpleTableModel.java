package edu.gof.visitor.view.table.model;

import edu.gof.visitor.command.EditCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;

public class SimpleTableModel extends CustomTableModel {

    public SimpleTableModel(String[][] data, String[] headers) {
        super(data, headers);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        MainController.instance().executeCommand(new EditCommand(MainController.instance(), aValue.toString(), new Position(rowIndex, columnIndex)));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

}
