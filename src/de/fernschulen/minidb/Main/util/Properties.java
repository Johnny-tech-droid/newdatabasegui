/**
 * Properties.java
 * Created by Johnny-tech-droid on 20.05.23
 * newdatabasegui
 * de.fernschulen.minidb.Main.util
 */


package de.fernschulen.minidb.Main.util;

public class Properties {
    private static final String DB_URL = "jdbc:derby:miniDB";
    private static final String[] FIELD_NAMES = {"NAME", "LASTNAME", "STREET", "POSTALCODE", "CITY", "PHONE"};
    private static final String[] FIELD_NAMES_EDIT = {"IDNUMBER", "NAME", "LASTNAME", "STREET", "POSTALCODE", "CITY", "PHONE"};
    private static final String[] BUTTON_TEXTS_MAIN = {"Listenanzeige", "Einzelanzeige", "Neuanzeige", "Ende"};
    private static final String[] ACTION_COMMANDS_MAIN = {"liste", "einzel", "neu", "ende"};
    public static String[] getButtonTextsMain() {
        return BUTTON_TEXTS_MAIN;
    }
    public static String[] getActionCommandsMain() {
        return ACTION_COMMANDS_MAIN;
    }
    public static String getDbUrl() {
        return DB_URL;
    }

    public static String[] getFIELD_NAMES() {
        return FIELD_NAMES;
    }

    public static String[] getFIELD_NAMES_EDIT() {
        return FIELD_NAMES_EDIT;
    }
}
