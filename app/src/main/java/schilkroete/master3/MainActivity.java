package schilkroete.master3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.widget.AbsListView;


public class MainActivity extends Activity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ShoppingMemoDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Log.e(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new ShoppingMemoDataSource(this);

        activateAddButton();
        // mit dieser Methode koennen wird Datensaetze loeschen
        initializeContextualActionBar();
    }



    // Diese Methode liest alle vorhandenen Datensätze aus der Tabelle unserer SQLite Datenbank aus
    // und speichert sie in einer Liste als ShoppingMemo-Objekte.
    private void zeigeAlleEintraege() {
        // Hier rufen wir über unsere Datenquelle die Methode getAllShoppingMemos() auf, die uns
        // alle Datenbankeinträge als Liste zurück liefert. Die Einträge dieser Liste sind
        // vom Typ ShoppingMemo.
        List<ShoppingMemo> shoppingMemoList = dataSource.getAllShoppingMemos();
        // Die Liste übergeben wir an einen ArrayAdapter*, der für uns die Verwaltungsarbeit übernimmt.
        ArrayAdapter<ShoppingMemo> shoppingMemoArrayAdapter = new ArrayAdapter<>(
                // Die ausgelesenen Einträge lassen wir jeweils in einem vordefinierten
                // Standardlayout, dem simple_list_item_multiple_choice, anzeigen
                this,
                android.R.layout.simple_list_item_multiple_choice,
                shoppingMemoList);
        // Damit Datenbankeinträge auch auf dem Android Gerät angezeigt werden,
        // binden wir den ArrayAdapter an den ListView der MainActivity.
        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_shopping_memos);
        shoppingMemosListView.setAdapter(shoppingMemoArrayAdapter);
    }


    // Datenquelle wird geoeffnet. Verbindung zur SQLite Datenbank wird hergestellt und
    // alle Inhalte werden im ListView angezeigt
    @Override
    protected void onResume() {
        super.onResume();

        Log.e(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.e(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        zeigeAlleEintraege();
    }


    // Hier schliessen wir dir SQLite Datenbank
    @Override
    protected void onPause() {
        super.onPause();

        Log.e(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }



    private void activateAddButton() {
        Button buttonAddProduct = (Button) findViewById(R.id.button_add_product);
        final EditText editTextQuantity = (EditText) findViewById(R.id.editText_quantity);
        final EditText editTextProduct = (EditText) findViewById(R.id.editText_product);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quantityString = editTextQuantity.getText().toString();
                String product = editTextProduct.getText().toString();

                if(TextUtils.isEmpty(quantityString)) {
                    editTextQuantity.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(product)) {
                    editTextProduct.setError(getString(R.string.editText_errorMessage));
                    return;
                }

                int quantity = Integer.parseInt(quantityString);
                editTextQuantity.setText("");
                editTextProduct.setText("");

                dataSource.createShoppingMemo(product, quantity);

                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                zeigeAlleEintraege();
            }
        });

    }



    private void initializeContextualActionBar() {
        // TODO MainActivityName muss geaendert werden
        // Anfrage einer Referenz zu dem ListView-Objekt der MainActivity
        final ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_shopping_memos);
        // Mit dieser Anweisung koennen wir mehrere Datensaetze markieren/ auswaehlen
        shoppingMemosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        shoppingMemosListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            // Hier wird das Menü der CAB mit dem Action Item gefuellt, der Lösch-Aktion
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            /* Hier wird geprueft welches Action Item angeklickt wurde und dann wird die
             * entsprechenden Aktionen ausgefuehrt. In diesem Fall prüfen wir, ob das Action Item
             * mit der ID cab_delete angeklickt wurde. Wenn das der Fall war,
             * lassen wir uns die Positionen aller berührten Listeneinträge geben. */
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.cab_delete:
                        SparseBooleanArray touchedShoppingMemosPositions =
                                shoppingMemosListView.getCheckedItemPositions();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                ShoppingMemo shoppingMemo = (ShoppingMemo)
                                        shoppingMemosListView.getItemAtPosition(postitionInListView);
                                Log.e(LOG_TAG, "Position im ListView: " + postitionInListView
                                        + " Inhalt: " + shoppingMemo.toString());
                                // Mit dieser Anweisung loeschen wir die Datensaetze aus der Db
                                dataSource.deleteShoppingMemo(shoppingMemo);
                            }
                        }
                        zeigeAlleEintraege();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Zeigt Menü an; fügt Items in die ActionBar, wenn diese aufgerufen wird
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Wir prüfen, ob Menü-Element mit der ID "action_settings" ausgewählt wurde
        int id = item.getItemId();
        // Vereinfachte if-Anweisung
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}