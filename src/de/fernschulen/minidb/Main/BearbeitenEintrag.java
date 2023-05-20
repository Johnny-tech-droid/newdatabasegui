package de.fernschulen.minidb.Main;

import de.fernschulen.minidb.Main.util.MiniDBTools;
import de.fernschulen.minidb.Main.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a dialog for editing database entries.
 * It extends {@link javax.swing.JDialog}, providing functionalities to read, delete and update database records.
 * <p>
 * It maintains an internal state connected to a specific SQL database via a {@link java.sql.Connection},
 * and manipulates entries of the "adressen" table. Navigation through the records is also possible.
 * It uses a GUI for interacting with the database records, providing JTextField for every database field.
 * </p>
 */
public class BearbeitenEintrag extends JDialog {
    /*
     * ============================ MEMBER VARIABLES HERE =====================
     */

    private static final String[] FIELD_NAMES = Properties.getFIELD_NAMES();
    private Map<String, JTextField> textFields;
    private static JLabel infoLabel;
    private static final String DB_URL = Properties.getDbUrl();

   // für die ImageIcons
    private static final String IMAGE_BASE_PATH = "/images/";
    private static final Map<String, String> ICON_MAP = Map.of(
            "loeschen", "Delete24.gif",
            "einenvor", "Forward24.gif",
            "einenzurueck", "Back24.gif",
            "vor", "Front24.gif",
            "zurueck", "End24.gif",
            "aktualisieren", "Save24.gif"
    );



    //für die Anzeige
    private JLabel nummer;
    //für die Aktionen
    private MeineAktionen loeschenAct, vorAct, zurueckAct, startAct, endeAct, aktualisierenAct;
    //für die Verbindung
    private Connection connection;
    private ResultSet resultSet;
    //für die Abfrage
    private String sqlQuery;

    /*
     * ============================ CONSTRUCTOR =============================
     */
    /**
     * Constructs a new instance of BearbeitenEintrag.
     * @param parent the JFrame from which the dialog is displayed
     * @param modal specifies whether dialog blocks user input to other top-level windows when shown
     */
    //der Konstruktor der Klasse BearbeitenEintrag
    public BearbeitenEintrag(JFrame parent, boolean modal) {
        super(parent, modal);
        initializeWindow();
        //die Aktionen erstellen
        initActions();

        addComponents();
        initializeDB();


        //die Verbindung mit dem Listener des Fensters herstellen
        addWindowListener(new FensterListener());

        //packen und anzeigen
        pack();
        setVisible(true);
        //Standardoperation setzen
        //hier den Dialog ausblenden und löschen
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }



    /*
     * ========================== GUI RELATED METHODS ========================
     */
    private void addComponents() {
        //die Symbolleiste oben einfügen
        add(symbolleiste(), BorderLayout.NORTH);


        //die Oberfläche erstellen und einfügen
        add(initGui(), BorderLayout.CENTER);

        // das Label fuer die anzeige Datensatz x von y
        add(initLabelPanel(), BorderLayout.SOUTH);
    }

    private void initializeWindow() {
        setTitle("Einträge bearbeiten");

        //wir nehmen ein Borderlayout
        setLayout(new BorderLayout());
    }

    private void initActions() {
        loeschenAct = new MeineAktionen("Datensatz löschen",
                loadImageIcon("loeschen"),
                "Löscht den aktuellen Datensatz",
                null,
                "loeschen");
        vorAct = new MeineAktionen("Einen Datensatz weiter",
                loadImageIcon("einenvor"),
                "Blättert einen Datensatz weiter",
                null,
                "einenvor");
        zurueckAct = new MeineAktionen("Einen Datensatz zurück",
                loadImageIcon("einenzurueck"),
                "Blättert einen Datensatz zurück",
                null,
                "einenzurueck");
        startAct = new MeineAktionen("Zum ersten Datensatz",
                loadImageIcon("vor"),
                "Geht zum ersten Datensatz",
                null,
                "vor");
        endeAct = new MeineAktionen("Zum letzten Datensatz",
                loadImageIcon("zurueck"),
                "Geht zum letzten Datensatz",
                null,
                "zurueck");
        aktualisierenAct = new MeineAktionen("Änderungen speichern",
                loadImageIcon("aktualisieren"),
                "Speichert Änderungen am aktuellen Datensatz",
                null,
                "aktualisieren");

    }

    private void initTextFields() {
        textFields = new HashMap<>();
        for (String fieldName : FIELD_NAMES) {
            textFields.put(fieldName, new JTextField());
        }
    }

    private JPanel initLabelPanel() {
        infoLabel = new JLabel("Datensatz: 1 von x");
        JPanel panel = new JPanel();
        panel.add(infoLabel);

        return panel;
    }
    //fügt die Felder in ein Panel ein und liefert das Panel zurück
    private JPanel initGui() {
        JPanel tempPanel = new JPanel();
        //im GridLayout mit zwei Spalten
        tempPanel.setLayout(new GridLayout(0, 2));
        //für die Nummer (nur Anzeige)
        tempPanel.add(new JLabel("ID-Nummer:"));
        nummer = new JLabel();
        tempPanel.add(nummer);

        initTextFields();
        for (String fieldName : FIELD_NAMES) {
            tempPanel.add(new JLabel(fieldName.toUpperCase() + ":"));
            tempPanel.add(textFields.get(fieldName));
        }
        //zurückgeben
        return tempPanel;
    }
    //die Symbolleiste erzeugen und zurückgeben
    private JToolBar symbolleiste() {
        JToolBar leiste = new JToolBar();
        //die Symbole über die Aktionen einbauen
        Action[] actions = {loeschenAct, aktualisierenAct, startAct, zurueckAct, vorAct, endeAct};
        for (int i = 0; i < actions.length; i++) {
            leiste.add(actions[i]);
            if (i == 1) {
                leiste.addSeparator();
        }

        }
        //die komplette Leiste zurückgeben
        return (leiste);
    }
    //die Methode liest die Daten und schreibt sie in die Felder
    /*
     * ========================= DATABASE RELATED METHODS ====================
     */
    /**
     * Method to initialize the database connection and load the first data entry into the GUI.
     */
    private void initializeDB() {
        //zuerst nehmen wir alle Einträge aus der Tabelle adressen
        sqlQuery = "SELECT * FROM adressen";
        //diese Abfrage wählt nur alle Müllers aus
        //sqlQuery = "SELECT * FROM adressen WHERE nachname = 'Müller'";

        //die Datenbankverbindung herstellen
        initDB();
        updateInfoLabel();
    }

    /**
     * Method to initialize the database connection and prepare a {@link java.sql.ResultSet} for future actions.
     */
    private void initDB() {
        try{
            //Verbindung herstellen und Ergebnismenge beschaffen
            connection =MiniDBTools.oeffnenDB(DB_URL);
            resultSet = MiniDBTools.liefereErgebnis(connection, sqlQuery);
            if (resultSet.next())
                datenLesen();
        }
        catch(Exception e) {
            showErrorDialog(e);
        }
    }
    private void datenLesen() {
        try {
            nummer.setText(Integer.toString(resultSet.getInt(1)));
            for (String fieldName : FIELD_NAMES) {
                textFields.get(fieldName).setText(resultSet.getString(resultSet.findColumn(fieldName)));
            }
        }
        catch(Exception e) {
            showErrorDialog(e);
        }
    }
    private int getTotalRowCount() {
        int rowCount = 0;
        try {
            int currentRow = resultSet.getRow();
            resultSet.last();
            rowCount = resultSet.getRow();
            resultSet.absolute(currentRow);
        } catch (SQLException e) {
            showErrorDialog(e);
        }
        return rowCount;
    }
    private void updateInfoLabel() {
        try {
            int currentRow = resultSet.getRow();
            int totalRows = getTotalRowCount();
            infoLabel.setText("Datensatz: " + currentRow + " von " + totalRows);
        } catch (SQLException e) {
            showErrorDialog(e);
        }
    }

    //die Methode geht zum ersten Datensatz
    private void ganzVor() {
        try {
            //ganz nach vorne gehen
            resultSet.first();
            datenLesen();
            updateInfoLabel();
        }

        catch(Exception e) {
            showErrorDialog(e);
        }
    }

    //die Methode geht zum letzten Datensatz
    private void ganzZurueck() {
        try {
            //ganz nach hinten gehen
            resultSet.last();
            datenLesen();
            updateInfoLabel();
        }
        catch(Exception e) {
            showErrorDialog(e);
        }
    }

    //die Methode geht einen Datensatz weiter
    private void einenVor() {
        try {
            //gibt es noch einen Datensatz?
            if (resultSet.next()) {
                datenLesen();
                updateInfoLabel();
            }

        }
        catch(Exception e) {
            showErrorDialog(e);
        }
    }

    //die Methode geht einen Datensatz zurück
    private void einenZurueck() {
        try{
            //gibt es noch einen Datensatz davor?
            if (resultSet.previous()) {
                datenLesen();
                updateInfoLabel();
            }

        }
        catch(Exception e) {
            showErrorDialog(e);
        }
    }

    //die Methode löscht einen Datensatz
    private void loeschen() {
        try {
            //wir müssen uns merken, wo wir sind
            int position;
            position = resultSet.getRow();
            //den Eintrag löschen
            resultSet.deleteRow();
            //Ergebnismenge schließen
            resultSet.close();
            // und neu öffnen
            resultSet = MiniDBTools.liefereErgebnis(connection, sqlQuery);

            //und wieder zur "alten" Position gehen
            resultSet.absolute(position);
            //stehen wir jetzt hinter dem letzten?
            if (resultSet.isAfterLast())
                //dann zum letzten gehen
                resultSet.last();
            //die Daten neu lesen
            datenLesen();
            updateInfoLabel();
        }
        catch(Exception e) {
            showErrorDialog(e);
        }
    }

    //die Methode aktualisiert einen Eintrag
    private void aktualisieren() {
        try {
            //wir müssen uns merken, wo wir sind
            int position;
            position = resultSet.getRow();

            //die Daten aktualisieren
            for (int i = 0; i < FIELD_NAMES.length; i++) {
                resultSet.updateString(i + 2, textFields.get(FIELD_NAMES[i]).getText());
            }
            //den Datensatz aktualisieren
            resultSet.updateRow();
            //Ergebnismenge schließen
            resultSet.close();
            // und neu öffnen
            resultSet = MiniDBTools.liefereErgebnis(connection, sqlQuery);
            //und wieder zur "alten" Position gehen
            resultSet.absolute(position);
            //die Daten neu lesen
            datenLesen();
            updateInfoLabel();
        }
        catch(Exception e) {
            showErrorDialog(e);
        }
    }
    /*
     * ================== UTILITY METHODS ==========================
     */

    /**
     * Method to load a specific ImageIcon based on a given command.
     * @param actionCommand command to specify which ImageIcon to load
     * @return ImageIcon to be used for an action
     */
    private ImageIcon loadImageIcon(String actionCommand) {
        URL imageUrl = getClass().getResource(IMAGE_BASE_PATH + ICON_MAP.get(actionCommand));
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        }
        else {
            System.err.println("Could not find image: " +IMAGE_BASE_PATH + ICON_MAP.get(actionCommand));
            return null;
        }
    }

    /**
     * Method to create error dialog when exceptions are encountered.
     * @param e the exception to be displayed in the dialog
     */
    private void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(this, "Problem: \n" + e.toString());
    }

    /*
     * ==================== INNER CLASSES =========================
     */

    /**
     * Inner class for defining actions on database entries.
     * Extends {@link javax.swing.AbstractAction} and provides cases to handle specific action commands.
     */
    //eine innere Klasse für die Aktionen
    class MeineAktionen extends AbstractAction {

        //der Konstruktor
        public MeineAktionen(String text, ImageIcon icon, String beschreibung, KeyStroke shortcut, String actionText) {
            //den Konstruktor der übergeordneten Klasse mit dem Text und dem Icon aufrufen
            super(text, icon);
            //die Beschreibung setzen für den Bildschirmtipp
            putValue(SHORT_DESCRIPTION, beschreibung);
            //den Shortcut
            putValue(ACCELERATOR_KEY, shortcut);
            //das ActionCommand
            putValue(ACTION_COMMAND_KEY, actionText);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "vor" -> ganzVor();
                case "zurueck" -> ganzZurueck();
                case "einenvor" -> einenVor();
                case "einenzurueck" -> einenZurueck();
                case "loeschen" -> loeschen();
                case "aktualisieren" -> aktualisieren();
            }
        }
    }
    /**
     * Inner class for handling window closing events.
     * Extends {@link java.awt.event.WindowAdapter} and overrides windowClosing() method to close the database connection when the window is closing.
     */
    //die innere Klasse für die Fensterereignisse
    class FensterListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            //die Datenbankverbindung trennen
            closeDBConnection();

        }
        private void closeDBConnection() {
            try {
                BearbeitenEintrag.this.resultSet.close();
                BearbeitenEintrag.this.connection.close();
                MiniDBTools.schliessenDB(DB_URL);
            }
            catch(Exception exc) {
                JOptionPane.showMessageDialog(null, "Problem: \n" + exc.toString());
            }
        }
    }

}
