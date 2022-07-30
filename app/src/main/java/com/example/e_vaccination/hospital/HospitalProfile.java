package com.example.e_vaccination.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_vaccination.MainActivity;
import com.example.e_vaccination.R;
import com.example.e_vaccination.model.Hospital;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HospitalProfile extends AppCompatActivity {
    private FirebaseUser hospital;
    private DatabaseReference reference;

    private String HospitalID;

    private Button logout;
    private TextView HospitalNameTV,AddressTV, EmailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile);

        logout=(Button) findViewById(R.id.logout);
        HospitalNameTV =(TextView) findViewById(R.id.HospitalName);
        AddressTV =(TextView) findViewById(R.id.address);
        EmailTV=(TextView) findViewById(R.id.Email);

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HospitalProfile.this, MainActivity.class));
        });

        hospital = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Hospital");
        HospitalID=hospital.getUid();

        reference.child(HospitalID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Hospital hospitalProfile = snapshot.getValue(Hospital.class);
                if(hospitalProfile!=null){
                    String HospitalName = hospitalProfile.HospitalName;
                    String Address = hospitalProfile.Address;
                    String Email = hospitalProfile.Email;

                    HospitalNameTV.setText("Hospital: "+HospitalName);
                    AddressTV.setText("Address: "+Address);
                    EmailTV.setText("Email: " + Email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HospitalProfile.this,"Something wrong happened!",Toast.LENGTH_LONG).show();
            }
        });
    }
}