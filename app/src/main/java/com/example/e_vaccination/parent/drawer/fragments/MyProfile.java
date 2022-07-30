package com.example.e_vaccination.parent.drawer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e_vaccination.R;
import com.example.e_vaccination.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyProfile extends Fragment {

    View view;
    EditText FirstNameET, LastNameET;
    TextView EmailTV;

    private FirebaseUser parent;
    private DatabaseReference reference;

    private String ParentID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parent_my_profile, container, false);

        EmailTV = (TextView) view.findViewById(R.id.Email);
        FirstNameET = (EditText) view.findViewById(R.id.FirstName);
        LastNameET = (EditText) view.findViewById(R.id.LastName);
        ((Button) view.findViewById(R.id.Update)).setOnClickListener(view -> {
            DatabaseReference dr= FirebaseDatabase.getInstance().getReference("Parent").child(ParentID);
            dr.child("FirstName").setValue(FirstNameET.getText().toString());
            dr.child("LastName").setValue(LastNameET.getText().toString());
            getParentFragmentManager().beginTransaction().detach(this).commitNow();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfile()).commit();
        });
        parent = FirebaseAuth.getInstance().getCurrentUser();
        ParentID = parent.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Parent").child(ParentID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Parent parentProfile = dataSnapshot.getValue(Parent.class);
                if (parentProfile != null) {
                    EmailTV.setText(parentProfile.Email);
                    FirstNameET.setText(parentProfile.FirstName);
                    LastNameET.setText(parentProfile.LastName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
}