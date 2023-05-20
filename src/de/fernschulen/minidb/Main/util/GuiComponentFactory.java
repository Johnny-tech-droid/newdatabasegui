/**
 * GuiComponentFactory.java
 * Created by Johnny-tech-droid on 20.05.23
 * newdatabasegui
 * de.fernschulen.minidb.Main.util
 */
/**
 * This class is a GuiHelper that extracts the logic of creating the GUI
 * Components to reduce repetition
 * initTextFields
 */

package de.fernschulen.minidb.Main.util;

import de.fernschulen.minidb.Main.BearbeitenEintrag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * In this class the GUI Component Factories are created.
 * For ListenAnzeige.java and NeuerEintrag.java the initialization of Buttons and ButtonPanels
 * is repetitive code.
 */
public class GuiComponentFactory {

    public static JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        return button;
    }

    public Map<String, JTextField> createTextFields(String[] fieldNames) {
        Map<String, JTextField> textFields = new HashMap<>();
        for (String fieldName : fieldNames) {
            JTextField textField = new JTextField();
            textFields.put(fieldName, textField);
        }
        return textFields;
    }
    public JPanel createTextFieldPanel(String[] fieldNames) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));
        Map<String, JTextField> textFields = createTextFields(fieldNames);
        for (String fieldName : fieldNames) {
            panel.add(new JLabel(fieldName.toUpperCase() +":"));
            panel.add(textFields.get(fieldName));
        }
        return panel;
    }
    public JLabel createLabel(String text) {
        return new JLabel(text);
    }
    public GuiComponentFactory() {
    }
}
