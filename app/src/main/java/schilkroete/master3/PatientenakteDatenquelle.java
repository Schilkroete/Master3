package schilkroete.master3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
            PatientenakteDatenbankManager.SPALTE_NOTIZEN,
            PatientenakteDatenbankManager.SPALTE_ERSTELLDATUM
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


    // Mit dieser Methode können Datensätze in die Tabelle der SQLite Datenbank eingefügt werden
    public Patientenakte erstellePatientenakte(
            String vorname, String nachname, String geburtsdatum,
            String beschreibung, String medikamente, String notizen, String datumZeit){
        // Hier wird ein ContentValue-Objekt erzeigt
        ContentValues alleWerte = new ContentValues();
        alleWerte.put(PatientenakteDatenbankManager.SPALTE_VORNAME, vorname);
        alleWerte.put(PatientenakteDatenbankManager.SPALTE_NACHNAME, nachname);
        alleWerte.put(PatientenakteDatenbankManager.SPALTE_GEBURTSDATUM, geburtsdatum);
        alleWerte.put(PatientenakteDatenbankManager.SPALTE_BESCHWERDEN, beschreibung);
        alleWerte.put(PatientenakteDatenbankManager.SPALTE_MEDIKAMENTE, medikamente);
        alleWerte.put(PatientenakteDatenbankManager.SPALTE_NOTIZEN, notizen);
        alleWerte.put(PatientenakteDatenbankManager.SPALTE_ERSTELLDATUM, datumZeit);

         /*
          * Hier werden die Werte mit Hilfe des ContentValues-Objekt in die Tabelle eingetragen.
          * Dazu wird der insert-Befehl verwendet, den wir auf dem SQLiteDatabase-Objekt ausfuehren
          * Als Argument wird der Name der Tabelle uebergeben, null fuer den ColumnHack und das vorbereitete
          * ContentValue-Objekt. Wenn das Einfuegen erfolgreich war, dann erhalten wir die ID des
          * erstelllten Datensatzes zurueck.
          */
        long einfuegenId = datenbank.insert(PatientenakteDatenbankManager.TABELLEN_NAME, null, alleWerte);
        /*
         * Wir lesen die eingegebenen Werte zur Kontrolle, mit dieser Anweisung, aus
         * Als Argument uebergeben wir den Namen der Tabelle, den Spaleten-Array (die Suchanfrage
         * soll die Werte fuer alle Spalten zurueckliefern)
         * und den Such-String mit dem wir nach dem eingefuegten Datensatz suchen.
         */
        Cursor cursor = datenbank.query(PatientenakteDatenbankManager.TABELLEN_NAME, spaltenArray,
                PatientenakteDatenbankManager.SPALTE_ID + "=" + einfuegenId, null, null, null,null);
        /*
         * Mit dieser Anweisung bewegen wir den Cursor an die Position seines ersten Datensatzes.
         * Anschließend wird die cursorToShoppingMemo() Methode aufgerufen und wandelt dadurch den
         * Datensatz des Cursor-Objekts in ein ShoppingMemo-Objekt um.
         */
        cursor.moveToFirst();
        Patientenakte patientenakte = cursorZuPatientenakte(cursor);
        cursor.close();
        // Das so erzeugte Patientenakte-Objekt geben wir an die aufrufende Methode zurück
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
        int idMedikamente = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_MEDIKAMENTE);
        int idNotizen = cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_NOTIZEN);
        int idErstelldatum= cursor.getColumnIndex(PatientenakteDatenbankManager.SPALTE_ERSTELLDATUM);

        long id = cursor.getLong(idIndex);
        String vorname = cursor.getString(idVorname);
        String nachname = cursor.getString(idNachname);
        String geburtsdatum = cursor.getString(idGeburtsdatum);
        String medikamente = cursor.getString(idMedikamente);
        String beschwerden = cursor.getString(idBeschwerden);
        String notizen = cursor.getString(idNotizen);
        String erstelldatum = cursor.getString(idErstelldatum);

        Patientenakte patientenakte = new Patientenakte(vorname, nachname, geburtsdatum, medikamente,
                beschwerden, notizen, erstelldatum, id);

        return patientenakte;
    }

    // Die ID des zu löschenden Datensatzes lesen wir aus dem übergebenen Patientenakte-Objekt
    public void loeschePatientenakte (Patientenakte patientenakte){
        // Anschließend führen wir die Lösch-Operation auf dem SQLiteDatenbank-Objekt aus.
        long id = patientenakte.getId();

        datenbank.delete(PatientenakteDatenbankManager.TABELLEN_NAME,
                PatientenakteDatenbankManager.SPALTE_ID + "=" + id, null);

        Log.e(TAG, "Eintrag gelöscht ID: " + id + " Inhalt: " + patientenakte.toString());
    }

    /**
     * Mit dieser Methode werden alle vorhandenen Datensätze aus der Tabelle unserer SQLite
     * Datenbank auslesen. Dazu erzeugen wir gleich zu Beginn der Methode eine Liste,
     * die Patientenakte-Objekt in sich aufnehmen kann.
     * @return Die erzeugte Patientenakte-Liste geben wir zurück, die alle Datensätze der Tabelle enthält
     */
    public List<Patientenakte> gibAllePatientenakten(){
        List<Patientenakte> patientenakteListe = new ArrayList<>();
        /*
         * Anschließend wird eine Suchanfrage gestartet. Diesmal übergeben wir als Argumente
         * nur den Namen der Tabelle und den Spalten-Array. Alle anderen Argumente sind null,
         * d.h. auch der Such-String ist null, wodurch alle in der Tabelle existierenden Datensätze
         * als Ergebnis zurückgeliefert werden.
         */
        Cursor cursor = datenbank.query(PatientenakteDatenbankManager.TABELLEN_NAME,
                spaltenArray, null, null, null, null, null);
        cursor.moveToFirst();
        Patientenakte patientenakte;
        /*
         * Hiermit lesen wir alle Datensätze der Suchanfrage aus, wandeln sie in Patienten-Objekte um
         * und fügen sie der Patientenakte-Liste hinzu. Mit der LOG-Meldung können wir überprüfen,
         * welche Datensätze sich in der Tabelle befinden.
         */
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