package edu.gof.visitor.view.table;

import edu.gof.visitor.view.table.model.CustomTableModel;

import javax.swing.*;

public interface Table {

    JTable getComponent();

    void displayData(CustomTableModel tableModel);

    CustomTableModel constructModel(String[][] data, String[] headers);

}
