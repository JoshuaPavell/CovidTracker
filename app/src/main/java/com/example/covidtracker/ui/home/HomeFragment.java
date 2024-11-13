package com.example.covidtracker.ui.home;

import static com.android.volley.VolleyLog.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.R;
import com.example.covidtracker.databinding.FragmentHomeBinding;
import com.example.covidtracker.ui.gallery.GalleryFragment;
import com.example.covidtracker.ui.slideshow.SlideshowFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment implements RecyclerViewInterface {
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    LinearLayout progressLayout;

    ArrayList<Home> listData = new ArrayList<>();
    String url = "https://disease.sh/v3/covid-19/continents";


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        recyclerView = binding.rvData;
        progressLayout = binding.progressLayout;
        setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        getDataFromApi();

        View root = binding.getRoot();

        return root;
    }

    public void getDataFromApi(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://disease.sh/v3/covid-19/continents";
        listData = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressLayout.setVisibility(View.GONE);
                if (response!=null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i<jsonArray.length();i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            listData.add(new Home(data.getString("continent"), data.getString("cases"), data.getString("deaths"), data.getString("recovered"), data.getString("population")));
                        }

                        Collections.sort(listData, new Comparator<Home>() {
                            @Override
                            public int compare(Home home, Home t1) {
                                return home.getContinent().compareToIgnoreCase(t1.getContinent());
                            }
                        });
                        showRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressLayout.setVisibility(View.GONE);
                Log.i(TAG, "Error:"+error.toString());
                Toast.makeText(getActivity(), "Error:"+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void sortAscending(){
        Collections.sort(listData, new Comparator<Home>() {
            @Override
            public int compare(Home home, Home t1) {
                return home.getContinent().compareToIgnoreCase(t1.getContinent());
            }
        });
        homeAdapter.notifyDataSetChanged();
    }

    public void sortDescending(){
        Collections.reverse(listData);
        homeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Continent");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String s) {
                        String text = s;
                        if (homeAdapter!=null)
                        {
                            homeAdapter.filter(text);
                        }
                        return false;
                    }
                });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ascending:
                Toast.makeText(getContext(), "Sort Ascending", Toast.LENGTH_SHORT).show();
                sortAscending();
                break;

            case R.id.descending:
                Toast.makeText(getContext(), "Sort Descending", Toast.LENGTH_SHORT).show();
                sortDescending();
                break;
        }
        return false;
    }

    public void showRecyclerView(){
        homeAdapter = new HomeAdapter(getActivity(), listData, this);
        recyclerView.setAdapter(homeAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Home home = listData.get(position);
        Toast.makeText(getActivity(), home.getContinent(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ListCountryActivity.class);
        intent.putExtra("continent_name", home.getContinent());
        startActivity(intent);
    }


}