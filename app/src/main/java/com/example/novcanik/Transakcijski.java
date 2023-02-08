package com.example.novcanik;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Transakcijski extends Fragment  implements RecViewInterface{
    DatabaseReference dataref;
    RecyclerView recyclerView;
    Adapter adapter;
    List<Transakcija> transakcije =new ArrayList<>();
    RecViewInterface r = this;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_transakcijski, container, false);
        dataref = FirebaseDatabase.getInstance("https://novcanik-a3abd-default-rtdb.europe-west1.firebasedatabase.app").getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataref.child("transakcije").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
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

                         recyclerView = (RecyclerView)  view.findViewById(R.id.rec);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

                         adapter = new Adapter(getContext(), transakcije, r);
                        recyclerView.setAdapter(adapter);
                    }
                }}});




        return  view;
    }

    @Override
    public void onItemclick(int position) {


        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Transakcija tr1 =  (Transakcija) transakcije.get(position);

        Pojedini fragment = new Pojedini();
        Bundle bundle = new Bundle();
        bundle.putSerializable("obj", tr1);
        fragment.setArguments(bundle);
        ft.replace(R.id.frameLayout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}