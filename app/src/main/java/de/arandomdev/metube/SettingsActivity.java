package de.arandomdev.metube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText baseUrlText;
    private CheckBox startAfterShareBox;
    private Button saveButton;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        baseUrlText = findViewById(R.id.baseUrlText);
        startAfterShareBox = findViewById(R.id.startAfterShareBox);
        saveButton = findViewById(R.id.saveButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settings.edit();

        baseUrlText.setText(settings.getString("metubeUrl", ""));
        startAfterShareBox.setChecked(settings.getBoolean("startAfterShare", false));

        saveButton.setOnClickListener(view -> {
            String baseUrl = baseUrlText.getText().toString().trim();
            if(baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length()-1);
            }
            boolean startAfterShare = startAfterShareBox.isChecked();

            settingsEditor.putString("metubeUrl", baseUrl);
            settingsEditor.putBoolean("startAfterShare", startAfterShare);
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