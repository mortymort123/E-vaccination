package com.example.e_vaccination.hospital;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_vaccination.MainActivity;
import com.example.e_vaccination.R;
import com.example.e_vaccination.hospital.drawer.HospitalDrawer;
import com.google.firebase.auth.FirebaseAuth;

public class HospitalLogin extends AppCompatActivity implements View.OnClickListener {

    TextView Register;
    Button Login;
    EditText EmailET, PasswordET;
    View eVaccination;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_login);
        mAuth = FirebaseAuth.getInstance();
        eVaccination = (View) findViewById(R.id.eVaccination);
        Register =(TextView) findViewById(R.id.GoToRegister);
        Login=(Button) findViewById(R.id.LoginButton);
        EmailET=(EditText) findViewById(R.id.Email);
        PasswordET= (EditText) findViewById(R.id.Password);


        eVaccination.setOnClickListener(this);
        Register.setOnClickListener(this);
        Login.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eVaccination:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.GoToRegister:
                startActivity(new Intent(this, HospitalRegister.class));
                break;
            case R.id.LoginButton:
                login();
                break;
        }
    }

    private void login() {
        String Email=EmailET.getText().toString().trim();
        String Password=PasswordET.getText().toString().trim();
        if(Email.isEmpty()){
            EmailET.setError("Email is required!");
            EmailET.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            EmailET.setError("Please enter valid email!");
            EmailET.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            PasswordET.setError("Password is required!");
            PasswordET.requestFocus();
            return;
        }

        if(Password.length()<6){
            PasswordET.setError("Minimum password length is 6 characters!");
            PasswordET.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                startActivity(new Intent(this, HospitalDrawer.class));
            }else{
                Toast.makeText(HospitalLogin.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
            }
        });
    }
}