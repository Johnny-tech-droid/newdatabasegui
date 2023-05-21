/**
 * MiniDBStart.java
 * Created by Johnny-tech-droid on 18.05.23
 * newdatabasegui
 * de.fernschulen.minidb.Main
 */


package de.fernschulen.minidb.Main;

import de.fernschulen.minidb.Main.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MiniDBStart extends JFrame {
    private static final String[] ACTION_COMMANDS = Properties.getActionCommandsMain();

    private static final String[] BUTTON_TEXTS = Properties.getButtonTextsMain();
    private Map<String, Runnable> actions;

    /*
     * ============================= CONSTRUCTOR =========================================
     */

    /**
     * Constructor for the MiniDBStart class.
     *
     * @param title the title of the JFrame
     * @throws HeadlessException if the device does not have a display, keyboard, or  mouse
     */
    public MiniDBStart(String title) throws HeadlessException {
        super(title);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        initGui();
        initActions();
    }
    /*
     * ============================= GUI RELATED METHODS =================================
     */

    /**
     * Initialize the action map with actions to be taken when buttons are clicked
     */
    private void initActions() {
        actions = new HashMap<>();
        actions.put(ACTION_COMMANDS[0], this::anzeigeListe);
        actions.put(ACTION_COMMANDS[1], this::anzeigeEinzel);
        actions.put(ACTION_COMMANDS[2], this::neuerEintrag);
        actions.put(ACTION_COMMANDS[3], this::beenden);
    }

    /**
     * Initialize the GUI components and add them to the JFrame
     *
     */
    private void initGui() {
        MeinListener listener = new MeinListener();
        JPanel panel = createButtonPanel(listener);
        add(panel);
        //Größe setzen, Standardverhalten festlegen und anzeigen
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Create a panel containing buttons, each associated with an action.
     * @param listener the ActionListener to be attached to the buttons.
     * @return a JPanel object containing buttons
     */
    private JPanel createButtonPanel(ActionListener listener) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,4));
        for (int i = 0; i < ACTION_COMMANDS.length; i++) {
            JButton tempButton = new JButton();
            tempButton.setText(BUTTON_TEXTS[i]);
            tempButton.addActionListener(listener);
            tempButton.setActionCommand(ACTION_COMMANDS[i]);
            panel.add(tempButton);
        }

        return panel;

    }

    /*
     * =========================== METHODS RELATED TO ACTIONS ====================================
     */
    // This belongs to a ViewController, not a View

    /**
     * Exit the application
     */
    private void beenden() {
        System.exit(0);
    }

    /**
     * Open a new dialog for adding an entry
     */
    private void neuerEintrag() {
        new NewEntry(this, true);
    }

    /**
     * Open a new dialog for displaying an individual entry
     */

    private void anzeigeEinzel() {
        new EditEntry(this, true);
    }

    /**
     * Open a new dialog for displaying a list of entries.
     */

    private void anzeigeListe() {
        new ListView(this, true);
    }
    /*
     * ====================================== INNER CLASSES ======================================
     */

    /**
     * Inner class for handling button click events.
     */
    class MeinListener implements ActionListener {
        /**
         * The method to be executed when an action event is detected.
         *
         * @param e the event to be processed
         */

        @Override
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();
            Runnable action = actions.get(command);
            if (action != null) {
                action.run();
            }
        }
    }
}
