package com.example.e_vaccination.admin.drawer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e_vaccination.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddVaccineAdmin extends Fragment {

    LinearLayout layout_container;

    private DatabaseReference reference;

    EditText VaccineET;
    Button CreateB;

    View view;
    LayoutInflater inf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_add_vaccine, container, false);
        inf=inflater;
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VaccineET = view.findViewById(R.id.VaccineName);
        CreateB = view.findViewById(R.id.Create);

        CreateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaccine = VaccineET.getText().toString().trim();
                if(vaccine.isEmpty()){
                    VaccineET.setError("Empty Vaccine Name!");
                    VaccineET.requestFocus();
                    return;
                }
                FirebaseDatabase.getInstance().getReference("Vaccine").child(vaccine).setValue("Available");
                getParentFragmentManager().beginTransaction().detach(AddVaccineAdmin.this).commitNow();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddVaccineAdmin()).commit();
            }
        });

        layout_container = view.findViewById(R.id.container);
        reference = FirebaseDatabase.getInstance().getReference("Vaccine");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String Name = ds.getKey();
                        @SuppressLint("InflateParams") View anotherView = inf.inflate(R.layout.group_admin_add_vaccine, null);
                        ((TextView) anotherView.findViewById(R.id.Vaccine)).setText(Name);
                        anotherView.findViewById(R.id.Delete).setOnClickListener(view -> {
                            FirebaseDatabase.getInstance().getReference("Vaccine").child(Name).removeValue();
                            getParentFragmentManager().beginTransaction().detach(AddVaccineAdmin.this).commitNow();
                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddVaccineAdmin()).commit();
                        });
                        layout_container.addView(anotherView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}