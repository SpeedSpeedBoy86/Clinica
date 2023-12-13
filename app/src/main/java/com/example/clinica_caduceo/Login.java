package com.example.clinica_caduceo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOTPButton;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_country_code);
        phoneInput = findViewById(R.id.login_phone_number);
        sendOTPButton = findViewById(R.id.login_send_otp);
        progressBar = findViewById(R.id.login_progress_bar);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        sendOTPButton.setOnClickListener((v)->{
            if(!countryCodePicker.isValidFullNumber()){

            }
        });

    }
}