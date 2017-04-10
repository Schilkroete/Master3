package schilkroete.healthy.datenbankzugriffe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import schilkroete.healthy.datenbanken.DatenbankUser;

/**
 * Regelt alle Datenbankzugriffe, ob lesend oder schreibend
 */
public class DatenquelleUser {

    private static final String TAG = DatenquelleUser.class.getSimpleName();

    private SQLiteDatabase datenbankUser;
    private DatenbankUser dbHelferUser;

    private String[] spaltenArray = {
            DatenbankUser.SPALTE_ID_USER,
            DatenbankUser.SPALTE_VORNAME,
            DatenbankUser.SPALTE_NACHNAME,
            DatenbankUser.SPALTE_PASSWORT,
            DatenbankUser.SPALTE_ROLLE,
            DatenbankUser.SPALTE_ERSTELLDATUM
    };

    /**
     * Erzeugt eine Instanz von der DatenbankRollen-Klasse
     * Context Übergabe - Umgebung in der die App ausgeführt wird
     */
    public DatenquelleUser(Context context){
        Log.e(TAG, "Unsere Datenquelle erzeugt jetzt den dbHelferUser.");
        dbHelferUser = new DatenbankUser(context);
    }


    /**
     * Verbindung yur Datenbank wird hier geöffnet
     */
    public void open() {
        Log.e(TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        datenbankUser = dbHelferUser.getWritableDatabase();
        Log.e(TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + datenbankUser.getPath());
    }


    /**
     * Verbindung zur Datenbank wird hier geschlossen
     */
    public void close() {
        dbHelferUser.close();
        Log.e(TAG, "Datenbank mit Hilfe des DbHelfers geschlossen.");
    }


    // Mit dieser Methode können Datensätze in die Tabelle der SQLite Datenbank eingefügt werden
    public User erstelleUser(
            String vorname, String nachname, String passwort,
            String rolle, String datumZeit){
        // Hier wird ein ContentValue-Objekt erzeigt
        ContentValues alleWerte = new ContentValues();
        alleWerte.put(DatenbankUser.SPALTE_VORNAME, vorname);
        alleWerte.put(DatenbankUser.SPALTE_NACHNAME, nachname);
        alleWerte.put(DatenbankUser.SPALTE_PASSWORT, passwort);
        alleWerte.put(DatenbankUser.SPALTE_ROLLE, rolle);
        alleWerte.put(DatenbankUser.SPALTE_ERSTELLDATUM, datumZeit);

         /*
          * Hier werden die Werte mit Hilfe des ContentValues-Objekt in die Tabelle eingetragen.
          * Dazu wird der insert-Befehl verwendet, den wir auf dem SQLiteDatabase-Objekt ausfuehren
          * Als Argument wird der Name der Tabelle uebergeben, null fuer den ColumnHack und das vorbereitete
          * ContentValue-Objekt. Wenn das Einfuegen erfolgreich war, dann erhalten wir die ID des
          * erstelllten Datensatzes zurueck.
          */
        long einfuegenId = datenbankUser.insert(DatenbankUser.TABELLEN_NAME, null, alleWerte);
        /*
         * Wir lesen die eingegebenen Werte zur Kontrolle, mit dieser Anweisung, aus
         * Als Argument uebergeben wir den Namen der Tabelle, den Spaleten-Array (die Suchanfrage
         * soll die Werte fuer alle Spalten zurueckliefern)
         * und den Such-String mit dem wir nach dem eingefuegten Datensatz suchen.
         */
        Cursor cursor = datenbankUser.query(DatenbankUser.TABELLEN_NAME, spaltenArray,
                DatenbankUser.SPALTE_ID_USER + "=" + einfuegenId, null, null, null,null);
        /*
         * Mit dieser Anweisung bewegen wir den Cursor an die Position seines ersten Datensatzes.
         * Anschließend wird die cursorToShoppingMemo() Methode aufgerufen und wandelt dadurch den
         * Datensatz des Cursor-Objekts in ein ShoppingMemo-Objekt um.
         */
        cursor.moveToFirst();
        User user = cursorZuUser(cursor);
        cursor.close();
        // Das so erzeugte User-Objekt geben wir an die aufrufende Methode zurück
        return user;
    }

    /**
     * Diese Methode wird verwendert, um Datensätze in User umzuwandeln.
     * @param cursor
     * @return
     */
    private User cursorZuUser(Cursor cursor){
        int idIndex = cursor.getColumnIndex(DatenbankUser.SPALTE_ID_USER);
        int idVorname = cursor.getColumnIndex(DatenbankUser.SPALTE_VORNAME);
        int idNachname = cursor.getColumnIndex(DatenbankUser.SPALTE_NACHNAME);
        int idPasswort = cursor.getColumnIndex(DatenbankUser.SPALTE_PASSWORT);
        int idRolle = cursor.getColumnIndex(DatenbankUser.SPALTE_ROLLE);
        int idErstelldatum= cursor.getColumnIndex(DatenbankUser.SPALTE_ERSTELLDATUM);

        long id = cursor.getLong(idIndex);
        String vorname = cursor.getString(idVorname);
        String nachname = cursor.getString(idNachname);
        String passwort = cursor.getString(idPasswort);
        String rolle = cursor.getString(idRolle);
        String erstelldatum = cursor.getString(idErstelldatum);

        User user = new User (vorname, nachname, passwort,
                rolle, erstelldatum, id);

        return user;
    }

    // Die ID des zu löschenden Datensatzes lesen wir aus dem übergebenen User-Objekt
    public void loescheUser (User user){
        // Anschließend führen wir die Lösch-Operation auf dem SQLiteDatenbank-Objekt aus.
        long id = user.getId();

        datenbankUser.delete(DatenbankUser.TABELLEN_NAME,
                DatenbankUser.SPALTE_ID_USER + "=" + id, null);

        Log.e(TAG, "Eintrag gelöscht ID: " + id + " Inhalt: " + user.toString());
    }

    /**
     * Mit dieser Methode werden alle vorhandenen Datensätze aus der Tabelle unserer SQLite
     * Datenbank auslesen. Dazu erzeugen wir gleich zu Beginn der Methode eine Liste,
     * die User-Objekt in sich aufnehmen kann.
     * @return Die erzeugte User-Liste geben wir zurück, die alle Datensätze der Tabelle enthält
     */
    public List<User> gibAlleUser(){
        List<User> userListe = new ArrayList<>();
        /*
         * Anschließend wird eine Suchanfrage gestartet. Diesmal übergeben wir als Argumente
         * nur den Namen der Tabelle und den Spalten-Array. Alle anderen Argumente sind null,
         * d.h. auch der Such-String ist null, wodurch alle in der Tabelle existierenden Datensätze
         * als Ergebnis zurückgeliefert werden.
         */
        Cursor cursor = datenbankUser.query(DatenbankUser.TABELLEN_NAME,
                spaltenArray, null, null, null, null, null);
        cursor.moveToFirst();
        User user;
        /*
         * Hiermit lesen wir alle Datensätze der Suchanfrage aus, wandeln sie in User-Objekte um
         * und fügen sie der User-Liste hinzu. Mit der LOG-Meldung können wir überprüfen,
         * welche Datensätze sich in der Tabelle befinden.
         */
        while(!cursor.isAfterLast()){
            user = cursorZuUser(cursor);
            userListe.add(user);
            Log.e(TAG, "ID: " + user.getId() + ", Inhalt: " + user.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return userListe;
    }


}