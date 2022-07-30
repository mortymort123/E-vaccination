package com.example.e_vaccination.parent.drawer.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.e_vaccination.R;
import com.example.e_vaccination.model.ParentAddChild;
import com.example.e_vaccination.parent.ParentRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class AddChild extends Fragment implements View.OnClickListener{

    EditText ChildNameET, AgeET, WeightET, HeightET;
    TextView DateBirthTV;
    Spinner GenderS;
    Button SubmitB;
    Calendar cal;
    View view;

    private FirebaseUser parent;
    private String ParentID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parent_add_child, container, false);

        AgeET = view.findViewById(R.id.Age);
        WeightET = view.findViewById(R.id.weight);
        HeightET = view.findViewById(R.id.Height);
        DateBirthTV = view.findViewById(R.id.DateOfBirth);
        GenderS = view.findViewById(R.id.Gender);
        ChildNameET = view.findViewById(R.id.ChildName);
        SubmitB = view.findViewById(R.id.Submit);



        //Gender Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.gender, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderS.setAdapter(adapter);


        cal = Calendar.getInstance();
        DateBirthTV.setOnClickListener(this);
        SubmitB.setOnClickListener(this);


        //firebase
        parent = FirebaseAuth.getInstance().getCurrentUser();
        ParentID = parent.getUid();


        return view;
    }

    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            DateBirthTV.setText(day1 + "/" + month1 + "/" + year1);

        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.DateOfBirth:
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        R.style.Theme_Evaccination, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();
                break;
            case R.id.Submit:
                addChild();
                break;
        }
    }

    private void addChild() {
        String ChildName = ChildNameET.getText().toString().trim();
        String Age = AgeET.getText().toString().trim();
        String Gender = GenderS.getSelectedItem().toString().trim();
        String DateOfBirth = DateBirthTV.getText().toString().trim();
        String Weight = WeightET.getText().toString().trim();
        String Height = HeightET.getText().toString().trim();
        if (ChildName.isEmpty()) {
            ChildNameET.setError("Name is required!");
            ChildNameET.requestFocus();
            return;
        }

        if (Age.isEmpty()) {
            AgeET.setError("Age is required!");
            AgeET.requestFocus();
            return;
        }

        if (DateOfBirth.isEmpty()) {
            DateBirthTV.setError("Date of Birth is required!");
            DateBirthTV.requestFocus();
            return;
        }

        if (Weight.isEmpty()) {
            WeightET.setError("Weight is required!");
            WeightET.requestFocus();
            return;
        }

        if (Height.isEmpty()) {
            HeightET.setError("Height is required!");
            HeightET.requestFocus();
            return;
        }
        ParentAddChild parentAddChild = new ParentAddChild(Age, DateOfBirth, Gender, Weight, Height);
        FirebaseDatabase.getInstance().getReference("Parent").child(ParentID).child("Children").child(ChildName).setValue(parentAddChild);
        getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewChild()).commitNow();

    }



}