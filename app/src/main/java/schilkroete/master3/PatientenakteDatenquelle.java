package schilkroete.master3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Schilkroete on 16.02.2017.
 */

/**
 * Regelt alle Datenbankzugriffe, ob lesend oder schreibend
 */
public class PatientenakteDatenquelle {

    private static final String LOG_TAG = PatientenakteDatenquelle.class.getSimpleName();

    // Membervariablen
    private SQLiteDatabase datenbank;
    private PatientenakteDatenbankManager dbHelfer;

    // Das Spalten-Array wird fuer spaetere Suchanfragen verwendet
    private String[] columns = {
            PatientenakteDatenbankManager.SPALTE_VORNAME,
            PatientenakteDatenbankManager.SPALTE_NACHNAME,
            PatientenakteDatenbankManager.SPALTE_GEBURTSDATUM,
            PatientenakteDatenbankManager.SPALTE_BESCHWERDEN,
            PatientenakteDatenbankManager.SPALTE_MEDIKAMENTE,
            PatientenakteDatenbankManager.SPALTE_NOTIZEN
    };

    /**
     * Erzeugt eine Instanz von der PatientenakteDatenbankManager-Klasse
     * Context Übergabe - Umgebung in der die App ausgeführt wird
     */
    public PatientenakteDatenquelle(Context context){
        Log.e(LOG_TAG, "Unsere Datenquelle erzeugt jetzt den dbHelfer.");
        dbHelfer = new PatientenakteDatenbankManager(context);
    }


    /**
     * Verbindung yur Datenbank wird hier geöffnet
     */
    public void open() {
        Log.e(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        datenbank = dbHelfer.getWritableDatabase();
        Log.e(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + datenbank.getPath());
    }


    /**
     * Verbindung zur Datenbank wird hier geschlossen
     */
    public void close() {
        dbHelfer.close();
        Log.e(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }


/*    public erstellePatientenakte(
            String vorname, String nachname, String geburtsdatum,
            String beschreibung, String medikamente, String notizen){
        ContentValues values = new ContentValues();
        values.put(PatientenakteDatenbankManager.SPALTE_VORNAME, vorname);
        values.put(PatientenakteDatenbankManager.SPALTE_NACHNAME, nachname);
        values.put(PatientenakteDatenbankManager.SPALTE_GEBURTSDATUM, geburtsdatum);
        values.put(PatientenakteDatenbankManager.SPALTE_BESCHWERDEN, beschreibung);
        values.put(PatientenakteDatenbankManager.SPALTE_MEDIKAMENTE, medikamente);
        values.put(PatientenakteDatenbankManager.SPALTE_NOTIZEN, notizen);

        long einuegenId = datenbank.insert(PatientenakteDatenbankManager.TABELLEN_NAME, null, values);

        Cursor cursor = datenbank.query(PatientenakteDatenbankManager.TABELLEN_NAME, columns,
                PatientenakteDatenbankManager.SPALTE_ID + "=" + einuegenId, null, null, null,null);

        cursor.moveToFirst();

    }*/





}
