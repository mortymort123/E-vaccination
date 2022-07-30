package com.example.e_vaccination.admin.drawer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e_vaccination.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HospitalListAdmin extends Fragment {
    LinearLayout layout_container;
    View view;
    private DatabaseReference reference;
    LayoutInflater inf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_hospital_list, container, false);
        inf = inflater;
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout_container = view.findViewById(R.id.container);

        reference = FirebaseDatabase.getInstance().getReference("Hospital");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String HospitalName = ds.child("HospitalName").getValue().toString();
                        String address = ds.child("Address").getValue().toString();
                        AddNew(HospitalName, address, ds.getKey());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void AddNew(String hospitalName, String address, String key) {
        @SuppressLint("InflateParams") View anotherView = inf.inflate(R.layout.group_admin_hospital_list, null);
        ((TextView) anotherView.findViewById(R.id.HospitalName)).setText(hospitalName);
        ((TextView) anotherView.findViewById(R.id.HospitalAddress)).setText(address);


        anotherView.findViewById(R.id.Delete).setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference("Hospital").child(key).removeValue();
            getParentFragmentManager().beginTransaction().detach(this).commitNow();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookAppointmentAdmin()).commit();
        });
        layout_container.addView(anotherView);
    }

}