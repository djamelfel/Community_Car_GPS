package djamelfel.communitycargps;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Anaelle on 18/10/2015.
 */
public class DisplaySettings extends Activity implements View.OnClickListener{

    private EditText distance;
    private EditText timeLimite;
    private EditText password;

    private Settings settings;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);

        distance = (EditText) findViewById(R.id.distance);
        timeLimite = (EditText) findViewById(R.id.timeLimite);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.bReset).setOnClickListener(this);
        findViewById(R.id.bSave).setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        settings = extras.getParcelable("settings");
    }

    private void saveSettings() {
        if (!distance.getText().toString().isEmpty())
            settings.setDistance(Integer.parseInt(distance.getText().toString()));
        if (!timeLimite.getText().toString().isEmpty())
            settings.setTimeLimite(Integer.parseInt(timeLimite.getText().toString()));
        if (!password.getText().toString().isEmpty()) {
            //TODO: Request database
        }
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bReset:
                distance.setText("");
                timeLimite.setText("");
                password.setText("");
                break;
            case R.id.bSave:
                saveSettings();
                Intent intent = new Intent(DisplaySettings.this, Maps.class);
                intent.putExtra("settings", settings);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DisplaySettings.this, Maps.class);
        intent.putExtra("settings", settings);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DisplaySettings.this, Maps.class);
                intent.putExtra("settings", settings);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
