package schilkroete.master3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

/**
 * Created by Schilkroete on 03.02.2017.
 */

public class ActivityPatientenSuchen extends Activity {

    public static final String LOG_TAG = ActivityPatientenSuchen.class.getSimpleName();

    private ShoppingMemoDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patientensuche);

        Log.e(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new ShoppingMemoDataSource(this);

        initializeContextualActionBar();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.e(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.e(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        zeigeAlleEintraege();
    }


    @Override
    protected void onPause() {
        super.onPause();

        Log.e(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }


    private void zeigeAlleEintraege() {
        List<ShoppingMemo> shoppingMemoList = dataSource.getAllShoppingMemos();
        ArrayAdapter<ShoppingMemo> shoppingMemoArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                shoppingMemoList);
        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_shopping_memos);
        shoppingMemosListView.setAdapter(shoppingMemoArrayAdapter);
    }




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



}