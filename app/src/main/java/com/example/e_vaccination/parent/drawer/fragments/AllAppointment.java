package com.example.e_vaccination.parent.drawer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e_vaccination.R;
import com.example.e_vaccination.hospital.HospitalProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AllAppointment extends Fragment {

    LinearLayout layout_container;
    View view;

    private FirebaseUser parent;
    private DatabaseReference reference;

    private String ParentID;
    LayoutInflater inf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inf = inflater;
        view = inflater.inflate(R.layout.fragment_parent_all_apointment, container, false);
        layout_container = view.findViewById(R.id.container);
        parent = FirebaseAuth.getInstance().getCurrentUser();
        ParentID = parent.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Parent").child(ParentID).child("Appointment");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String Name = ds.child("ChildName").getValue().toString();
                    String DoA = ds.child("DoA").getValue().toString();
                    String HospitalName = ds.child("HospitalName").getValue().toString();
                    String Time = ds.child("Time").getValue().toString();
                    String Status = ds.hasChild("Status") ? ds.child("Status").getValue().toString() : "";
                    String Vaccine = ds.hasChild("Vaccine") ? ds.child("Vaccine").getValue().toString() : "";
                    AddNew(Name, DoA, HospitalName, Time, Status, Vaccine);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void AddNew(String name, String DoA, String HospitalName, String Time, String Status, String Vaccine) {
        View anotherView = inf.inflate(R.layout.group_parent_all_appointment, null);
        ((TextView) anotherView.findViewById(R.id.ChildName)).setText(name);
        ((TextView) anotherView.findViewById(R.id.DateOfAppointment)).setText(DoA);
        ((TextView) anotherView.findViewById(R.id.HospitalName)).setText(HospitalName);
        ((TextView) anotherView.findViewById(R.id.Time)).setText(Time);
        ((TextView) anotherView.findViewById(R.id.Status)).setText(Status.isEmpty() ? "Pending" : Status);
        ((TextView) anotherView.findViewById(R.id.Vaccine)).setText(Vaccine.isEmpty() ? "Pending" : Vaccine);
        layout_container.addView(anotherView);
    }
}