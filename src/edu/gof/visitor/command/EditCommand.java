package edu.gof.visitor.command;

import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EditCommand extends Command<Void, Void> {

    private final Position editPosition;
    private final JTable table;
    private final String prevValue;
    private final String editValue;

    public EditCommand(MainController mainController, JTable table, String editValue, Position position) {
        super(mainController);
        this.editValue = editValue;
        this.editPosition = position;
        this.table = table;
        this.prevValue = mainController.getValueAt(position);
    }

    @Override
    public Void execute() {
        doEdit(editValue);
        return null;
    }

    @Override
    public Void unexecute() {
        doEdit(prevValue);
        return null;
    }

    private void doEdit(String value) {
        mainController.doModifyValueAt(editPosition.getRow(), editPosition.getColumn(), value);

        DefaultTableModel tableModel = ((DefaultTableModel) table.getModel());
        var rowVector = tableModel.getDataVector().elementAt(editPosition.getRow());
        rowVector.setElementAt(value, editPosition.getColumn());
        tableModel.fireTableCellUpdated(editPosition.getRow(), editPosition.getColumn());
    }

}
