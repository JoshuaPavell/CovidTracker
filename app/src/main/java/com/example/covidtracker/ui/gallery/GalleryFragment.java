package com.example.covidtracker.ui.gallery;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.R;
import com.example.covidtracker.databinding.FragmentGalleryBinding;

import org.json.JSONException;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private TextView tvTotalCase, tvTotalDeath, tvTotalRecovered, tvLastUpdated;
    private PieChart Piechart;
    String tCases, tDeath, tRecovered;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvTotalCase = root.findViewById(R.id.txtTotalCase);
        tvTotalDeath = root.findViewById(R.id.txtTotalDeath);
        tvTotalRecovered = root.findViewById(R.id.txtTotalRecovered);
        tvLastUpdated = root.findViewById(R.id.txtLastUpdated);
        setHasOptionsMenu(true);
        Piechart = root.findViewById(R.id.piechart);
        getDataApiGlobalCases();
        return root;
    }

    private void setPieChart() {
        Piechart.addPieSlice(
                new PieModel(
                        "Cases",
                        Integer.parseInt(tCases),
                        Color.parseColor("#FFC107")
                )
        );
        Piechart.addPieSlice(
                new PieModel(
                        "Death",
                        Integer.parseInt(tDeath),
                        Color.parseColor("#801616")
                )
        );
        Piechart.addPieSlice(
                new PieModel(
                        "Recovered",
                        Integer.parseInt(tRecovered),
                        Color.parseColor("#FF018786")
                )
        );
        Piechart.startAnimation();
    }

    private String getLastUpdated(Long miliseconds) {
        SimpleDateFormat formatDate = new SimpleDateFormat("EEE,dd-MMM-yyyy hh:mm:ss aaa");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliseconds);
        return formatDate.format(calendar.getTime());
    }

    private void getDataApiGlobalCases() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://disease.sh/v3/covid-19/all";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    tCases = jsonObject.getString("cases");
                    tDeath = jsonObject.getString("deaths");
                    tRecovered = jsonObject.getString("recovered");

                    tvTotalCase.setText(decimalFormat(Long.parseLong(tCases)));
                    tvTotalDeath.setText(decimalFormat(Long.parseLong(tDeath)));
                    tvTotalRecovered.setText(decimalFormat(Long.parseLong(tRecovered)));
                    // rendered Last Updated from URL
                    tvLastUpdated.setText(getLastUpdated(jsonObject.getLong("updated")));

                    setPieChart();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error response",error.toString());
            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setVisible(false);
        MenuItem sortAsc = menu.findItem(R.id.ascending);
        sortAsc.setVisible(false);
        MenuItem sortDesc = menu.findItem(R.id.descending);
        sortDesc.setVisible(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String decimalFormat(long Number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###,###");
        String formattedString = formatter.format(Number);
        return formattedString;
    }

}