package com.example.novcanik;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Grafovi extends Fragment {
    TextView najveci, najmanji, prvi, drugi;
    Button prikaziakao;
    DatabaseReference dataref;
    ImageView datep;
    TextView stisni;
    BarChart barChart;
    PieChart pieChart;
    RadioButton rb_prihod, rbPotrosnja;
    String vrijednost_kategorije;
    RadioGroup rg;
    ArrayList<BarEntry> barEntries;
    ArrayList<PieEntry> pieEntries;
    Date result = new Date();
    Date result2 = new Date();
    List<Transakcija> transakcije =new ArrayList<>();
    List<ZaGraf> zaGraf =new ArrayList<>();
    final ArrayList<String> xAxisLabel = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_grafovi, container, false);
        barChart = (BarChart) v.findViewById(R.id.bar_chart);
        pieChart = (PieChart) v.findViewById(R.id.pie_chart);
        barChart.setVisibility(v.GONE);
        pieChart.setVisibility(v.GONE);
        prikaziakao = (Button) v.findViewById(R.id.prikazikao);
        stisni = (TextView) v.findViewById(R.id.stisni);
        barEntries = new ArrayList<>();
        pieEntries = new ArrayList<>();
        datep = (ImageView) v.findViewById(R.id.raspondatum);
        rg = (RadioGroup) v.findViewById(R.id.rgroup);
        stisni.setVisibility(v.GONE);
        rb_prihod = (RadioButton) v.findViewById(R.id.radioPrihod);
        najveci = (TextView) v.findViewById(R.id.najvise_na);
        najmanji = (TextView) v.findViewById(R.id.najmanje_na);
        rb_prihod.setEnabled(false);
        rbPotrosnja = (RadioButton) v.findViewById(R.id.radipPotrosnja);
        rbPotrosnja.setEnabled(false);
        prikaziakao.setEnabled(false);
        vrijednost_kategorije = "Prihod";
        LinearLayout l1 = (LinearLayout) v.findViewById(R.id.hide1);
        LinearLayout l2 = (LinearLayout) v.findViewById(R.id.hide2);
        LinearLayout l3 = (LinearLayout) v.findViewById(R.id.hide3);
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Odaberite datum");
        final  MaterialDatePicker materialDatePicker = builder.build();
        povlacenje();


        prikaziakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 l1.setVisibility(v.GONE);
                l2.setVisibility(v.GONE);
                 l3.setVisibility(v.GONE);
                postavljanje_grafova(vrijednost_kategorije);
            }
        });
        datep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    materialDatePicker.show(getParentFragmentManager(), "DATE_PICKER");

            }

        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair p1 = (Pair) selection;
                DateFormat simple = new SimpleDateFormat("d. MMMM yyyy.");
                 result = new Date(Long.parseLong(p1.first.toString()));
                    result2 = new Date(Long.parseLong(p1.second.toString()));
                Toast.makeText(getActivity(),"Od " + simple.format(result).toString() + " do " +simple.format(result2).toString(), Toast.LENGTH_LONG).show();
                rb_prihod.setEnabled(true);
                rbPotrosnja.setEnabled(true);
            }
        });
        stisni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Grafovi fragment = new Grafovi();
                ft.replace(R.id.frameLayout, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId){
                    case 2131231095:
                        barEntries= new ArrayList<>();
                        pieEntries= new ArrayList<>();
                        vrijednost_kategorije = "Prihod";
                        //postavljanje_grafova(vrijednost_kategorije);
                        prvi = (TextView) v.findViewById(R.id.najvise);
                        prvi.setText("U odabranom razdoblju najviše prihoda imate:");
                        drugi = (TextView) v.findViewById(R.id.najmanje);
                        drugi.setText("U odabranom razdoblju najmanje prihoda imate:");
                        funckijazastatistiku(vrijednost_kategorije);
                        stisni.setVisibility(v.VISIBLE);
                        prikaziakao.setEnabled(true);
                        rbPotrosnja.setEnabled(false);
                        break;
                    case 2131231096:
                        vrijednost_kategorije = "Potrosnja";
                        rb_prihod.setEnabled(false);
                        funckijazastatistiku(vrijednost_kategorije);
                       // postavljanje_grafova(vrijednost_kategorije);
                        prikaziakao.setEnabled(true);
                        stisni.setVisibility(v.VISIBLE);
                        break;
                }
            }
        });

        return v;
    }


    public void povlacenje(){
    dataref = FirebaseDatabase.getInstance("https://novcanik-a3abd-default-rtdb.europe-west1.firebasedatabase.app").getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    dataref.child("transakcije").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if(task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot ds1 = task.getResult();
                    for (DataSnapshot uniqueKeySnapshot : ds1.getChildren()) {
                        Transakcija t1 = new Transakcija(0.0,"","","");
                        double iznos1 = Double.parseDouble(uniqueKeySnapshot.child("iznos").getValue().toString());
                        t1.setDatum(uniqueKeySnapshot.child("datum").getValue().toString());
                        t1.setIznos(iznos1);
                        t1.setPodkategorija(uniqueKeySnapshot.child("podkategorija").getValue().toString());
                        t1.setKategorija(uniqueKeySnapshot.child("kategorija").getValue().toString());
                        transakcije.add(t1);
                    }
                }
            }
        }

    });
}
    public void postavljanje_grafova(String vrijednost){
        barChart.setVisibility(getView().VISIBLE);
        pieChart.setVisibility(getView().VISIBLE);
        BarDataSet barDataSet = new BarDataSet(barEntries,null);
        Legend l = barChart.getLegend();
        Legend l1 = pieChart.getLegend();
        l.setEnabled(false);
        l1.setEnabled(false);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(true);
        barChart.setData(new BarData(barDataSet));
        barChart.animateY(3000);
        barChart.getDescription().setText(vrijednost);
        barChart.getDescription().setTextColor(Color.BLUE);
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Kategorije");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(3000, 3000);
        pieChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        pieChart.setUsePercentValues(true);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xAxisLabel.get((int) value);
            }
        };

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        zaGraf= new ArrayList<>();
    }
    public void funckijazastatistiku(String vrijednost){
        int brojac =0;
        String naziv="";
        for (Transakcija t: transakcije) {
            SimpleDateFormat sdformat = new SimpleDateFormat("d. MMMM yyyy.");
            int privremeno =0;
            String naziv_privrmeno= "";
            try {
                Date d1 = sdformat.parse(t.getDatum().toString());
                if(d1.compareTo(result) >= 0 && d1.compareTo(result2) <= 0 ) {
                    boolean ima = false;
                    if(t.getKategorija().equals(vrijednost)){

                        for(ZaGraf zg :zaGraf){
                            if(zg.getPodkategorija().equals(t.getPodkategorija()))
                            {
                                zg.setIznos(zg.getIznos() + t.getIznos());
                                privremeno += 1;
                                naziv_privrmeno = zg.getPodkategorija();
                                ima = true;
                            }
                        }
                        if(!ima){
                            zaGraf.add(new ZaGraf(t.getIznos(),t.getPodkategorija()));
                            privremeno += 1;
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(privremeno > brojac){
                brojac = privremeno;
                naziv = naziv_privrmeno;
            }

        }
        int index = 0;

        Double maksimalno =0.0;
        Double minimalno =10000000000000000000.0;
        String kljuc =" ";
        String kljuc2=" ";

        for(ZaGraf zg :zaGraf){
            BarEntry barEntry = new BarEntry(index++, Float.parseFloat(zg.getIznos().toString()));
            PieEntry pieEntry = new PieEntry(Float.parseFloat(zg.getIznos().toString()), index);
            xAxisLabel.add(zg.getPodkategorija());
            barEntries.add(barEntry);
            pieEntries.add(pieEntry);
            if(zg.getIznos() > maksimalno)
            {
                maksimalno = zg.getIznos();
                kljuc= zg.getPodkategorija();
            }
            if(zg.getIznos() < minimalno)
            {
                minimalno = zg.getIznos();
                kljuc2= zg.getPodkategorija();
            }
        }
        najveci.setText(kljuc.toString() + " " + maksimalno.toString()+"€");
        najmanji.setText(kljuc2.toString() + " " + minimalno.toString()+"€");
    }
}