package com.example.novcanik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Pojedini extends Fragment {

    TextView k_pojedini, kat_pojedini, iznos, datum;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pojedini, container, false);
        Bundle bundle = getArguments();

        Transakcija obj= (Transakcija) bundle.getSerializable("obj");
        k_pojedini = (TextView) view.findViewById(R.id.k_pojedini);
        kat_pojedini = (TextView) view.findViewById(R.id.kat_pojedini);
        iznos = (TextView) view.findViewById(R.id.iznos_pojedini);
        datum = (TextView) view.findViewById(R.id.datum_pojedini);

        k_pojedini.setText(obj.getKategorija().toString());
        kat_pojedini.setText(obj.getPodkategorija().toString());
        iznos.setText(obj.getIznos().toString() + "€");
        datum.setText(obj.getDatum().toString());


        return view;
    }
}