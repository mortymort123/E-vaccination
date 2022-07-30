package com.example.e_vaccination.admin.drawer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e_vaccination.R;
import com.example.e_vaccination.parent.drawer.fragments.ViewChild;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GetChildDetailAdmin extends Fragment {

    LinearLayout layout_container;

    private DatabaseReference reference;

    View view;
    LayoutInflater inf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_get_child_detail, container, false);
        inf=inflater;
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
                        if(dataSnapshot1.hasChild("Children")){
                            for(DataSnapshot ds: dataSnapshot1.child("Children").getChildren()){
                                String Name = ds.getKey();
                                String Age = ds.child("Age").getValue().toString();
                                String DoB = ds.child("DoB").getValue().toString();
                                String Gender = ds.child("Gender").getValue().toString();
                                String Weight = ds.child("Weight").getValue().toString();
                                String Height = ds.child("Height").getValue().toString();
                                AddNew(Name, Age, DoB, Gender, Weight, Height,dataSnapshot1.getKey());
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
    private void AddNew(String name, String age, String DoB, String gender, String weight, String height,String userID) {
        @SuppressLint("InflateParams") View anotherView = inf.inflate(R.layout.group_admin_get_child, null);
        ((TextView) anotherView.findViewById(R.id.ChildName)).setText(name);
        ((TextView) anotherView.findViewById(R.id.DateOfBirth)).setText(DoB);
        ((TextView) anotherView.findViewById(R.id.Age)).setText(age);
        ((TextView) anotherView.findViewById(R.id.Gender)).setText(gender);
        ((TextView) anotherView.findViewById(R.id.Weight)).setText(weight);
        ((TextView) anotherView.findViewById(R.id.Height)).setText(height);
        anotherView.findViewById(R.id.Delete).setOnClickListener(view -> {
            reference = FirebaseDatabase.getInstance().getReference("Parent").child(userID).child("Children");
            reference.child(name).removeValue();
            getParentFragmentManager().beginTransaction().detach(this).commitNow();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new GetChildDetailAdmin()).commit();
        });
        layout_container.addView(anotherView);
    }


}