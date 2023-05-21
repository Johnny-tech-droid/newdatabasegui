/**
 * ListView.java
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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class ListView extends JDialog{
    /*
     * ============================ MEMBER VARIABLES HERE =====================
     */
    private static final String DB_URL = Properties.getDbUrl();
    private static final String[] FIELD_NAMES = Properties.getFIELD_NAMES_EDIT();



    /*
     * ============================ CONSTRUCTOR =============================
     */

    /**
     * Constructor for the ListView class.
     *
     * @param parent the parent JFrame
     * @param modal modal a boolean indicating whether the dialog should be modal
     */
    public ListView(JFrame parent, boolean modal) {
        super(parent, modal);
        setTitle("Listenanzeige");
        setLayout(new FlowLayout(FlowLayout.CENTER));

        //die Daten lesen
        readData();
        //die Schaltfläche
        JButton ok = GuiComponentFactory.createButton("Ok", "ok", new ListListener());
        add(ok);
        //Größe setzen und anzeigen
        setSize(440, 700);
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /*
     * ========================= DATABASE RELATED METHODS ====================
     */

    //die Methode liest die Daten ein und zeigt sie an

    /**
     * Reads the data and displays it.
     */
    private void readData() {
        List<Map<String, String>> data = fetchData();
        displayData(data);

    }

    /**
     * Fetches data from the database.
     * @return a list of maps where each map represents a row in the database,
     * with keys being the column names and values being the corresponding field values
     */
    private List<Map<String, String>> fetchData() {
        List<Map<String, String>> data = new ArrayList<>();
        try (Connection connection = MiniDBTools.oeffnenDB(DB_URL);
             ResultSet resultSet = MiniDBTools.liefereErgebnis(connection, "SELECT * FROM adressen")
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String fieldName = metaData.getColumnName(i);
                    row.put(fieldName, resultSet.getString(i));
                }
                data.add(row);
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Problem: \n" + e);
        }
        return data;
    }

    /**
     * Display the fetched data on the panel.
     *
     * @param data a list of maps where each map represents a row in the database,
     *             with keys being the column names and values being the corresponding field values.
     */
    private void displayData(List<Map<String, String>> data) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));
        JScrollPane pane = new JScrollPane(panel);
        pane.setPreferredSize(new Dimension(400, 620));

        for (Map<String, String> row : data) {
            for (String fieldName : FIELD_NAMES) {
                panel.add(new JLabel(fieldName + ":"));
                panel.add(new JLabel(row.get(fieldName)));
            }

            panel.add(new JLabel("---------------------"));
            panel.add(new JLabel("---------------------"));

        }
        add(pane);
    }
    /*
     * ==================== INNER CLASSES =========================
     */

    /**
     * Inner class for handling button click events.
     */
    class ListListener implements ActionListener {
        /**
         * The method to be executed when an action event is detected.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            //wurde auf OK geklickt?
            if (e.getActionCommand().equals("ok"))
                //dann Dialog schließen
                dispose();
        }
    }
}
