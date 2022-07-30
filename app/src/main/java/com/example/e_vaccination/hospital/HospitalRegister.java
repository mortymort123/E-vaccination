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
import com.example.e_vaccination.model.Hospital;
import com.example.e_vaccination.model.Parent;
import com.example.e_vaccination.parent.ParentLogin;
import com.example.e_vaccination.parent.ParentRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class HospitalRegister extends AppCompatActivity implements View.OnClickListener {

    TextView Login;
    Button Register;
    EditText NameET,AddressET,EmailET,PasswordET;
    private View eVaccination;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_register);
        mAuth = FirebaseAuth.getInstance();

        eVaccination=(View) findViewById(R.id.eVaccination);
        Login =(TextView) findViewById(R.id.GoToLogin);
        Register=(Button) findViewById(R.id.RegisterButton);
        NameET=(EditText) findViewById(R.id.name);
        AddressET=(EditText) findViewById(R.id.address);
        EmailET=(EditText) findViewById(R.id.Email);
        PasswordET=(EditText) findViewById(R.id.Password);


        eVaccination.setOnClickListener(this);
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eVaccination:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.GoToLogin:
                startActivity(new Intent(this, HospitalLogin.class));
                break;
            case R.id.RegisterButton:
                register();
                break;
        }
    }

    private void register() {
        String Name=NameET.getText().toString().trim();
        String Address=AddressET.getText().toString().trim();
        String Email=EmailET.getText().toString().trim();
        String Password=PasswordET.getText().toString().trim();
        if(Name.isEmpty()){
            NameET.setError("Hospital Name is required!");
            NameET.requestFocus();
            return;
        }

        if(Address.isEmpty()){
            AddressET.setError("Address is required!");
            AddressET.requestFocus();
            return;
        }

        if(Email.isEmpty()){
            EmailET.setError("Email is required!");
            EmailET.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            EmailET.setError("Please provide valid email!");
            EmailET.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            PasswordET.setError("Password is required!");
            PasswordET.requestFocus();
            return;
        }

        if(Password.length()<6){
            PasswordET.setError("Minimum password length should be 6 characters!");
            PasswordET.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {

                        Hospital hospital=new Hospital(Name,Address,Email);

                        FirebaseDatabase.getInstance().getReference("Hospital")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(hospital).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(HospitalRegister.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(HospitalRegister.this,"Failed to register try again!",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }else{
                        Toast.makeText(HospitalRegister.this,"Failed to register try again!",Toast.LENGTH_LONG).show();
                    }
                });
    }
}