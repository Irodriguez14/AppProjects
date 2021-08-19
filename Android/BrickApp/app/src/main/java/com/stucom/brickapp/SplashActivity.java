package com.stucom.brickapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.stucom.brickapp.model.LegoSetsApiResponse;
import com.stucom.brickapp.model.LegoThemes;
import com.stucom.brickapp.model.LegoThemesApiResponse;

public class SplashActivity extends AppCompatActivity implements Runnable {

    TextView tvCurrentStep;

    Handler handler;
    int stepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvCurrentStep = findViewById(R.id.tvCurrentStep);

        // Program and fire the handler immediately
        handler = new Handler();
        stepCounter = 0;
        handler.postDelayed(this, 0);
    }

    @Override
    public void run() {
        if (stepCounter == 0) {
            // First step: only text
            tvCurrentStep.setText(R.string.lbl_step_initializing);
            stepCounter++;
            handler.postDelayed(this, 1000);
        }
        else if (stepCounter == 1) {
            // Second step: only text
            tvCurrentStep.setText(R.string.lbl_step_checking_api_key);
            stepCounter++;
            handler.postDelayed(this, 1000);
        }
        else if (stepCounter == 2) {
            // Third step: checking the API key
            SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            String apiKey = prefs.getString("apiKey", "");
            Log.d("flx", "API Key = " + apiKey);
            if (apiKey.isEmpty()) {
                // The API key is not working properly or there is no network connection
                Intent intent = new Intent(this, EnterApiKeyActivity.class);
                finish();
                startActivity(intent);
            }
            else {
                stepCounter++;
                handler.postDelayed(this, 1000);
            }
        }
        else if (stepCounter == 3) {
            // Fourth step: download theme list
            tvCurrentStep.setText(R.string.lbl_step_downloading_themes);
            SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            String apiKey = prefs.getString("apiKey", "");
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://rebrickable.com/api/v3/lego/themes?" +
                    "key=" + apiKey + "&" +
                    "page_size=5000" + "&" +
                    "ordering=name";
            StringRequest request = new StringRequest(
            Request.Method.GET,
            url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    LegoThemesApiResponse legoThemesApiResponse = gson.fromJson(response, LegoThemesApiResponse.class);
                    LegoThemes themes = legoThemesApiResponse.getFirstLevelThemes();
                    String themesJson = gson.toJson(themes);
                    SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("themesJson", themesJson);
                    editor.apply();
                    stepCounter++;
                    handler.postDelayed(SplashActivity.this, 1000);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String msg = "Network error (timeout or disconnected)";
                    if (error.networkResponse != null) {
                        msg = "ERROR: " + error.networkResponse.statusCode;
                    }
                    Log.d("flx", msg);
                    // TODO Decide what to do in this case!
                    stepCounter++;
                    handler.postDelayed(SplashActivity.this, 1000);
                }
            });
            queue.add(request);
        }
        else {
            // All went well, continue to the main activity
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        // Replace the current activity so the back button
        // will exit the app after loading the new activity
    }
}