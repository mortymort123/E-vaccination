package com.example.e_vaccination.parent.drawer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.e_vaccination.MainActivity;
import com.example.e_vaccination.R;
import com.example.e_vaccination.model.Parent;
import com.example.e_vaccination.parent.drawer.fragments.AddChild;
import com.example.e_vaccination.parent.drawer.fragments.AllAppointment;
import com.example.e_vaccination.parent.drawer.fragments.BookAppointment;
import com.example.e_vaccination.parent.drawer.fragments.MyProfile;
import com.example.e_vaccination.parent.drawer.fragments.ViewChild;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser parent;
    private DatabaseReference reference;

    private String ParentID;

    TextView NameTV;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_drawer);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddChild()).commit();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        NameTV = navigationView.getHeaderView(0).findViewById(R.id.Name);

        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_open, R.string.menu_close);

        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        parent = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Parent/");
        ParentID = parent.getUid();

        reference.child(ParentID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Parent parentProfile = snapshot.getValue(Parent.class);
                if (parentProfile != null) {
                    String FirstName = parentProfile.FirstName;
                    String LastName = parentProfile.LastName;
                    NameTV.setText(LastName + ", " + FirstName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddChild:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddChild()).commitNow();
                break;
            case R.id.ViewChild:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewChild()).commitNow();
                break;
            case R.id.BookAppointment:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BookAppointment()).commitNow();
                break;
            case R.id.AllAppointment:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllAppointment()).commitNow();
                break;
            case R.id.MyProfile:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyProfile()).commitNow();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ParentDrawer.this, MainActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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