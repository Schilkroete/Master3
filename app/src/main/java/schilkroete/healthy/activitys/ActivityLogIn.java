package schilkroete.healthy.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import schilkroete.healthy.R;
import schilkroete.healthy.datenbankzugriffe.DatenquelleUser;

/**
 * Created by Schil on 10.04.2017.
 */

public class ActivityLogIn extends Activity {

    private static final String TAG = ActivityLogIn.class.getSimpleName();

    Button btn;
    private DatenquelleUser datenquelleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        Log.e(TAG, "Das Datenquellen-Objekt wird angelegt.");
        datenquelleUser = new DatenquelleUser(this);

        btn = (Button) findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {

                Intent einloggen = new Intent(ActivityLogIn.this,
                        ActivityDashboard.class);
                startActivity(einloggen);

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Die Datenquelle wird geöffnet.");
        datenquelleUser.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Die Datenquelle wird geschlossen.");
        datenquelleUser.close();
    }



}
