package com.example.covidtracker.ui.home;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.covidtracker.R;
import com.example.covidtracker.ui.gallery.GalleryFragment;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HolderData> {
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<Home> listData;
    ArrayList<Home> arrayList;
    LayoutInflater inflater;
    Context context;

    public HomeAdapter(Context context, ArrayList<Home> listData, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.listData = listData;
        this.context = context;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(listData);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_view, parent, false);
        return new HolderData(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HolderData holder, int position) {
        Home home = listData.get(position);
        holder.txtData.setText(home.getContinent());

        if(home.getContinent().equals("Asia")){
            Glide.with(context).load(R.drawable.asia).into(holder.imgData);
        }
        else if(home.getContinent().equals("Europe")){
            Glide.with(context).load(R.drawable._51641).into(holder.imgData);
        }
        else if(home.getContinent().equals("North America")){
            Glide.with(context).load(R.drawable.north_america).into(holder.imgData);
        }
        else if(home.getContinent().equals("South America")){
            Glide.with(context).load(R.drawable.south_america).into(holder.imgData);
        }
        else if(home.getContinent().equals("Australia-Oceania")){
            Glide.with(context).load(R.drawable.oceania).into(holder.imgData);
        }

        String fCases = decimalFormat(Long.parseLong(home.getCases()));
        String fDeaths = decimalFormat(Long.parseLong(home.getDeaths()));
        String fRecovered = decimalFormat(Long.parseLong(home.getRecovered()));
//
//        String fCases = prettyCount(Integer.parseInt(home.getCases()));
//        String fDeaths = prettyCount(Integer.parseInt(home.getDeaths()));
//        String fRecovered = prettyCount(Integer.parseInt(home.getRecovered()));
        String fPopulation = prettyCount(Long.parseLong(home.getPopulation()));


        holder.casesData.setText(fCases);
        holder.deathsData.setText(fDeaths);
        holder.recoveredData.setText(fRecovered);
        holder.populationData.setText(fPopulation);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        ImageView imgData;
        TextView txtData;
        TextView casesData;
        TextView deathsData;
        TextView recoveredData;
        TextView populationData;
        public HolderData(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);
            imgData = itemView.findViewById(R.id.gambar_continent);
            txtData = itemView.findViewById(R.id.nama_continent);
            casesData = itemView.findViewById(R.id.cases_continent);
            deathsData = itemView.findViewById(R.id.deaths_continent);
            recoveredData = itemView.findViewById(R.id.recovered_continent);
            populationData = itemView.findViewById(R.id.population);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    public String prettyCount(long number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T'};
        int value = (int) Math.floor(Math.log10(number));
        int base = value / 3;

        if(number >= 1000000000){
            return String.format("%.2fB", number/ 1000000000.0);
        }
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(number / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(number);
        }
    }

    public  void filter(String charText) {
        if (!charText.isEmpty()) {
            charText = charText.toLowerCase(Locale.getDefault());
        }
        listData.clear();

        if (charText.length() == 0) {
            listData.addAll(arrayList);
        } else {
            for (Home wp : arrayList) {
                if (wp.getContinent().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listData.add(wp);
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
