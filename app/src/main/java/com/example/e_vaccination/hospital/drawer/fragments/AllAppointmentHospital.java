package com.example.e_vaccination.hospital.drawer.fragments;

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
import com.example.e_vaccination.admin.drawer.fragments.BookAppointmentAdmin;
import com.example.e_vaccination.admin.drawer.fragments.GetChildDetailAdmin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AllAppointmentHospital extends Fragment {

    private FirebaseUser hospital;
    private String HospitalID, HospitalName;
    LinearLayout layout_container;
    private DatabaseReference reference;

    View view;
    LayoutInflater inf;

    List<String> arr_vaccine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inf = inflater;
        view = inf.inflate(R.layout.fragment_hospital_all_apointment, container, false);
        arr_vaccine=new ArrayList();
        reference = FirebaseDatabase.getInstance().getReference();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospital = FirebaseAuth.getInstance().getCurrentUser();
        HospitalID = hospital.getUid();
        layout_container = view.findViewById(R.id.container);
        reference.child("Hospital").child(HospitalID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HospitalName = snapshot.child("HospitalName").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Parent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.hasChild("Appointment") &&
                                dataSnapshot1.child("Appointment").hasChild(HospitalName) &&
                                dataSnapshot1.child("Appointment").child(HospitalName).hasChild("Status") &&
                                dataSnapshot1.child("Appointment").child(HospitalName).child("Status").getValue().toString().equals("Approved") &&//TODO: most likely to have bug in hospital
                                (!(dataSnapshot1.child("Appointment").child(HospitalName).hasChild("Vaccine")))) {
                            String ChildName = dataSnapshot1.child("Appointment").child(HospitalName).child("ChildName").getValue().toString();
                            String DoA = dataSnapshot1.child("Appointment").child(HospitalName).child("DoA").getValue().toString();
                            String Hospital = dataSnapshot1.child("Appointment").child(HospitalName).child("HospitalName").getValue().toString();
                            String Status = dataSnapshot1.child("Appointment").child(HospitalName).child("Status").getValue().toString();
                            String Time = dataSnapshot1.child("Appointment").child(HospitalName).child("Time").getValue().toString();
                            AddNew(ChildName, DoA, Hospital, Status, Time,dataSnapshot1.getKey());
                        }
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void AddNew(String childName, String doA, String hospital, String status, String time,String key) {
        @SuppressLint("InflateParams") View anotherView = inf.inflate(R.layout.group_hospital_all_appointment, null);
        ((TextView) anotherView.findViewById(R.id.ChildName)).setText(childName);
        ((TextView) anotherView.findViewById(R.id.Status)).setText(status);

        ((TextView) anotherView.findViewById(R.id.DateOfAppointment)).setText(doA);
        ((TextView) anotherView.findViewById(R.id.Time)).setText(time);
        ((TextView) anotherView.findViewById(R.id.HospitalName)).setText(hospital);
        Spinner VaccineS=(Spinner) anotherView.findViewById(R.id.Vaccine);

      reference.child("Vaccine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        arr_vaccine.add(ds.getKey().toString());
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, arr_vaccine);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                       VaccineS.setAdapter(dataAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        anotherView.findViewById(R.id.Update).setOnClickListener(view -> {
           reference.child("Parent").child(key).child("Appointment").child(HospitalName).child("Vaccine").setValue(VaccineS.getSelectedItem().toString());
            getParentFragmentManager().beginTransaction().detach(this).commitNow();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllAppointmentHospital()).commit();
        });

        layout_container.addView(anotherView);
    }


}