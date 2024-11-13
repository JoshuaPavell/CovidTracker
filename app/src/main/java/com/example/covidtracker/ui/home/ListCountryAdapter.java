package com.example.covidtracker.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.covidtracker.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListCountryAdapter extends RecyclerView.Adapter<ListCountryAdapter.DataHold> {
    ArrayList<ListCountry> dataCountry;
    ArrayList<ListCountry> arrayList;
    LayoutInflater inflater;
    Context context;

    public ListCountryAdapter(Context context, ArrayList<ListCountry> dataCountry){
        this.dataCountry = dataCountry;
        this.context = context;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(dataCountry);
        this.inflater = LayoutInflater.from(context);
    }



    @NonNull
    @Override
    public ListCountryAdapter.DataHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_view_negara, parent, false);
        return new DataHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCountryAdapter.DataHold holder, int position) {
        ListCountry listCountry = dataCountry.get(position);
        holder.namaNegara.setText(listCountry.getcName());
        Glide.with(context).load(listCountry.getcFlag()).into(holder.imgNegara);

        String caseNegara = decimalFormat(Long.parseLong(listCountry.gettCases()));
        String deathNegara = decimalFormat(Long.parseLong(listCountry.gettDeath()));
        String recoveredNegara = decimalFormat(Long.parseLong(listCountry.gettRecovered()));

        holder.deathNegara.setText(deathNegara);
        holder.caseNegara.setText(caseNegara);
        holder.recoveredNegara.setText(recoveredNegara);


    }

    @Override
    public int getItemCount() {
        return dataCountry.size();
    }

    public class DataHold extends RecyclerView.ViewHolder{

        ImageView imgNegara;
        TextView namaNegara, caseNegara, deathNegara, recoveredNegara;

        public DataHold(@NonNull View itemView){
            super(itemView);
            imgNegara = itemView.findViewById(R.id.gambar_bendera);
            namaNegara = itemView.findViewById(R.id.nama_negara);
            caseNegara = itemView.findViewById(R.id.caseNegara);
            deathNegara = itemView.findViewById(R.id.deathNegara);
            recoveredNegara = itemView.findViewById(R.id.recoveredNegara);
        }
    }

    public  void filter(String charText) {
        if (!charText.isEmpty()) {
            charText = charText.toLowerCase(Locale.getDefault());
        }
        dataCountry.clear();

        if (charText.length() == 0) {
            dataCountry.addAll(arrayList);
        } else {
            for (ListCountry wp : arrayList) {
                if (wp.getcName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataCountry.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public String decimalFormat(long Number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(Number);
        return formattedString;
    }
}
