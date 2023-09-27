package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

public class verifinbr extends AppCompatActivity {

    EditText mgetotp;
    MaterialButton verifbtn;
    FirebaseAuth firebaseAuth;
    String enterotp ;
    ProgressBar mprogressbarofmain;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifinbr);

        firebaseAuth = FirebaseAuth.getInstance();
        mgetotp = (EditText) findViewById(R.id.eotp);
        verifbtn = (MaterialButton) findViewById(R.id.verfotpbtn);
        mprogressbarofmain = (ProgressBar) findViewById(R.id.progress);

        verifbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterotp = mgetotp.getText().toString();
                if (enterotp.isEmpty()) {
                    Toast.makeText(verifinbr.this, "PLEASE ENTERN THE OTP CODE ", Toast.LENGTH_LONG).show();
                } else {
                    mprogressbarofmain.setVisibility(View.VISIBLE);
                    String codereceive = getIntent().getStringExtra("otp");
                    // am karnon bs m fi 8r hk karn
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codereceive, enterotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

        private void signInWithPhoneAuthCredential (PhoneAuthCredential crede){
            firebaseAuth.signInWithCredential(crede).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mprogressbarofmain.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"LOGIN SUCCES",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(verifinbr.this,profile.class);
                        startActivity(intent);
                        finish();
                    }
                    else {if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                       mprogressbarofmain.setVisibility(View.INVISIBLE);
                       Toast.makeText(getApplicationContext(),"LOGIN FAIL",Toast.LENGTH_SHORT).show();
                    }
                    }
                    
                }
            });}


    }
