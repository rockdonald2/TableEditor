package edu.gof.visitor;

import com.formdev.flatlaf.FlatLightLaf;
import edu.gof.visitor.controller.MainController;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class ApplicationMain {

    public static void main(String[] args) {
        FlatLightLaf.setup(); // look-and-feel

        SwingUtilities.invokeLater(() -> MainController.instance().init());
    }

}
