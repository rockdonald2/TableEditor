package edu.gof.visitor.view.table;

import javax.swing.table.TableModel;
import java.awt.*;

public interface Table {

    Component getComponent();

    void displayData(TableModel tableModel);

    TableModel constructModel(String[][] data, String[] headers);

}
