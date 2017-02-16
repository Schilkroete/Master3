package schilkroete.master3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Schilkroete on 02.10.2016.
 */

    /*Die eigentliche Arbeit wird von der ShoppingMemoDataSource-Klasse übernommen, sie regelt alle
    * Datenbankzugriffe, ob lesend oder schreibend

    //TODO Evtl. in der Doku verwenden
    * Die Arbeiterklasse. Sie ist für alle Datenbankzugriffe verantwortlich.
    * Mit ihrer Hilfe schreiben wir Datensätze in die Tabelle unserer SQLDatenbank und
    * lesen diese auch wieder aus.

    * Die ShoppingMemoDataSource-Klasse besitzt eine Membervariable vom Datentyp SQLiteDatabase,
    * in der die Datenbank-Objekte abspeichern werden.
    * Dadurch hält die ShoppingMemoDataSource-Klasse die Verbindung zu Datenbank aufrecht.
    */

public class ShoppingMemoDataSource { //DatenquellePatienten

    private static final String LOG_TAG = ShoppingMemoDataSource.class.getSimpleName();
    // Membervariablen
    private SQLiteDatabase database;
    private ShoppingMemoDbHelper dbHelper;

    //* Das Spalten-Array wird fuer spaetere Suchanfragen verwendet
    private String[] columns = {
            ShoppingMemoDbHelper.COLUMN_ID,
            ShoppingMemoDbHelper.COLUMN_PRODUCT,
            ShoppingMemoDbHelper.COLUMN_QUANTITY
    };

    // In diesem Konstruktor wird eine ShoppingMemoDbHelper-Instanz erzeugt
    // und es wird ihr der Context übergeben, also die Umgebung in der unsere App ausgeführt wird.
    public ShoppingMemoDataSource(Context context) {
        Log.e(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ShoppingMemoDbHelper(context);
    }



    public void open() {
        Log.e(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.e(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }



    // Verbindung zur Datenbank wird hier geschlossen
    public void close() {
        dbHelper.close();
        Log.e(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }



    // Mit dieser Methode koennen Datensaetze in die Tabelle der SQLite Datenbank eingefuegt werden
    public ShoppingMemo createShoppingMemo(String product, int quantity) {
        ContentValues values = new ContentValues();
        // In der Methode wird ein ContentValues-Objekt erzeugt, in das der Produktnamen und
        // die Menge einfügt werden.
        values.put(ShoppingMemoDbHelper.COLUMN_PRODUCT, product);
        values.put(ShoppingMemoDbHelper.COLUMN_QUANTITY, quantity);
        /* Hier werden die Werte mit Hilfe des ContentValues-Objekt in die Tabelle eingetragen.
         * Dazu wird der insert-Befehl verwendet, den wir auf dem SQLiteDatabase-Objekt ausfuehren
         * Als Argument wird der Name der Tabelle uebergeben, null fuer den ColumnHack und das vorbereitete
         * ContentValue-Objekt. Wenn das Einfuegen erfolgreich war, dann erhalten wir die ID des
         * erstelllten Datensatzes zurueck.
        */
        long insertId = database.insert(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, null, values);
        /* Wir lesen die eingegebenen Werte zur Kontrolle, mit dieser Anweisung, aus
         * Als Argument uebergeben wir den Namen der Tabelle, den Spaleten-Array (die Suchanfrage
         * soll die Werte fuer alle Spalten zurueckliefern)
         * und den Such-String mit dem wir nach dem eingefuegten Datensatz suchen.
         */
        Cursor cursor = database.query(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,
                columns, ShoppingMemoDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);
        /* Mit dieser Anweisung bewegen wir den Cursor an die Position seines ersten Datensatzes.
         * Anschließend wird die cursorToShoppingMemo() Methode aufgerufen und wandelt dadurch den
         * Datensatz des Cursor-Objekts in ein ShoppingMemo-Objekt um.
         */
        cursor.moveToFirst();
        ShoppingMemo shoppingMemo = cursorToShoppingMemo(cursor);
        // Wir schließen noch das Cursor-Objekt
        cursor.close();
        // Das so erzeugte ShoppingMemo-Objekt geben wir an die aufrufende Methode zurück.
        return shoppingMemo;
    }



    // Die ID des zu löschenden Datensatzes lesen wir aus dem übergebenen ShoppingMemo-Objekt
    public void deleteShoppingMemo(ShoppingMemo shoppingMemo) {
        // Anschließend führen wir die Lösch-Operation auf dem SQLiteDatabase-Objekt aus.
        long id = shoppingMemo.getId();

        database.delete(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,
                ShoppingMemoDbHelper.COLUMN_ID + "=" + id,
                null);
        // zu Testzwecken wird die Log-Meldung ausgegeben TODO evtl loeschen, mal sehen
        Log.e(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + shoppingMemo.toString());
    }



    // Diese Methode wird verwendet, um Datensätze aus der Datenbank in ShoppingMemos umzuwandeln.
    private ShoppingMemo cursorToShoppingMemo (Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID);
        int idProduct = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_PRODUCT);
        int idQuantity = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_QUANTITY);

        String product = cursor.getString(idProduct);
        int quantity = cursor.getInt(idQuantity);
        long id = cursor.getLong(idIndex);

        ShoppingMemo shoppingMemo = new ShoppingMemo(product, quantity, id);

        return shoppingMemo;
    }


    /* Mit dieser Methode werden alle vorhandenen Datensätze aus der Tabelle unserer SQLite
     * Datenbank auslesen. Dazu erzeugen wir gleich zu Beginn der Methode eine Liste,
     * die ShoppingMemo-Objekt in sich aufnehmen kann.
     */
    public List<ShoppingMemo> getAllShoppingMemos() {
        List<ShoppingMemo> shoppingMemoList = new ArrayList<>();

        /* Anschließend wird eine Suchanfrage gestartet. Diesmal übergeben wir als Argumente
         * nur den Namen der Tabelle und den Spalten-Array. Alle anderen Argumente sind null,
         * d.h. auch der Such-String ist null, wodurch alle in der Tabelle existierenden Datensätze
         * als Ergebnis zurückgeliefert werden.
         */
        Cursor cursor = database.query(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST,
                columns, null, null, null, null, null);
        // Hier wird das erhaltenen Cursor-Objekt an seine erste Position gesetzen...
        cursor.moveToFirst();
        // ... und es wird eine Variable vom Typ ShoppingMemo deklariert.
        ShoppingMemo shoppingMemo;

        /* Hiermit lesen wir alle Datensatze der Suchanfrage aus, wandeln sie in Shopping-Objekte um
        * und fuegen sie der ShoppingMemo-Liste hinzu. Mit der LOG-Meldung koennen wir ueberpruefen,
        * welche Datensaetze sich in der Tabelle befinden.
        */
        while(!cursor.isAfterLast()) {
            shoppingMemo = cursorToShoppingMemo(cursor);
            shoppingMemoList.add(shoppingMemo);
            Log.e(LOG_TAG, "ID: " + shoppingMemo.getId() + ", Inhalt: " + shoppingMemo.toString());
            cursor.moveToNext();
        }
        // Cursor wird wieder geschlossen... ganz wichtig und darf ncht vergessen werden
        cursor.close();
        // Die erzeugte ShoppingMemo-Liste geben wir zurueck, die alle Datensatzen der Tabelle enthaelt
        return shoppingMemoList;
    }

}