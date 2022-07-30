package com.example.e_vaccination.parent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_vaccination.MainActivity;
import com.example.e_vaccination.R;
import com.example.e_vaccination.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ParentRegister extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView LoginTV;
    private Button RegisterB;
    private EditText FirstNameET,LastNameET,EmailET,PasswordET;
    private View eVaccinationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        mAuth = FirebaseAuth.getInstance();

        eVaccinationTV=(View) findViewById(R.id.eVaccination);
        LoginTV =(TextView) findViewById(R.id.GoToLogin);
        RegisterB=(Button) findViewById(R.id.RegisterButton);
        FirstNameET=(EditText)findViewById(R.id.FirstName);
        LastNameET=(EditText)findViewById(R.id.LastName);
        EmailET=(EditText)findViewById(R.id.Email);
        PasswordET=(EditText)findViewById(R.id.Password);

        eVaccinationTV.setOnClickListener(this);
        LoginTV.setOnClickListener(this);
        RegisterB.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eVaccination:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.GoToLogin:
                startActivity(new Intent(this, ParentLogin.class));
                break;
            case R.id.RegisterButton:
                register();
                break;
        }
    }
    private void register(){
        String FirstName=FirstNameET.getText().toString().trim();
        String LastName=LastNameET.getText().toString().trim();
        String Email=EmailET.getText().toString().trim();
        String Password=PasswordET.getText().toString().trim();
        if(FirstName.isEmpty()){
            FirstNameET.setError("First name is required!");
            FirstNameET.requestFocus();
            return;
        }

        if(LastName.isEmpty()){
            LastNameET.setError("Last name is required!");
            LastNameET.requestFocus();
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

                        Parent parent=new Parent(FirstName,LastName,Email);

                        FirebaseDatabase.getInstance().getReference("Parent")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(parent).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(ParentRegister.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(ParentRegister.this,"Failed to register try again!",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }else{
                        Toast.makeText(ParentRegister.this,"Failed to register try again!",Toast.LENGTH_LONG).show();
                    }
                });
    }
}