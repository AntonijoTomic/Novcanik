package com.example.novcanik;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Home extends Fragment {
    Button izracunaj, kamera;
    FirebaseAuth mAuth;
    DatabaseReference dataref;
    TextView saldo;
    EditText unos, kateogorije_unos;
    Spinner odabirkategorija;
    String  aa, currentuser;
    Object a;
    Date datum;
    Boolean oduzmi = true;
    Transakcija transakcija;
    private TextView prikazsalda;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        izracunaj = (Button) view.findViewById(R.id.skini);
        kamera = (Button) view.findViewById(R.id.odvedime_kamera);
        unos = (EditText) view.findViewById(R.id.unos);
        odabirkategorija = (Spinner) view.findViewById(R.id.kategorije);
        kateogorije_unos = (EditText) view.findViewById(R.id.unos_kategorija);
       // mAuth = FirebaseAuth.getInstance();
         currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        TextView prihod_text = (TextView) view.findViewById(R.id.Prihod_text);
        TextView potrosnja_text = (TextView) view.findViewById(R.id.Potrosnja_text);
        Switch s1 = (Switch) view.findViewById(R.id.odabir);
        datum = new Date();
        transakcija = new Transakcija(0.0,"a","a","a");
        dataref = FirebaseDatabase.getInstance("https://novcanik-a3abd-default-rtdb.europe-west1.firebasedatabase.app").getReference(currentuser);
        dataref.child("iznos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                         a = ds.getValue();
                        saldo = (TextView) getView().findViewById(R.id.saldo);
                        saldo.setText(a +"€");

                    }}
                else {
                }}});



SpinnerPrikaz(s1,odabirkategorija,potrosnja_text,prihod_text);


    s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SpinnerPrikaz(s1,odabirkategorija,potrosnja_text,prihod_text);
        }
    });
        kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                camera fragment = new camera();

                ft.replace(R.id.frameLayout, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        izracunaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double finalnacijena = 0.0;
                Double nekacijena =0.0;
                saldo = (TextView) getView().findViewById(R.id.saldo);
                String kk = saldo.getText().toString();
                String kkk = kk.replace("€", "");
                Toast.makeText(getActivity(), kk.toString(), Toast.LENGTH_LONG).show();
                 nekacijena = Double.parseDouble(kkk);
               Double unesenaCijena= Double.parseDouble(unos.getText().toString());
               if(oduzmi){
                finalnacijena = nekacijena - unesenaCijena;}
                else{  finalnacijena = nekacijena + unesenaCijena;}
                saldo.setText(finalnacijena +"€");
                dataref.child("iznos").setValue(finalnacijena);
                CharSequence s  = DateFormat.format("d. MMMM yyyy.", datum.getTime());
                transakcija.setDatum(s);
                transakcija.setIznos(unesenaCijena);
                dataref.child("transakcije").push().setValue(transakcija);
            }
        });

        return view;
    }



    private void SpinnerPrikaz(Switch s, Spinner spini, TextView pt, TextView ptt)
    {
      if(!s.isChecked()){
          oduzmi = false;
          String vr = "Prihod";
          pt.setTypeface(null, Typeface.NORMAL);
          ptt.setTypeface(null, Typeface.BOLD);
          aa= kateogorije_unos.getText ().toString();
          kateogorije_unos.setOnKeyListener(new View.OnKeyListener() {
              @Override
              public boolean onKey(View v, int keyCode, KeyEvent event) {
                  if(event.getAction() == KeyEvent.ACTION_DOWN &&keyCode==KeyEvent.KEYCODE_ENTER)
                  {
                      aa= kateogorije_unos.getText().toString();
                      kateogorije_unos.setText("");
                      postavljanje_spinnera(vr);
                      return true;
                  }
                  return false;
              }
          });
          postavljanje_spinnera(vr);
      }
      else
      {
          String vr = "Potrosnja";
          oduzmi = true;
          pt.setTypeface(null, Typeface.BOLD);
          ptt.setTypeface(null, Typeface.NORMAL);
          kateogorije_unos.setOnKeyListener(new View.OnKeyListener() {
              @Override
              public boolean onKey(View v, int keyCode, KeyEvent event) {
                  if(event.getAction() == KeyEvent.ACTION_DOWN &&keyCode==KeyEvent.KEYCODE_ENTER)
                  {
                      aa= kateogorije_unos.getText ().toString();
                      kateogorije_unos.setText("");
                      postavljanje_spinnera(vr);
                      return true;
                  }

                  return false;
              }
          });
          postavljanje_spinnera(vr);
      }

    }


    public void postavljanje_spinnera(String vrijednost) {
        transakcija.setKategorija(vrijednost);
        List<Kategorije> kate = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        if(aa.length() >0){dataref.child(vrijednost).push().setValue(aa);aa = "";};
        dataref.child(vrijednost).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot ds1 = task.getResult();
                        for(DataSnapshot uniqueKeySnapshot : ds1.getChildren()){
                            Kategorije k1 = new Kategorije();
                            k1.setKey(uniqueKeySnapshot.getKey());
                            k1.setNaziv(uniqueKeySnapshot.getValue().toString());
                            kate.add(k1);
                            values.add(uniqueKeySnapshot.getValue().toString());}}
                    ArrayAdapter adapt = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,values);
                    adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    odabirkategorija.setAdapter(adapt);
                    odabirkategorija.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            transakcija.setPodkategorija(parent.getItemAtPosition(position).toString());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                }});
    }
}