package schilkroete.master3;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Schilkroete on 03.02.2017.
 */

public class ActivityPatientenSuchen extends Activity {
/*

    private DatenbankManagerPatienten mHelper;
    private SQLiteDatabase mDatenbank;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patientensuche);

       /* mHelper = new DatenbankManagerPatienten(this);*/
    }

/*

    @Override
    protected void onPause() {
        super.onPause();
        // Pause wird nie betreten bevor onResume durchlaufen wurde, also kann ich mir eine
        // Überprüfung, ob die Datenbank offen ist, sparen
        mDatenbank.close();
        Toast.makeText(this, "Datenbank geschlossen", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // zum öffnen der Datenbank
        mDatenbank = mHelper.getReadableDatabase();
        Toast.makeText(this, "Datenbank geöffnet", Toast.LENGTH_LONG).show();
*/
/*
        Cursor patientenCursor = mDatenbank.rawQuery(DatenbankManagerPatienten.PATIENTENAKTE_SELECT_RAW, null);
        startManagingCursor(patientenCursor);

        SimpleCursorAdapter patientenAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                patientenCursor,
                new String[]{"vorname"},
                new int[] {android.R.id.text1}
        );
        setListAdapter(patientenAdapter);*//*

    }


*/


}