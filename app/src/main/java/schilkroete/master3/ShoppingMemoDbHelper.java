package schilkroete.master3;

/**
 * Created by Schilkroete on 02.10.2016.
 */

// Die ShoppingMemoDbHelper-Klasse hilft uns beim Erstellen und Aktualisieren der Datenbank.

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ShoppingMemoDbHelper extends SQLiteOpenHelper{ //DatenbankManagerPatienten

    private static final String LOG_TAG = ShoppingMemoDbHelper.class.getSimpleName();

    private static final String DB_NAME = "shopping_list.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_SHOPPING_LIST = "shopping_list";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT = "product";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String SQL_CREATE =
            "CREATE TABLE " +
                    TABLE_SHOPPING_LIST +
                    "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL);";



    public ShoppingMemoDbHelper(Context context) {
//super(context, "PLATZHALTER_DATENBANKNAME", null, 1);
// wurde vorerst auskommentiert ggf. loeschen
        /* Ruft Konstruktor der ShoppingMemoDbHelper-Elternklasse auf
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
         */
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}