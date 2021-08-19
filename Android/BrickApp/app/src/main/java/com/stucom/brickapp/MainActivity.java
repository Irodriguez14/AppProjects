package com.stucom.brickapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.stucom.brickapp.model.LegoSetsApiResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView tvResults;
    RecyclerView recyclerView;

    String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        tvResults = findViewById(R.id.tvResults);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lastSearch = prefs.getString("lastSearch", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        apiKey = prefs.getString("apiKey", "");
        String lastSearch = prefs.getString("lastSearch", "");

        SearchView edSearch = (SearchView) menu.findItem(R.id.btnSearch).getActionView();
        if (!lastSearch.isEmpty()) {
            edSearch.setIconified(false);
            edSearch.setQuery(lastSearch, true);
        }
        edSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query, 100);
                return false;
            }
            @Override public boolean onQueryTextChange(String newText) { return false; }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnSetup) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void search(String query, int pageSize) {
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        boolean filterByYear = prefs.getBoolean("filterByYear", false);
        int fromYear = prefs.getInt("fromYear", 1980);
        int toYear = prefs.getInt("toYear", 2020);
        boolean filterByParts = prefs.getBoolean("filterByParts", false);
        int minimumParts = prefs.getInt("minimumParts", 0);
        int maximumParts = prefs.getInt("maximumParts", 10000);
        long themeSelected = prefs.getLong("idThemeSelectedSpinner",0);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastSearch", query);
        editor.apply();

        Toast.makeText(this, "Searching " + query, Toast.LENGTH_SHORT).show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF8");
        }
        catch (Exception ignored) {
            encodedQuery = query;
        }
        String url = "https://rebrickable.com/api/v3/lego/sets?" +
                "key=" + apiKey + "&" +
                "page_size=" + pageSize + "&" +
                "search=" + encodedQuery;
        if (filterByYear) {
            url += "&min_year=" + fromYear + "&max_year=" + toYear;
        }
        if (filterByParts) {
            url += "&min_parts=" + minimumParts + "&max_parts=" + maximumParts;
        }
        if (themeSelected != 0){
            url += "&theme_id=" + themeSelected;
        }
        Log.d("flx", "URL=" + url);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("flx", "RESPONSE: " + response);
                        Gson gson = new Gson();
                        LegoSetsApiResponse legoSetsApiResponse = gson.fromJson(response, LegoSetsApiResponse.class);
                        int count = legoSetsApiResponse.getCount();
                        int resultCount = legoSetsApiResponse.getResults().size();
                        String msg = getResources().getQuantityString(R.plurals.found_normal, resultCount, resultCount);
                        if (count > resultCount) {
                            msg = getString(R.string.found_overflow, resultCount, count);
                        }
                        tvResults.setText(msg);
                        LegoSetsAdapter adapter = new LegoSetsAdapter(MainActivity.this, legoSetsApiResponse);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
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
                        Snackbar.make(tvResults, msg, 3000).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        queue.add(request);
        progressBar.setVisibility(View.VISIBLE);
        tvResults.setText("");
    }
}