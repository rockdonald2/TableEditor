package edu.gof.visitor.view;

import edu.gof.visitor.command.EditCommand;
import edu.gof.visitor.command.RemoveColumnCommand;
import edu.gof.visitor.command.RemoveRowCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class Table extends JTable {

    public Table() {
        this.setSize(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT);
        this.setRowHeight(24);
        this.setModel(new DefaultTableModel(new String[][]{}, new String[]{"...", "..."}));

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
                } else if ((e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) != 0) {
                    return false;
                }

                return true;
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
                Point point = SwingUtilities.convertPoint(deleteRowPopupItem, new Point(0, 0), Table.this);
                int rowAtPoint = Table.this.rowAtPoint(point);

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
                Point point = SwingUtilities.convertPoint(deleteColPopupItem, new Point(0, 0), Table.this);
                int colAtPoint = Table.this.columnAtPoint(point);

                if (colAtPoint > -1) {
                    MainController.instance().executeCommand(new RemoveColumnCommand(MainController.instance(), new Position(0, colAtPoint)));
                }
            }
        });
        popupMenu.add(deleteColPopupItem);

        this.setComponentPopupMenu(popupMenu);
    }

    public void displayData(String[][] data, String[] headers) {
        this.setModel(new DefaultTableModel(data, headers) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                MainController.instance().executeCommand(new EditCommand(MainController.instance(), Table.this, aValue.toString(), new Position(row, column)));
            }
        });
    }

}
