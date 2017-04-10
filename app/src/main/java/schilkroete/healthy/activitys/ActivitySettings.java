package schilkroete.healthy.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import schilkroete.healthy.R;

/**
 * Created by Schil on 10.04.2017.
 */

public class ActivitySettings extends Activity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        btn = (Button) findViewById(R.id.btn_user_anlegen);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {

                Intent userHinzufuegen = new Intent(ActivitySettings.this,
                        ActivityUserAnlegen.class);
                startActivity(userHinzufuegen);

            }
        });

        btn = (Button) findViewById(R.id.btn_usersuche);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {

                Intent usersuche = new Intent(ActivitySettings.this,
                        ActivityUserSuchen.class);
                startActivity(usersuche);

            }
        });

    }
}
