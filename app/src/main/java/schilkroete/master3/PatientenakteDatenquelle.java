package schilkroete.master3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schilkroete on 16.02.2017.
 */

/**
 * Regelt alle Datenbankzugriffe, ob lesend oder schreibend
 */
public class PatientenakteDatenquelle {

    private static final String TAG = PatientenakteDatenquelle.class.getSimpleName();

    private SQLiteDatabase datenbank;
    private PatientenakteDatenbankManager dbHelfer;

    private String[] spaltenArray = {
            PatientenakteDatenbankManager.SPALTE_ID,
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
        Log.e(TAG, "Unsere Datenquelle erzeugt jetzt den dbHelfer.");
        dbHelfer = new PatientenakteDatenbankManager(context);
    }


    /**
     * Verbindung yur Datenbank wird hier geöffnet
     */
    public void open() {
        Log.e(TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        datenbank = dbHelfer.getWritableDatabase();
        Log.e(TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + datenbank.getPath());
    }


    /**
     * Verbindung zur Datenbank wird hier geschlossen
     */
    public void close() {
        dbHelfer.close();
        Log.e(TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }


    public Patientenakte erstellePatientenakte(
            String vorname, String nachname, String geburtsdatum,
            String beschreibung, String medikamente, String notizen){
        ContentValues values = new ContentValues();
        values.put(PatientenakteDatenbankManager.SPALTE_VORNAME, vorname);
        values.put(PatientenakteDatenbankManager.SPALTE_NACHNAME, nachname);
        values.put(PatientenakteDatenbankManager.SPALTE_GEBURTSDATUM, geburtsdatum);
        values.put(PatientenakteDatenbankManager.SPALTE_BESCHWERDEN, beschreibung);
        values.put(PatientenakteDatenbankManager.SPALTE_MEDIKAMENTE, medikamente);
        values.put(PatientenakteDatenbankManager.SPALTE_NOTIZEN, notizen);

        long einfuegenId = datenbank.insert(PatientenakteDatenbankManager.TABELLEN_NAME, null, values);

        Cursor cursor = datenbank.query(PatientenakteDatenbankManager.TABELLEN_NAME, spaltenArray,
                PatientenakteDatenbankManager.SPALTE_ID + "=" + einfuegenId, null, null, null,null);

        cursor.moveToFirst();
        Patientenakte patientenakte = cursorZuPatientenakte(cursor);
        cursor.close();

        return patientenakte;
    }

    /**
     * Diese Methode wird verwendert, um Datensätze in Patientenakten umzuwandeln.
     * @param cursor
     * @return
     */
    private Patientenakte cursorZuPatientenakte(Cursor cursor){
        int idIndex = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_ID);
        int idVorname = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_VORNAME);
        int idNachname = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_NACHNAME);
        int idGeburtsdatum = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_GEBURTSDATUM);
        int idBeschwerden = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_BESCHWERDEN);
        int idNotizen = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_NOTIZEN);
        int idMedikamente = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_MEDIKAMENTE);

        long id = cursor.getLong(idIndex);
        String vorname = cursor.getString(idVorname);
        String nachname = cursor.getString(idNachname);
        String geburtsdatum = cursor.getString(idGeburtsdatum);
        String medikamente = cursor.getString(idMedikamente);
        String beschwerden = cursor.getString(idBeschwerden);
        String notizen = cursor.getString(idNotizen);

        Patientenakte patientenakte = new Patientenakte(vorname, nachname, geburtsdatum, medikamente,
                beschwerden, notizen, id);

        return patientenakte;
    }

    public void loeschePatientenakte (Patientenakte patientenakte){
        long id = patientenakte.getId();

        datenbank.delete(PatientenakteDatenbankManager.TABELLEN_NAME,
                PatientenakteDatenbankManager.SPALTE_ID + "=" + id, null);

        Log.e(TAG, "Eintrag gelöscht ID: " + id + " Inhalt: " + patientenakte.toString());
    }


    public List<Patientenakte> gibAllePatientenakten(){
        List<Patientenakte> patientenakteListe = new ArrayList<>();

        Cursor cursor = datenbank.query(PatientenakteDatenbankManager.TABELLEN_NAME,
                spaltenArray, null, null, null, null, null);
        cursor.moveToFirst();
        Patientenakte patientenakte;

        while(!cursor.isAfterLast()){
            patientenakte = cursorZuPatientenakte(cursor);
            patientenakteListe.add(patientenakte);
            Log.e(TAG, "ID: " + patientenakte.getId() + ", Inhalt: " + patientenakte.toString());
            cursor.moveToNext();
        }
        cursor.close();

        return patientenakteListe;
    }



}
