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
    public MiniDBStart(String title) throws HeadlessException {
        super(title);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        initGui();
        initActions();
    }
    /*
     * ============================= GUI RELATED METHODS =================================
     */
    private void initActions() {
        actions = new HashMap<>();
        actions.put(ACTION_COMMANDS[0], this::anzeigeListe);
        actions.put(ACTION_COMMANDS[1], this::anzeigeEinzel);
        actions.put(ACTION_COMMANDS[2], this::neuerEintrag);
        actions.put(ACTION_COMMANDS[3], this::beenden);
    }

    private void initGui() {
        MeinListener listener = new MeinListener();
        JPanel panel = createButtonPanel(listener);
        add(panel);
        //Größe setzen, Standardverhalten festlegen und anzeigen
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

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
    private void beenden() {
        System.exit(0);
    }

    private void neuerEintrag() {
        new NeuerEintrag(this, true);
    }

    private void anzeigeEinzel() {
        new BearbeitenEintrag(this, true);
    }

    private void anzeigeListe() {
        new ListenAnzeige(this, true);
    }
    /*
     * ====================================== INNER CLASSES ======================================
     */
    class MeinListener implements ActionListener {

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
