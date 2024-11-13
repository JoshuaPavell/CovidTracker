package com.example.covidtracker.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.R;
import com.example.covidtracker.databinding.ActivityListCountryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListCountryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ListCountryAdapter listCountryAdapter;
    ActivityListCountryBinding binding;
    LinearLayout progressLayout;
    String nameContinent;
    ArrayList<ListCountry> dataCountry = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListCountryBinding.inflate(getLayoutInflater());
        recyclerView = binding.rvCountry;
        progressLayout = binding.progressLayout;
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        getData();
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

    }

    //for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_utama, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        Intent intent = getIntent();
        nameContinent = intent.getStringExtra("continent_name");
        binding.toolbar.setTitle(nameContinent);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Country");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                if (listCountryAdapter!=null)
                {
                    listCountryAdapter.filter(text);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ascending:
                Toast.makeText(getApplicationContext(), "Sort Ascending", Toast.LENGTH_SHORT).show();
                sortAscending();
                break;

            case R.id.descending:
                Toast.makeText(getApplicationContext(), "Sort Descending", Toast.LENGTH_SHORT).show();
                sortDescending();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://disease.sh/v3/covid-19/countries";
        dataCountry = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressLayout.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i<jsonArray.length();i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            if(data.getString("continent").equalsIgnoreCase(nameContinent)){
                                JSONObject countryinfo = data.getJSONObject("countryInfo");
                                dataCountry.add(new ListCountry(data.getString("cases"), data.getString("deaths"), data.getString("recovered"), countryinfo.getString("flag"), data.getString("country")));
                            }
                            Collections.sort(dataCountry, new Comparator<ListCountry>() {
                                @Override
                                public int compare(ListCountry listCountry, ListCountry t1) {
                                    return listCountry.getcName().compareToIgnoreCase(t1.getcName());
                                }
                            });
                            showRecyclerView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressLayout.setVisibility(View.GONE);
                Log.i(VolleyLog.TAG, "Error:"+error.toString());
                Toast.makeText(getApplicationContext(), "Error:"+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
    public void sortAscending(){
        Collections.sort(dataCountry, new Comparator<ListCountry>() {
            @Override
            public int compare(ListCountry listCountry, ListCountry t1) {
                return listCountry.getcName().compareToIgnoreCase(t1.getcName());
            }
        });
        listCountryAdapter.notifyDataSetChanged();
    }
    public void sortDescending(){
        Collections.reverse(dataCountry);
        listCountryAdapter.notifyDataSetChanged();
    }
    public void showRecyclerView(){
        listCountryAdapter = new ListCountryAdapter(getApplicationContext(), dataCountry);
        recyclerView.setAdapter(listCountryAdapter);
    }
}