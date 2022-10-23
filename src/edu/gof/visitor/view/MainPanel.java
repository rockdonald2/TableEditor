package edu.gof.visitor.view;

import edu.gof.visitor.command.AddColumnCommand;
import edu.gof.visitor.command.AddRowCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Data;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.service.exception.ServiceException;
import edu.gof.visitor.utils.Constants;

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
        this.table = new Table();
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT));
        this.add(scrollPane);
    }

    private void createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu mainMenu = new JMenu("Main Menu");
        mainMenu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(mainMenu);

        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        final JMenuItem openItem = new JMenuItem("Open Document");
        openItem.addActionListener(this::importData);

        exportItem = new JMenuItem("Export Table");
        exportItem.addActionListener(e -> mainController.doExportData());
        exportItem.setEnabled(false);

        addRowBtn = new JMenuItem("Add Row");
        addRowBtn.setEnabled(false);
        addRowBtn.addActionListener(this::addNewRow);

        addColumnBtn = new JMenuItem("Add Column");
        addColumnBtn.setEnabled(false);
        addColumnBtn.addActionListener(this::addNewColumn);

        mainMenu.add(openItem);
        mainMenu.add(exportItem);

        fileMenu.add(addRowBtn);
        fileMenu.add(addColumnBtn);

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

        table.displayData(tableData, headers.toArray(new String[]{}));

        addRowBtn.setEnabled(true);
        addColumnBtn.setEnabled(true);
        exportItem.setEnabled(true);
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

    public Optional<File> saveData() throws ServiceException {
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
