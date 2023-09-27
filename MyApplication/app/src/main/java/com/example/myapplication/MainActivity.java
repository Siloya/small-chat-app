package com.example.myapplication;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText tnumbre;
    MaterialButton loginbtn;
    CountryCodePicker countryCodePicker;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    String codesent;
    String phonenumber, countrycode;
    ProgressBar mprogressbarofmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        tnumbre = (EditText) findViewById(R.id.number);
        loginbtn = (MaterialButton) findViewById(R.id.login);
        mprogressbarofmain = (ProgressBar) findViewById(R.id.progress);

        countryCodePicker = findViewById(R.id.countryCodePicker);
        countryCodePicker.setCountryForNameCode("LB"); // Set default country
        countrycode = countryCodePicker.getSelectedCountryCodeWithPlus();

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.i("Test", phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("Test", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(MainActivity.this, "otp is sent ", Toast.LENGTH_LONG).show();
                codesent = s;
                mprogressbarofmain.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(MainActivity.this, verifinbr.class);
                intent.putExtra("otp", codesent);
                startActivity(intent);
            }
        };

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //    countrycode = countryCodePicker.getSelectedCountryNameCode();
                countrycode = countryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = tnumbre.getText().toString();

                if (number.isEmpty()) {
                    Toast.makeText(MainActivity.this, "please enter your number  ", Toast.LENGTH_LONG).show();
                } else if (number.length() < 5) {
                    Toast.makeText(MainActivity.this, "please enter a valid number ", Toast.LENGTH_LONG).show();
                } else {
                    mprogressbarofmain.setVisibility(View.VISIBLE);
                    phonenumber = countrycode + number;

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phonenumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mcallbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, recycleview.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }


}