package schilkroete.master3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Schilkroete on 16.02.2017.
 */

/**
 * Die PatientenakteDatenbankManager-Klasse hilft uns beim Erstellen und Aktualisieren der Datenbank.
 */
public class PatientenakteDatenbankManager extends SQLiteOpenHelper {

    private static final String LOG_TAG = PatientenakteDatenbankManager.class.getSimpleName();

    private static final String DB_NAME = "ergoTest.db";
    public static final int DB_VERSION = 1;

    public static final String TABELLEN_NAME = "patientenakte";
    public static final String SPALTE_ID = "_id";
    public static final String SPALTE_VORNAME = "vorname";
    public static final String SPALTE_NACHNAME = "nachname";
    public static final String SPALTE_GEBURTSDATUM = "geburtsdatum";
    public static final String SPALTE_BESCHWERDEN = "beschwerden";
    public static final String SPALTE_MEDIKAMENTE = "medikamente";
    public static final String SPALTE_NOTIZEN = "notizen";

    public static final String SQL_CREATE =
            "CREATE TABLE " +
                    TABELLEN_NAME + "(" +
                    SPALTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SPALTE_VORNAME + " NVARCHAR NOT NULL, " +
                    SPALTE_NACHNAME + " NVARCHAR NOT NULL, " +
                    SPALTE_GEBURTSDATUM + " DATETIME NOT NULL, " +
                    SPALTE_BESCHWERDEN + " TEXT NOT NULL, " +
                    SPALTE_MEDIKAMENTE + " TEXT NOT NULL, " +
                    SPALTE_NOTIZEN + " TEXT NOT NULL);";


    public PatientenakteDatenbankManager(Context context) {
        /* Ruft Konstruktor der PatientenakteDatenbankManager-Elternklasse auf
         * (auf dieser ist die SQLiteObenHelpter-Klasse)
         */
        super(context, DB_NAME, null, DB_VERSION);
        Log.e(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }


    // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
    // !Sollte die App deinstalliert werden wird die DB auch entfernt!
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            /* Die Tabelle wird erstellt und die SQL-Datenbank eingefuegt
             * Damit wird mittels "execSQL()" dem SQL-DB-Objekt
             * das SQL-Kommando zur Ausfuehrung uebergeben
             */
            Log.e(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        /* Sollte ein Fehler beim Erstellen der Tabelle aufgetreten sein,
         * wird dieser hier aufgefangen
         */ catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}