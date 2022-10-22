package edu.gof.visitor;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import edu.gof.visitor.controller.MainController;

import java.sql.Timestamp;
import java.util.logging.Logger;

public class ApplicationMain {

    private static final Logger log = Logger.getLogger(ApplicationMain.class.getName());

    public static void main(String[] args) {
        log.info(String.format("Application startup at %s", new Timestamp(System.currentTimeMillis())));
        FlatIntelliJLaf.setup(); // look-and-feel
        new MainController();
    }

}
