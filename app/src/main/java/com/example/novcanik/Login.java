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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

EditText inputEmail, inputPassword;
Button btnLog;

    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
TextView reg = findViewById(R.id.Rega);
reg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Login.this, Register.class));
    }
});
        inputEmail = findViewById(R.id.loggemail);
        inputPassword = findViewById(R.id.logpass);
        btnLog = findViewById(R.id.btnlogin);
        mAuth = FirebaseAuth.getInstance();
    mLoadingBar=new ProgressDialog(Login.this);
       btnLog.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               provjera2();
           }

           private void provjera2() {
               String email = inputEmail.getText().toString();
               String passw = inputPassword.getText().toString();

               if(email.isEmpty() || !email.contains("@"))
               {
                   ShowError(inputEmail, "Nepravilan unos");
               }
               else if(passw.isEmpty() || passw.length()<7)
               {
                   ShowError(inputPassword, "Lozinka mora imati najmanje 7 znakova");

               }
               else
               {
mLoadingBar.setTitle("Login");
mLoadingBar.setMessage("Molimo pričekajte");
mLoadingBar.setCanceledOnTouchOutside(false);
mLoadingBar.show();

mAuth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            mLoadingBar.dismiss();
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

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
       });
    }
}