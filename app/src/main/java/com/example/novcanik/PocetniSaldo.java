package com.example.novcanik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PocetniSaldo extends AppCompatActivity {
DatabaseReference reff;
    member member;
    EditText iznoss;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetni_saldo);
        iznoss =(EditText)findViewById(R.id.edt);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reff = FirebaseDatabase.getInstance("https://novcanik-a3abd-default-rtdb.europe-west1.firebasedatabase.app").getReference(currentuser);
        member = new member();
        button= findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double iznos = Double.parseDouble((iznoss.getText().toString().trim()));
                Double aaa = Math.round(iznos * 100.0) / 100.0;
                member.setIznos(aaa);
                reff.setValue(member);
                Toast.makeText(PocetniSaldo.this, aaa.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PocetniSaldo.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}