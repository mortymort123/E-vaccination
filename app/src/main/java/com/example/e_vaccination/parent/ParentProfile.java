package com.example.e_vaccination.parent;

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
import com.example.e_vaccination.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentProfile extends AppCompatActivity {

   private FirebaseUser parent;
    private DatabaseReference reference;

    private String ParentID;

    private Button logout;
    private TextView FullNameTV, EmailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        logout=(Button) findViewById(R.id.logout);
        FullNameTV=(TextView) findViewById(R.id.FullName);
        EmailTV=(TextView) findViewById(R.id.Email);

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ParentProfile.this, MainActivity.class));
        });

        parent = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Parent");
        ParentID=parent.getUid();

        reference.child(ParentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Parent parentProfile = snapshot.getValue(Parent.class);
                if(parentProfile!=null){
                    String FirstName = parentProfile.FirstName;
                    String LastName = parentProfile.LastName;
                    String Email = parentProfile.Email;

                     FullNameTV.setText("Welcome "+LastName + ", "+FirstName+"!");
                     EmailTV.setText(Email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ParentProfile.this,"Something wrong happened!",Toast.LENGTH_LONG).show();
            }
        });
    }
}