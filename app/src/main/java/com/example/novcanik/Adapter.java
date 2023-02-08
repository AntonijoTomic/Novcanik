package com.example.novcanik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    private final RecViewInterface recViewInterface;


    Context context;
    List<Transakcija> transakcije;
    public  Adapter(Context context, List<Transakcija> transakcije, RecViewInterface recViewInterface) {
    this.context = context;
        this.transakcije = transakcije;
        this.recViewInterface = recViewInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.okvir, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.datum.setText(transakcije.get(position).getDatum());
        holder.iznos.setText(transakcije.get(position).getIznos().toString() + "€") ;
    }

    @Override
    public int getItemCount() {
        return transakcije.size();
    }

    public  static  class MyViewHolder extends RecyclerView.ViewHolder {

        TextView iznos;
        TextView datum;
        public MyViewHolder(@NonNull View itemView, RecViewInterface recViewInterface) {
            super(itemView);
            iznos = itemView.findViewById(R.id.iznos);
            datum = itemView.findViewById(R.id.datum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recViewInterface != null){
                    int positi = getAdapterPosition();
                    if(positi != RecyclerView.NO_POSITION){
                        recViewInterface.onItemclick(positi);
                    }
                    }
                }
            });
                    }
    }
    }

