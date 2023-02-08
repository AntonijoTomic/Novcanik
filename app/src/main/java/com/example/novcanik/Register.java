package com.example.novcanik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
private EditText inputEmail, inputPassword, inputPassword2;
TextView btn;
Button btnRegister;
private FirebaseAuth mAuth;
private ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        inputEmail=findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputPassword2 = findViewById(R.id.PasswordPotvrda);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(Register.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 Provjera();
            }
        });
    }
    private void Provjera(){
        String email = inputEmail.getText().toString();
        String pass = inputPassword.getText().toString();
        String pass2 = inputPassword2.getText().toString();

        if(email.isEmpty() || !email.contains("@"))
        {
            ShowError(inputEmail, "Nepravilan unos");
        }
        else if(pass.isEmpty() || pass.length()<7)
        {
            ShowError(inputPassword, "Lozinka mora imati najmanje 7 znakova");

        }
        else if(!pass2.equals(pass))
        {
            ShowError(inputPassword2, "Lozinke se ne podudaraju");
        }
        else
        {
            mLoadingBar.setTitle("Registracija");
            mLoadingBar.setMessage("Molimo pričekajte");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Register.this, "Registracija uspješna", Toast.LENGTH_LONG);
                        mLoadingBar.dismiss();
                    Intent intent = new Intent(Register.this, PocetniSaldo.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_LONG);
                    }
                }
            });
        }
    }
    private void ShowError(EditText input, String s)
    {
        input.setError(s);
        input.requestFocus();
    }
}