package schilkroete.healthy.activitys;

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

import schilkroete.healthy.R;
import schilkroete.healthy.datenbankzugriffe.DatenquelleUser;
import schilkroete.healthy.datenbankzugriffe.User;

/**
 * Created by Schil on 10.04.2017.
 */

public class ActivityUserSuchen extends Activity {

    public static final String TAG = ActivityUserSuchen.class.getSimpleName();

    private DatenquelleUser datenquelleUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_usersuche);

        Log.e(TAG, "Das Datenquellen-Objekt wird angelegt.");
        datenquelleUser = new DatenquelleUser(this);

        initializeContextualActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "Die Datenquelle wird geöffnet.");
        datenquelleUser.open();

        Log.e(TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        zeigeAlleEintraege();
    }


    private void zeigeAlleEintraege() {
        List<User> userListe = datenquelleUser.gibAlleUser();
        ArrayAdapter<User> userArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                userListe);
        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_userliste);
        shoppingMemosListView.setAdapter(userArrayAdapter);
    }


    private void initializeContextualActionBar() {
        final ListView userListView = (ListView) findViewById(R.id.listview_userliste);
        userListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        userListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

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
                                userListView.getCheckedItemPositions();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                User user = (User)
                                        userListView.getItemAtPosition(postitionInListView);
                                Log.e(TAG, "Position im ListView: " + postitionInListView
                                        + " Inhalt: " + user.toString());
                                datenquelleUser.loescheUser(user);
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