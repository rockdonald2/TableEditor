package edu.gof.visitor.panel;

import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.service.exception.ServiceException;
import edu.gof.visitor.service.loader.Data;
import edu.gof.visitor.utils.Constants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class MainPanel extends JFrame {

    private final MainController mainController;
    private JTable table;
    private JButton addRowBtn;
    private JButton addColumnBtn;
    private JMenuItem exportItem;

    public MainPanel(MainController mainController) {
        this.mainController = mainController;

        createMenuButtons();
        createMenuBar();
        createTable();
        baseConfig();
    }

    private void addNewRow(ActionEvent e) {
        mainController.doAddNewRow();
    }

    private void addNewColumn(ActionEvent actionEvent) {
        String columnName = JOptionPane.showInputDialog(this, "Specify the name of the new column");

        if (columnName == null || columnName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty column name specified");
            return;
        }

        mainController.doAddNewColumn(columnName);
    }

    private void createMenuButtons() {
        final JPanel menuPanel = new JPanel();

        addRowBtn = new JButton("Add Row");
        addRowBtn.setEnabled(false);
        addRowBtn.addActionListener(this::addNewRow);

        addColumnBtn = new JButton("Add Column");
        addColumnBtn.setEnabled(false);
        addColumnBtn.addActionListener(this::addNewColumn);

        menuPanel.add(addRowBtn);
        menuPanel.add(addColumnBtn);
        this.add(menuPanel);
    }

    private void createTable() {
        this.table = new JTable();
        this.table.setSize(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT);
        this.table.setModel(new DefaultTableModel(new String[][]{}, new String[]{"...", "..."}));
        this.add(new JScrollPane(table));
    }

    private void createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu mainMenu = new JMenu("Main Menu");
        mainMenu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(mainMenu);

        final JMenuItem openItem = new JMenuItem("Open Document");
        openItem.addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Comma-separated values", "csv"));
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int hasOpened = chooser.showOpenDialog(MainPanel.this);

            if (hasOpened != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selectedFile = chooser.getSelectedFile();
            mainController.doImportData(selectedFile.getAbsolutePath());
        });

        exportItem = new JMenuItem("Export Table");
        exportItem.addActionListener(e -> {
            mainController.doExportData();
        });
        exportItem.setEnabled(false);

        mainMenu.add(openItem);
        mainMenu.add(exportItem);
        this.setJMenuBar(menuBar);
    }

    private void baseConfig() {
        this.getRootPane().registerKeyboardAction(e -> exportItem.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null); // centralizer
        this.setSize(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void displayData(Data data) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        String[][] tableData = new String[rowData.size()][headers.size()];
        IntStream.range(0, rowData.size())
                .forEach(idx -> {
                    tableData[idx] = rowData.get(idx).toArray(new String[]{});
                });

        this.table.setModel(new DefaultTableModel(tableData, headers.toArray()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
                mainController.doModifyValueAt(row, column, aValue.toString());
            }
        });

        addRowBtn.setEnabled(true);
        addColumnBtn.setEnabled(true);
        exportItem.setEnabled(true);
    }

    public Optional<File> saveData() throws ServiceException {
        final JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setFileFilter(new FileNameExtensionFilter("JSON", "json"));
        int hasOpened = chooser.showSaveDialog(this);

        if (hasOpened != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }

        return Optional.ofNullable(chooser.getSelectedFile());
    }

}
