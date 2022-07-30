package com.example.e_vaccination.hospital.drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_vaccination.MainActivity;
import com.example.e_vaccination.R;
import com.example.e_vaccination.hospital.HospitalProfile;
import com.example.e_vaccination.hospital.drawer.fragments.AllAppointmentHospital;
import com.example.e_vaccination.model.Hospital;
import com.example.e_vaccination.parent.drawer.fragments.AllAppointment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HospitalDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser hospital;
    private DatabaseReference reference;

    private String HospitalID;

    TextView NameTV;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        NameTV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.Name);

        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_open, R.string.menu_close);

        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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
                    NameTV.setText(HospitalName);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HospitalDrawer.this,"Something wrong happened!",Toast.LENGTH_LONG).show();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllAppointment()).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AllAppointment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllAppointmentHospital()).commit();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}