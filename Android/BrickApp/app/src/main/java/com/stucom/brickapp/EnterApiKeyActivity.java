package com.stucom.brickapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterApiKeyActivity extends AppCompatActivity {

    EditText edApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_api_key);

        setTitle(R.string.lbl_enter_api_key_title);

        edApiKey = findViewById(R.id.edApiKey);
        Button btnCheckApiKey = findViewById(R.id.btnCheckApiKey);

        btnCheckApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the API key for a valid format
                String apiKey = edApiKey.getText().toString().toLowerCase().trim();
                if (apiKey.isEmpty()) {
                    edApiKey.setError(getString(R.string.hint_cannot_be_empty));
                    return;
                }
                if (!apiKey.matches("[0-9a-f]{32}")) {
                    edApiKey.setError(getString(R.string.hint_bad_format));
                    return;
                }

                // TODO REAL NETWORK CHECK WILL BE STARTED HERE!!!

                SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("apiKey", apiKey);
                ed.apply();
                // Reset the app by going to the first screen (splash)
                Intent intent = new Intent(EnterApiKeyActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Display the current API key
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String apiKey = prefs.getString("apiKey", "");
        edApiKey.setText(apiKey);
    }
}