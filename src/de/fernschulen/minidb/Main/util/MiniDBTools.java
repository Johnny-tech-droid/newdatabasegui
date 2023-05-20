/**
 * MiniDBTools.java
 * Created by Johnny-tech-droid on 18.05.23
 * newdatabasegui
 * de.fernschulen.minidb.Main.util
 */


package de.fernschulen.minidb.Main.util;

import javax.swing.*;
import java.sql.*;


public class MiniDBTools {

    //die Methode stellt die Verbindung her
    //zurückgeliefert wird ein Connection-Objekt
    //übergeben werden Argumente für das Öffnen
    public static Connection oeffnenDB(String arg) {
        Connection verbindung = null;
        try {
            //die Datenbank ist adressenDB
            verbindung = DriverManager.getConnection(arg);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Problem: \n" + e.toString());
        }
        return verbindung;
    }

    //die Methode liefert eine Ergebnismenge
    //übergeben wird die Verbindung und der SQL-Ausdruck
    public static ResultSet liefereErgebnis(Connection verbindung, String sqlAnweisung) {
        ResultSet ergebnisMenge = null;
        try {
            Statement state = verbindung.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ergebnisMenge = state.executeQuery(sqlAnweisung);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Problem: \n" + e.toString());
        }
        return ergebnisMenge;
    }

    //die Methode fährt das gesamte Datenbanksystem herunter
    //das wird bei der "embedded" Version von Apache Derby ausdrücklich empfohlen
    //übergeben wird das Protokoll
    public static void schliessenDB(String protokoll) {
        //das Herunterfahren löst bei Erfolg eine Exception aus!!!
        boolean erfolg = false;
        try {
            DriverManager.getConnection(protokoll + "adressenDB; shutdown = true");
        }
        catch (SQLException e) {
            erfolg = true;
        }
        if (!erfolg)
            JOptionPane.showMessageDialog(null, "Das DBMS konnte nicht heruntergefahren werden.");
    }
}
