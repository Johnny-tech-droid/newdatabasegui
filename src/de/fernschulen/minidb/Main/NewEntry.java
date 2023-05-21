/**
 * NewEntry.java
 * Created by Johnny-tech-droid on 18.05.23
 * newdatabasegui
 * de.fernschulen.minidb.Main
 */


package de.fernschulen.minidb.Main;

import de.fernschulen.minidb.Main.util.GuiComponentFactory;
import de.fernschulen.minidb.Main.util.MiniDBTools;
import de.fernschulen.minidb.Main.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class NewEntry extends JDialog{
    /*
     * ============================ MEMBER VARIABLES HERE =====================
     */

    //für die Eingabefelder
    private static final String[] FIELD_NAMES = Properties.getFIELD_NAMES();

    private Map<String, JTextField> textFields;
    private static final String DB_URL = Properties.getDbUrl();



    /*
     * ============================ CONSTRUCTOR =============================
     */
    //der Konstruktor
    public NewEntry(JFrame parent, boolean modal) {
        super(parent, modal);
        setTitle("Neuer Eintrag");
        //die Oberfläche erstellen
        initGui();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    /*
     * ========================== GUI RELATED METHODS ========================
     */

    private JPanel initTextFields() {
//        GuiComponentFactory guiFactory = new GuiComponentFactory();
        JPanel panel = GuiComponentFactory.createTextFieldPanel(FIELD_NAMES);
        textFields = GuiComponentFactory.createTextFields(FIELD_NAMES);
        return panel;
    }
    private void initGui() {
        setLayout(new BorderLayout());

        JPanel textFieldPanel = initTextFields();

        add(textFieldPanel, BorderLayout.CENTER);
        add(initButtonPanel(), BorderLayout.SOUTH);

        //packen und anzeigen
        pack();
        setVisible(true);

    }
    private JPanel initButtonPanel() {
        JPanel panel = new JPanel();
        NeuListener listener = new NeuListener();
        panel.add(GuiComponentFactory.createButton("Ok", "ok", listener));
        panel.add(GuiComponentFactory.createButton("Abbrechen", "abbrechen", listener));
        return panel;
    }

    //die Methode legt einen neuen Datensatz an
    /*
     * ========================= DATABASE RELATED METHODS ====================
     */
    private void uebernehmen() {


        try (Connection connection = MiniDBTools.oeffnenDB(DB_URL);
             ResultSet resultSet = MiniDBTools.liefereErgebnis(connection, "SELECT * FROM adressen")
        ){

            resultSet.moveToInsertRow();
            for (int i = 0; i < FIELD_NAMES.length; i++) {
                String fieldName = FIELD_NAMES[i];
                String dbColumnName = FIELD_NAMES[i];
                JTextField field = textFields.get(fieldName);
                String text = field.getText();
                resultSet.updateString(dbColumnName, text);
            }
            resultSet.insertRow();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Problem: \n" + e);
        } finally {
            MiniDBTools.schliessenDB(DB_URL);
        }
    }

    /*
     * ==================== INNER CLASSES =========================
     */
    //die innere Klasse für den ActionListener
    class NeuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //wurde auf OK geklickt?
            if (e.getActionCommand().equals("ok")) {
                //dann die Daten übernehmen
                uebernehmen();
                dispose();
            }

            //wurde auf Abbrechen geklickt?
            if (e.getActionCommand().equals("abbrechen"))
                //dann Dialog schließen
                dispose();
        }
    }
}
