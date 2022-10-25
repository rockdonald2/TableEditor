package edu.gof.visitor.view.menu;

import edu.gof.visitor.view.exception.ViewException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MenuBar extends JMenuBar {

    private final Map<String, JMenu> menus;

    public MenuBar() {
        this.menus = new HashMap<>();
    }

    public JMenu addMenu(String menuName, int shortcutKey) {
        if (menus.containsKey(menuName)) {
            throw new ViewException("Menu already exists");
        }

        final JMenu menu = new JMenu(menuName);
        menu.setMnemonic(shortcutKey);
        this.menus.put(menu.getText(), menu);
        this.add(menu);

        return menu;
    }

    public JMenuItem addItemToMenu(String menuName, String itemName, ActionListener listener, boolean enabled) {
        if (!menus.containsKey(menuName)) {
            throw new ViewException("Non-existing menu given");
        }

        final JMenu menu = menus.get(menuName);

        final JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.addActionListener(listener);
        menuItem.setEnabled(enabled);

        menu.add(menuItem);

        return menuItem;
    }

    public JCheckBoxMenuItem addToggleItemToMenu(String menuName, String itemName, ActionListener listener, boolean enabled) {
        if (!menus.containsKey(menuName)) {
            throw new ViewException("Non-existing menu given");
        }

        final JMenu menu = menus.get(menuName);

        final JCheckBoxMenuItem toggleButton = new JCheckBoxMenuItem(itemName);
        toggleButton.addActionListener(listener);
        toggleButton.setEnabled(enabled);
        menu.add(toggleButton);

        return toggleButton;
    }

}
