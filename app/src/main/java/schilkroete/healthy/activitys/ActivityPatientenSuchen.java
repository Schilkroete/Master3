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

import schilkroete.healthy.datenbankzugriffe.Patientenakte;
import schilkroete.healthy.datenbankzugriffe.DatenquellePatientenakte;
import schilkroete.healthy.R;


public class ActivityPatientenSuchen extends Activity {

    public static final String TAG = ActivityPatientenSuchen.class.getSimpleName();

    private DatenquellePatientenakte datenquelleAkte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patientensuche);

        Log.e(TAG, "Das Datenquellen-Objekt wird angelegt.");
        datenquelleAkte = new DatenquellePatientenakte(this);

        initializeContextualActionBar();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "Die Datenquelle wird geöffnet.");
        datenquelleAkte.open();

        Log.e(TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        zeigeAlleEintraege();
    }


    @Override
    protected void onPause() {
        super.onPause();

        Log.e(TAG, "Die Datenquelle wird geschlossen.");
        datenquelleAkte.close();
    }


    private void zeigeAlleEintraege() {
        List<Patientenakte> patientenakteListe = datenquelleAkte.gibAllePatientenakten();
        ArrayAdapter<Patientenakte> patientenakteArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                patientenakteListe);
        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_patientenakte);
        shoppingMemosListView.setAdapter(patientenakteArrayAdapter);
    }


    private void initializeContextualActionBar() {
        final ListView patientenakteListView = (ListView) findViewById(R.id.listview_patientenakte);
        patientenakteListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        patientenakteListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

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
                                patientenakteListView.getCheckedItemPositions();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                Patientenakte patientenakte = (Patientenakte)
                                        patientenakteListView.getItemAtPosition(postitionInListView);
                                Log.e(TAG, "Position im ListView: " + postitionInListView
                                        + " Inhalt: " + patientenakte.toString());
                                datenquelleAkte.loeschePatientenakte(patientenakte);
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