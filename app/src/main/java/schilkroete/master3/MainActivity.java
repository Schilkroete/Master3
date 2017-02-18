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

        aktiviereHinzufuegenButton();
//        initializeContextualActionBar();
    }

/*
    private void zeigeAlleEintraege() {
        List<ShoppingMemo> shoppingMemoList = dataSource.getAllShoppingMemos();
        ArrayAdapter<ShoppingMemo> shoppingMemoArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                shoppingMemoList);
        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_shopping_memos);
        shoppingMemosListView.setAdapter(shoppingMemoArrayAdapter);
    }
*/


    @Override
    protected void onResume() {
        super.onResume();

        Log.e(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.e(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
//        zeigeAlleEintraege();
    }


    @Override
    protected void onPause() {
        super.onPause();

        Log.e(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }



    private void aktiviereHinzufuegenButton() {
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
 //               zeigeAlleEintraege();
            }
        });

    }



/*
    private void initializeContextualActionBar() {
        final ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_shopping_memos);
        shoppingMemosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        shoppingMemosListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }


            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.cab_delete:
                        SparseBooleanArray touchedShoppingMemosPositions = shoppingMemosListView.getCheckedItemPositions();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                ShoppingMemo shoppingMemo = (ShoppingMemo)
                                        shoppingMemosListView.getItemAtPosition(postitionInListView);
                                Log.e(LOG_TAG, "Position im ListView: " + postitionInListView +
                                        " Inhalt: " + shoppingMemo.toString());
                                dataSource.deleteShoppingMemo(shoppingMemo);
                            }
                        }
 //                       zeigeAlleEintraege();
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
*/














/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}