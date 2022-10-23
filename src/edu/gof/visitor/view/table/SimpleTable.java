package edu.gof.visitor.view.table;

import edu.gof.visitor.command.RemoveColumnCommand;
import edu.gof.visitor.command.RemoveRowCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.utils.Constants;
import edu.gof.visitor.view.table.model.CustomTableModel;
import edu.gof.visitor.view.table.model.SimpleTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class SimpleTable extends JTable implements Table {

    public SimpleTable() {
        this.setSize(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT);
        this.setRowHeight(24);
        this.setModel(this.constructModel(new String[][]{}, new String[]{"...", "..."}));
        this.setPreferredSize(new Dimension(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT));
        this.getTableHeader().setReorderingAllowed(false);

        addPopup();
        addEditor();
    }

    private void addEditor() {
        DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean isCellEditable(EventObject e) {
                if (e instanceof KeyEvent) {
                    return startWithKeyEvent((KeyEvent) e);
                }

                return super.isCellEditable(e);
            }

            private boolean startWithKeyEvent(KeyEvent e) {
                if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
                    return false;
                } else return (e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) == 0;
            }
        };

        this.setDefaultEditor(Object.class, editor);
    }

    private void addPopup() {
        final JPopupMenu popupMenu = new JPopupMenu();

        final JMenuItem deleteRowPopupItem = new JMenuItem("Delete Selected Row");
        deleteRowPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = SwingUtilities.convertPoint(deleteRowPopupItem, new Point(0, 0), SimpleTable.this);
                int rowAtPoint = SimpleTable.this.rowAtPoint(point);

                if (rowAtPoint > -1) {
                    MainController.instance().executeCommand(new RemoveRowCommand(MainController.instance(), new Position(rowAtPoint, 0)));
                }
            }
        });
        popupMenu.add(deleteRowPopupItem);

        final JMenuItem deleteColPopupItem = new JMenuItem("Delete Selected Column");
        deleteColPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = SwingUtilities.convertPoint(deleteColPopupItem, new Point(0, 0), SimpleTable.this);
                int colAtPoint = SimpleTable.this.columnAtPoint(point);

                if (colAtPoint > -1) {
                    MainController.instance().executeCommand(new RemoveColumnCommand(MainController.instance(), new Position(0, colAtPoint)));
                }
            }
        });
        popupMenu.add(deleteColPopupItem);

        this.setComponentPopupMenu(popupMenu);
    }

    @Override
    public JTable getComponent() {
        return this;
    }

    @Override
    public void displayData(CustomTableModel tableModel) {
        this.setModel(tableModel);
    }

    @Override
    public CustomTableModel constructModel(String[][] data, String[] headers) {
        return new SimpleTableModel(this, data, headers);
    }

}
