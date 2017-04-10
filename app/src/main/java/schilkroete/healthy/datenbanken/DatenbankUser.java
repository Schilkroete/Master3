package schilkroete.healthy.datenbanken;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Die DatenbankRollen-Klasse hilft uns beim Erstellen und Aktualisieren der Datenbank.
 */
public class DatenbankUser extends SQLiteOpenHelper {

    private static final String TAG = DatenbankUser.class.getSimpleName();

    private static final String DB_NAME = "userErsterVersuch.db";
    public static final int DB_VERSION = 1;

    public static final String TABELLEN_NAME = "user";
    public static final String SPALTE_ID_USER = "_id_user";
    public static final String SPALTE_VORNAME = "vorname";
    public static final String SPALTE_NACHNAME = "nachname";
    public static final String SPALTE_PASSWORT = "passwort";
    public static final String SPALTE_ROLLE = "rolle";
    public static final String SPALTE_ERSTELLDATUM = "erstelldatum";

    public static final String SQL_CREATE =
            "CREATE TABLE " +
                    TABELLEN_NAME + "(" +
                    SPALTE_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SPALTE_VORNAME + " NVARCHAR NOT NULL, " +
                    SPALTE_NACHNAME + " NVARCHAR NOT NULL, " +
                    SPALTE_PASSWORT + " NVARCHAR NOT NULL, " +
                    SPALTE_ROLLE + " NVARCHAR NOT NULL, " +
                    SPALTE_ERSTELLDATUM + " DATETIME DEFAULT CURRENT_TIMESTAMP);";


    public DatenbankUser(Context context) {
        /*
         * Ruft Konstruktor der DatenbankRollen-Elternklasse auf
         * (auf dieser ist die SQLiteObenHelpter-Klasse)
         */
        super(context, DB_NAME, null, DB_VERSION);
        Log.e(TAG, "DbHelfer hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }


    /**
     * Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
     * !Sollte die App deinstalliert werden wird die DB auch entfernt!
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            /*
             * Die Tabelle wird erstellt und die SQL-Datenbank eingefügt
             * Damit wird mittels "execSQL()" dem SQL-DB-Objekt
             * das SQL-Kommando zur Ausfuehrung uebergeben
             */
            Log.e(TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        } catch (Exception ex) {
            Log.e(TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}