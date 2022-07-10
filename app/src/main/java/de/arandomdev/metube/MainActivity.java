package de.arandomdev.metube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String[] formats;
    private HashMap<String, String[]> qualities;
    private AutoCompleteTextView formatMenu, qualityMenu;
    private Button downloadButton;
    private EditText urlInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if(Intent.ACTION_SEND.equals(intent.getAction())) {
            Log.d("MeTube", "Shared: "+intent.getStringExtra(Intent.EXTRA_TEXT));
        }

        formats = new String[] {"MP4", "MP3", "Thumbnail"};
        qualities = new HashMap<>();
        qualities.put("MP4", new String[] {"Best", "1440p", "1080p", "720p", "480p"});
        qualities.put("MP3", new String[] {"Best", "320 kbps", "192 kbps", "128 kbps"});
        qualities.put("Thumbnail", new String[] {"Best"});

        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settings.edit();

        urlInput = findViewById(R.id.urlText);

        qualityMenu = findViewById(R.id.qualityMenu);
        qualityMenu.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedQuality = adapterView.getItemAtPosition(position).toString();

            settingsEditor.putString("lastQuality", selectedQuality);
            settingsEditor.apply();
        });

        formatMenu = findViewById(R.id.formatMenu);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, formats);
        formatMenu.setAdapter(adapter);
        formatMenu.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedFormat = adapterView.getItemAtPosition(position).toString();

            settingsEditor.putString("lastFormat", selectedFormat);
            settingsEditor.putString("lastQuality", "Best");
            settingsEditor.apply();

            populateQualityMenu(selectedFormat, "Best");
        });

        String savedFormat = settings.getString("lastFormat", "MP4");
        String savedQuality = settings.getString("lastQuality", "Best");
        formatMenu.setText(savedFormat, false);
        populateQualityMenu(savedFormat, savedQuality);

        downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(view -> {
            String format = formatMenu.getText().toString();
            String quality = qualityMenu.getText().toString();

            urlInput.setText("");
            Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show();
        });
    }

    private void populateQualityMenu(String format, String selected) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, qualities.get(format));
        qualityMenu.setAdapter(adapter);
        qualityMenu.setText(selected, false);
    }
}