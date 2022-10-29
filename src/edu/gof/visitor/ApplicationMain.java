package edu.gof.visitor;

import com.formdev.flatlaf.FlatLightLaf;
import edu.gof.visitor.controller.MainController;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class ApplicationMain {

    private static final Logger log = Logger.getLogger(ApplicationMain.class.getName());

    public static void main(String[] args) {
        log.info(String.format("Application startup at %s", new Timestamp(System.currentTimeMillis())));
        FlatLightLaf.setup(); // look-and-feel

        SwingUtilities.invokeLater(() -> MainController.instance().init());
    }

}
