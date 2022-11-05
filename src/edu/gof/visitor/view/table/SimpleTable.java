package edu.gof.visitor.view.table;

import edu.gof.visitor.command.RemoveColumnCommand;
import edu.gof.visitor.command.RemoveRowCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.utils.Constants;
import edu.gof.visitor.view.MainPanel;
import edu.gof.visitor.view.diagrams.BarChartStrategy;
import edu.gof.visitor.view.diagrams.PieChartStrategy;
import edu.gof.visitor.view.table.decorator.ProtectedFieldTableDecorator;
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
                if (e instanceof KeyEvent keyEvent) {
                    return startWithKeyEvent(keyEvent);
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
                int rowAtPoint = getRowAtPoint(popupMenu);

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
                int colAtPoint = getColumnAtPoint(popupMenu);

                if (colAtPoint == -1) {
                    return;
                }

                if (isProtectedColumn(colAtPoint)) {
                    MainPanel.instance().showError("Cannot delete protected column");
                    return;
                }

                MainController.instance().executeCommand(new RemoveColumnCommand(MainController.instance(), new Position(0, colAtPoint)));
            }
        });
        popupMenu.add(deleteColPopupItem);

        final JMenuItem sortPopupItem = new JMenuItem("Sort on column values");
        sortPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int colAtPoint = getColumnAtPoint(popupMenu);

                if (colAtPoint == -1) {
                    return;
                }

                MainController.instance().doSort(colAtPoint);
            }
        });
        popupMenu.add(sortPopupItem);

        final JMenu diagramsPopupMenu = new JMenu("Diagrams");
        final JMenuItem pieChartPopupItem = new JMenuItem("Pie chart on row values");
        pieChartPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int rowAtPoint = getRowAtPoint(popupMenu);

                if (rowAtPoint == -1) {
                    return;
                }

                MainController.instance().showDiagram(new PieChartStrategy(), rowAtPoint);
            }
        });
        diagramsPopupMenu.add(pieChartPopupItem);

        final JMenuItem barChartPopupItem = new JMenuItem("Bar chart on column values");
        barChartPopupItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int colAtPoint = getColumnAtPoint(popupMenu);

                if (colAtPoint == -1) {
                    return;
                }

                MainController.instance().showDiagram(new BarChartStrategy(), colAtPoint);
            }
        });
        diagramsPopupMenu.add(barChartPopupItem);

        popupMenu.add(diagramsPopupMenu);

        this.setComponentPopupMenu(popupMenu);
    }

    private int getRowAtPoint(JComponent accordingTo) {
        Point point = SwingUtilities.convertPoint(accordingTo, new Point(0, 0), SimpleTable.this);
        return SimpleTable.this.rowAtPoint(point);
    }

    private int getColumnAtPoint(JComponent accordingTo) {
        Point point = SwingUtilities.convertPoint(accordingTo, new Point(0, 0), SimpleTable.this);
        return SimpleTable.this.columnAtPoint(point);
    }

    private boolean isProtectedColumn(int colIdx) {
        if (!(MainPanel.instance().getTable() instanceof ProtectedFieldTableDecorator protectedFieldTableDecorator)) {
            return false;
        }

        final Position position = protectedFieldTableDecorator.getProtectedPosition();

        return position.getColumn() == colIdx;
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
        return new SimpleTableModel(data, headers);
    }

}
