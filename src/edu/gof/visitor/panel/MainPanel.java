package edu.gof.visitor.panel;

import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.service.loader.Data;
import edu.gof.visitor.utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

public class MainPanel extends JFrame {

    private final MainController mainController;
    private JTable table;

    public MainPanel(MainController mainController) {
        this.mainController = mainController;

        createMenuBar();
        createTable();
        baseConfig();
    }

    private void createTable() {
        this.table = new JTable();
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
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int hasOpened = chooser.showOpenDialog(MainPanel.this);

            if (hasOpened != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selectedFile = chooser.getSelectedFile();
            mainController.doImportData(selectedFile.getAbsolutePath());
        });
        final JMenuItem exportItem = new JMenuItem("Export Table");
        exportItem.addActionListener(e -> {
            mainController.doExportData();
        });

        mainMenu.add(openItem);
        mainMenu.add(exportItem);
        this.setJMenuBar(menuBar);
    }

    private void baseConfig() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null); // centralizer
        this.setSize(Constants.WIN_SIZE_WIDTH, Constants.WIN_SIZE_HEIGHT);
    }

    public void showError(String message) {
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

        this.table.setModel(new DefaultTableModel(tableData, headers.toArray()));
    }

}
