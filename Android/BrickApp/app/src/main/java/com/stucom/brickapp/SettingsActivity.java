package com.stucom.brickapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.stucom.brickapp.model.LegoTheme;
import com.stucom.brickapp.model.LegoThemes;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    // Constant value for initial year in the SeekBar views
    public static final int MIN_YEAR = 1960;

    // Tick values for the "parts" SeekBars
    public static final int[] TICK_VALUES = new int[] {
            0, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000
    };

    // All the useful views
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    SwitchMaterial chkFilterByYear, chkFilterByParts;
    LinearLayout groupYear, groupParts;
    SeekBar sbFromYear, sbToYear, sbMinimumParts, sbMaximumParts;
    TextView tvFromYear, tvToYear, tvMinimumParts, tvMaximumParts;
    TextView tvYourApiKey;
    Spinner spTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Change the Activity title
        setTitle(getString(R.string.lbl_settings));

        spTheme = findViewById(R.id.spTheme);

        // Find all the useful views from the layout
        chkFilterByYear = findViewById(R.id.chkFilterByYear);
        groupYear = findViewById(R.id.groupYear);
        sbFromYear = findViewById(R.id.sbFromYear);
        tvFromYear = findViewById(R.id.tvFromYear);
        sbToYear = findViewById(R.id.sbToYear);
        tvToYear = findViewById(R.id.tvToYear);
        chkFilterByParts = findViewById(R.id.chkFilterByParts);
        groupParts = findViewById(R.id.groupParts);
        sbMinimumParts = findViewById(R.id.sbMinimumParts);
        tvMinimumParts = findViewById(R.id.tvMinimumParts);
        sbMaximumParts = findViewById(R.id.sbMaximumParts);
        tvMaximumParts = findViewById(R.id.tvMaximumParts);
        tvYourApiKey = findViewById(R.id.tvYourApiKey);

        // The current year will be the maximum possible value to the SeekBar objects
        int currentYear = (Calendar.getInstance()).get(Calendar.YEAR);
        sbFromYear.setMax(currentYear - MIN_YEAR);
        sbToYear.setMax(currentYear - MIN_YEAR);

        // The number of possible ticks comes from the size of the TICK_VALUES length
        int numberOfTicks = TICK_VALUES.length;
        sbMinimumParts.setMax(numberOfTicks - 2);
        sbMaximumParts.setMax(numberOfTicks - 2);

        // When the switch "Filter by year" is checked or unchecked, update the views below
        chkFilterByYear.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateYearViews(isChecked);
            }
        });
        // Listener for changes in the "From year" SeekBar
        sbFromYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Avoid having the two SeekBars with inverted order
                if (progress > sbToYear.getProgress()) {
                    sbToYear.setProgress(progress);
                }
                updateYearViews(true);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        // Listener for changes in the "To year" SeekBar
        sbToYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Avoid having the two SeekBars with inverted order
                if (progress < sbFromYear.getProgress()) {
                    sbFromYear.setProgress(progress);
                }
                updateYearViews(true);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // When the switch "Filter by parts" is checked or unchecked, update the views below
        chkFilterByParts.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updatePartsViews(isChecked);
            }
        });
        // Listener for changes in the "Minimum parts" SeekBar
        sbMinimumParts.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Avoid having the two SeekBars with inverted order
                if (progress > sbMaximumParts.getProgress()) {
                    sbMaximumParts.setProgress(progress);
                }
                updatePartsViews(true);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        // Listener for changes in the "Maximum parts" SeekBar
        sbMaximumParts.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Avoid having the two SeekBars with inverted order
                if (progress < sbMinimumParts.getProgress()) {
                    sbMinimumParts.setProgress(progress);
                }
                updatePartsViews(true);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // API Key actions
        Button btnChangeApiKey = findViewById(R.id.btnChangeApiKey);
        btnChangeApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the edit activity
                Intent intent = new Intent(SettingsActivity.this, EnterApiKeyActivity.class);
                startActivity(intent);
            }
        });
        Button btnRemoveApiKey = findViewById(R.id.btnRemoveApiKey);
        btnRemoveApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show an alert dialog and ask the user to confirm the action
                new MaterialAlertDialogBuilder(SettingsActivity.this)
                        .setTitle(R.string.lbl_remove_api_key_alert_title)
                        .setMessage(R.string.lbl_remove_api_key_alert_message)
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @SuppressLint("ApplySharedPref")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                                prefs.edit().clear().commit();
                                dialog.cancel();
                                // Reset the app by going to the first screen (splash)
                                Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);




        // Recover from SharedPreferences the stored values for "Year" views
        boolean filterByYear = prefs.getBoolean("filterByYear", false);
        chkFilterByYear.setChecked(filterByYear);
        int fromYear = prefs.getInt("fromYear", 1980);
        sbFromYear.setProgress(fromYear - MIN_YEAR);
        int toYear = prefs.getInt("toYear", MIN_YEAR + sbToYear.getMax());
        sbToYear.setProgress(toYear - MIN_YEAR);
        updateYearViews(filterByYear);

        // Recover from SharedPreferences the stored values for "Parts" views
        boolean filterByParts = prefs.getBoolean("filterByParts", false);
        chkFilterByParts.setChecked(filterByParts);
        int minimumParts = prefs.getInt("minimumParts", 0);
        int minimumPartsProgress = searchTick(minimumParts);
        sbMinimumParts.setProgress(minimumPartsProgress);
        int maximumParts = prefs.getInt("maximumParts", 10000);
        int maximumPartsProgress = searchTick(maximumParts) - 1;
        if (maximumPartsProgress < 0) maximumPartsProgress = 1;
        sbMaximumParts.setProgress(maximumPartsProgress);
        updatePartsViews(filterByParts);

        // Recover the API key
        String apiKey = prefs.getString("apiKey", "");
        if (apiKey.isEmpty()) {
            tvYourApiKey.setText(R.string.lbl_no_api_key);
        }
        else {
            tvYourApiKey.setText(apiKey);
        }

        String themesJson = prefs.getString("themesJson", "[ ]");
        Gson gson = new Gson();
        LegoThemes themes = gson.fromJson(themesJson, LegoThemes.class);
        LegoTheme emptyTheme = new LegoTheme(0, null, getString(R.string.no_theme_selected));
        themes.add(0, emptyTheme);
        LegoThemesAdapter adapter = new LegoThemesAdapter(this, themes);
        spTheme.setAdapter(adapter);

        long themeSelected = prefs.getLong("idThemeSelectedSpinner",0);
        Log.d("uwu","ID selected: " + themeSelected);
        int selectedThemePosition = adapter.getItemPositionById(themeSelected);
        spTheme.setSelection(selectedThemePosition);


        Log.d("flx", "COUNT=" + themes.size());
    }

    private int searchTick(int value) {
        for(int i = 0; i < TICK_VALUES.length; i++) {
            if (value == TICK_VALUES[i]) return i;
        }
        return 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();

        // Save current values from "Year" views to SharedPreferences
        boolean filterByYear = chkFilterByYear.isChecked();
        ed.putBoolean("filterByYear", filterByYear);
        int fromYear = MIN_YEAR + sbFromYear.getProgress();
        ed.putInt("fromYear", fromYear);
        int toYear = MIN_YEAR + sbToYear.getProgress();
        ed.putInt("toYear", toYear);

        // Save current values from "Parts" views to SharedPreferences
        boolean filterByParts = chkFilterByParts.isChecked();
        ed.putBoolean("filterByParts", filterByParts);
        int minimumParts = TICK_VALUES[sbMinimumParts.getProgress()];
        ed.putInt("minimumParts", minimumParts);
        int maximumParts = TICK_VALUES[sbMaximumParts.getProgress() + 1];
        ed.putInt("maximumParts", maximumParts);

        long themeSelected = spTheme.getSelectedItemId();
        ed.putLong("idThemeSelectedSpinner", themeSelected);
        ed.apply();
    }

    private void updateYearViews(boolean isChecked) {
        // NEW CODE: Group visibility (disappear)
        groupYear.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        tvFromYear.setText(String.valueOf(MIN_YEAR + sbFromYear.getProgress()));
        tvToYear.setText(String.valueOf(MIN_YEAR + sbToYear.getProgress()));
    }

    private void updatePartsViews(boolean isChecked) {
        groupParts.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        int minimumParts = TICK_VALUES[sbMinimumParts.getProgress()];
        tvMinimumParts.setText(String.valueOf(minimumParts));
        int maximumParts = TICK_VALUES[sbMaximumParts.getProgress() + 1];
        tvMaximumParts.setText(String.valueOf(maximumParts));
    }
}