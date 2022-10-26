package edu.gof.visitor.view;

import edu.gof.visitor.command.AddColumnCommand;
import edu.gof.visitor.command.AddRowCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Data;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.utils.Constants;
import edu.gof.visitor.view.menu.MenuBar;
import edu.gof.visitor.view.table.SimpleTable;
import edu.gof.visitor.view.table.Table;
import edu.gof.visitor.view.table.decorator.ResettableTableDecorator;
import edu.gof.visitor.view.table.decorator.RowNumberTableDecorator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public final class MainPanel extends JFrame {

    private static MainPanel instance;

    private final MainController mainController;

    private Table table;
    private JMenuItem addRowBtn;
    private JMenuItem addColumnBtn;
    private JMenuItem exportItem;
    private JCheckBoxMenuItem rowDecoratorBtn;

    // flag
    private boolean initialized;

    private MainPanel(MainController mainController) {
        this.mainController = mainController;
    }

    public static synchronized MainPanel instance() {
        if (instance == null) {
            instance = new MainPanel(MainController.instance());
        }

        return instance;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException(String.format("%s already initialized", MainPanel.class.getName()));
        }

        createMenuBar();
        createTable();
        baseConfig();
        initialized = true;
    }

    public Table getTable() {
        return table;
    }

    private void addNewRow(ActionEvent e) {
        mainController.executeCommand(new AddRowCommand(mainController, new Position(mainController.getRows(), 0)));
    }

    private void addNewColumn(ActionEvent actionEvent) {
        String columnName = JOptionPane.showInputDialog(this, "Specify the name of the new column");

        if (columnName == null || columnName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty column name specified");
            return;
        }

        mainController.executeCommand(new AddColumnCommand(mainController, new Position(0, mainController.getColumns()), columnName));
    }

    private void createTable() {
        this.table = new SimpleTable();
        final JScrollPane scrollPane = new JScrollPane(table.getComponent());
        scrollPane.setPreferredSize(new Dimension(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT));
        this.add(scrollPane);
    }

    private void createMenuBar() {
        final edu.gof.visitor.view.menu.MenuBar menuBar = new MenuBar();

        final JMenu mainMenu = menuBar.addMenu("Main Menu", KeyEvent.VK_M);
        final JMenu fileMenu = menuBar.addMenu("File", KeyEvent.VK_F);
        final JMenu othersMenu = menuBar.addMenu("Others", KeyEvent.VK_O);

        menuBar.addItemToMenu(mainMenu.getText(), "Open Document", MainPanel.this::importData, true);
        exportItem = menuBar.addItemToMenu(mainMenu.getText(), "Export Table", e -> mainController.doExportData(), false);
        addRowBtn = menuBar.addItemToMenu(fileMenu.getText(), "Add Row", this::addNewRow, false);
        addColumnBtn = menuBar.addItemToMenu(fileMenu.getText(), "Add Column", this::addNewColumn, false);
        rowDecoratorBtn = menuBar.addToggleItemToMenu(othersMenu.getText(), "Add Row Numbering", e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                MainPanel.this.table = new RowNumberTableDecorator(MainPanel.this.table);
                MainPanel.this.mainController.doDisplayData();
            } else {
                if (MainPanel.this.table instanceof ResettableTableDecorator resettableTableDecorator) {
                    resettableTableDecorator.resetModel();
                }

                MainPanel.this.table = (Table) MainPanel.this.table.getComponent();
                MainPanel.this.mainController.doDisplayData();
            }
        }, false);

        this.setJMenuBar(menuBar);
    }

    private void baseConfig() {
        registerKeyShortcuts();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setSize(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT);
    }

    private void registerKeyShortcuts() {
        this.getRootPane().registerKeyboardAction(e -> exportItem.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.getRootPane().registerKeyboardAction(e -> mainController.undoCommand(), KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.getRootPane().registerKeyboardAction(e -> mainController.redoCommand(), KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayData(Data data) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        String[][] tableData = new String[rowData.size()][headers.size()];
        IntStream.range(0, rowData.size())
                .forEach(idx -> {
                    tableData[idx] = rowData.get(idx).toArray(new String[]{});
                });

        table.displayData(table.constructModel(tableData, headers.toArray(new String[]{})));

        addRowBtn.setEnabled(true);
        addColumnBtn.setEnabled(true);
        exportItem.setEnabled(true);
        rowDecoratorBtn.setEnabled(true);
    }

    private void importData(ActionEvent e) {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int hasOpened = chooser.showOpenDialog(MainPanel.this);

        if (hasOpened != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final File selectedFile = chooser.getSelectedFile();
        mainController.doImportData(selectedFile.getAbsolutePath());
    }

    public Optional<File> saveData() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setFileFilter(new FileNameExtensionFilter("JSON (*.json)", "json"));
        int hasOpened = chooser.showSaveDialog(this);

        if (hasOpened != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }

        return Optional.ofNullable(chooser.getSelectedFile());
    }

}
