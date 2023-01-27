package edu.gof.visitor;

import edu.gof.visitor.controller.MainController;

import javax.swing.*;

public class ApplicationMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainController.instance().init());
    }

}
