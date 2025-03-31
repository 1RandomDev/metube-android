package de.arandomdev.metube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText baseUrlText, basicAuthUsername, basicAuthPassword;
    private CheckBox startAfterShareBox, enableBasicAuth;
    private Button saveButton;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        baseUrlText = findViewById(R.id.baseUrlText);
        enableBasicAuth = findViewById(R.id.enableBasicAuth);
        basicAuthUsername = findViewById(R.id.basicAuthUsername);
        basicAuthPassword = findViewById(R.id.basicAuthPassword);
        startAfterShareBox = findViewById(R.id.startAfterShareBox);
        saveButton = findViewById(R.id.saveButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settings.edit();

        baseUrlText.setText(settings.getString("metubeUrl", ""));

        boolean basicAuth = settings.getBoolean("enableBasicAuth", false);
        enableBasicAuth.setChecked(basicAuth);
        basicAuthUsername.setEnabled(basicAuth);
        basicAuthPassword.setEnabled(basicAuth);
        basicAuthUsername.setText(settings.getString("basicAuthUsername", ""));
        basicAuthPassword.setText(settings.getString("basicAuthPassword", ""));

        startAfterShareBox.setChecked(settings.getBoolean("startAfterShare", false));

        enableBasicAuth.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            boolean enabled = enableBasicAuth.isChecked();
            basicAuthUsername.setEnabled(enabled);
            basicAuthPassword.setEnabled(enabled);
        });

        saveButton.setOnClickListener(view -> {
            String baseUrl = baseUrlText.getText().toString().trim();
            if(baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length()-1);
            }
            settingsEditor.putString("metubeUrl", baseUrl);

            boolean basicAuth1 = enableBasicAuth.isChecked();
            settingsEditor.putBoolean("enableBasicAuth", basicAuth1);
            settingsEditor.putString("basicAuthUsername", basicAuth1 ? basicAuthUsername.getText().toString() : null);
            settingsEditor.putString("basicAuthPassword", basicAuth1 ? basicAuthPassword.getText().toString() : null);
            basicAuthUsername.setEnabled(basicAuth1);
            basicAuthPassword.setEnabled(basicAuth1);
            if(!basicAuth1) {
                basicAuthUsername.setText("");
                basicAuthPassword.setText("");
            }

            settingsEditor.putBoolean("startAfterShare", startAfterShareBox.isChecked());
            settingsEditor.apply();
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}