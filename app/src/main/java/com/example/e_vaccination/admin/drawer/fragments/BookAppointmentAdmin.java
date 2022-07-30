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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e_vaccination.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BookAppointmentAdmin extends Fragment {
    LinearLayout layout_container;
    View view;
    private DatabaseReference reference;
    LayoutInflater inf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_book_appointment, container, false);
        inf = inflater;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout_container = view.findViewById(R.id.container);

        reference = FirebaseDatabase.getInstance().getReference("Parent");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.hasChild("Appointment")) {
                            for (DataSnapshot ds : dataSnapshot1.child("Appointment").getChildren()) {
                                if (!(ds.hasChild("Status"))) {
                                    String HospitalName = ds.getKey();
                                    String ChildName = ds.child("ChildName").getValue().toString();
                                    String DoA = ds.child("DoA").getValue().toString();
                                    String Time = ds.child("Time").getValue().toString();
                                    AddNew(HospitalName, ChildName, DoA, Time, dataSnapshot1.getKey());
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void AddNew(String hospitalName, String childName, String doA, String time, String key) {
        @SuppressLint("InflateParams") View anotherView = inf.inflate(R.layout.group_admin_book_appointment, null);
        ((TextView) anotherView.findViewById(R.id.HospitalName)).setText(hospitalName);
        ((TextView) anotherView.findViewById(R.id.ChildName)).setText(childName);
        ((TextView) anotherView.findViewById(R.id.DateOfAppointment)).setText(doA);
        ((TextView) anotherView.findViewById(R.id.Time)).setText(time);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.status, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) anotherView.findViewById(R.id.Status)).setAdapter(adapter);

        anotherView.findViewById(R.id.Update).setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference("Parent").child(key).child("Appointment").child(hospitalName).child("Status").setValue("Approved");
            getParentFragmentManager().beginTransaction().detach(this).commitNow();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookAppointmentAdmin()).commit();
        });
        layout_container.addView(anotherView);
    }

}