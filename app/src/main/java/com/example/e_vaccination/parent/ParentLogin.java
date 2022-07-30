package com.example.e_vaccination.parent;


import androidx.annotation.NonNull;
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
import com.example.e_vaccination.admin.drawer.AdminDrawer;
import com.example.e_vaccination.parent.drawer.ParentDrawer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ParentLogin extends AppCompatActivity implements View.OnClickListener {

    TextView Register;
    Button Login;
    EditText EmailET, PasswordET;
    View eVaccination;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        mAuth = FirebaseAuth.getInstance();
        eVaccination = (View) findViewById(R.id.eVaccination);
        Register = (TextView) findViewById(R.id.GoToRegister);
        Login = (Button) findViewById(R.id.LoginButton);
        EmailET = (EditText) findViewById(R.id.Email);
        PasswordET = (EditText) findViewById(R.id.Password);

        eVaccination.setOnClickListener(this);
        Register.setOnClickListener(this);
        Login.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eVaccination:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.GoToRegister:
                startActivity(new Intent(this, ParentRegister.class));
                break;
            case R.id.LoginButton:
                login();
                break;
        }
    }

    private void login() {
        String Email = EmailET.getText().toString().trim();
        String Password = PasswordET.getText().toString().trim();
        if (Email.isEmpty()) {
            EmailET.setError("Email is required!");
            EmailET.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            EmailET.setError("Please enter valid email!");
            EmailET.requestFocus();
            return;
        }

        if (Password.isEmpty()) {
            PasswordET.setError("Password is required!");
            PasswordET.requestFocus();
            return;
        }

        if (Password.length() < 6) {
            PasswordET.setError("Minimum password length is 6 characters!");
            PasswordET.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String parentID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Parent").child(parentID);
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Admin")) {
                            startActivity(new Intent(ParentLogin.this, AdminDrawer.class));
                        } else {
                            startActivity(new Intent(ParentLogin.this, ParentDrawer.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                Toast.makeText(ParentLogin.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
            }
        });
    }
}