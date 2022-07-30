package com.example.e_vaccination.parent.drawer.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.e_vaccination.R;
import com.example.e_vaccination.model.ParentBookAppointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BookAppointment extends Fragment implements View.OnClickListener {

    Button SubmitB;
    TextView DateAppointmentTV, TimeTV;
    Spinner ChildNameS,HospitalNameS;
    Calendar cal;

    View view;

    private FirebaseUser parent;
    private String ParentID;

    List<String> arr_child,arr_hospital;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_book_appointment, container, false);
        SubmitB = view.findViewById(R.id.Submit);
        DateAppointmentTV = view.findViewById(R.id.DateOfAppointment);
        TimeTV = view.findViewById(R.id.Time);
        ChildNameS = view.findViewById(R.id.ChildName);
        HospitalNameS = view.findViewById(R.id.HospitalName);

        cal = Calendar.getInstance();
        DateAppointmentTV.setOnClickListener(this);
        TimeTV.setOnClickListener(this);
        SubmitB.setOnClickListener(this);

        parent = FirebaseAuth.getInstance().getCurrentUser();
        ParentID=parent.getUid();

        arr_child = new ArrayList();
        arr_hospital = new ArrayList();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Parent").child(ParentID).child("Children").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        arr_child.add(ds.getKey().toString());
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, arr_child);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ChildNameS.setAdapter(dataAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Hospital").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        arr_hospital.add(ds.child("HospitalName").getValue().toString());
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, arr_hospital);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        HospitalNameS.setAdapter(dataAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private final TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @SuppressLint("SetTextI18n")
        public void onTimeSet(TimePicker timePicker, int Hour, int Minutes) {
            int hour = Hour>12?Hour-12:Hour;
            hour=hour==0?12:hour;
            String day=Hour>=12?"PM":"AM";
            TimeTV.setText(hour + ":" + Minutes+" "+day);
        }
    };

    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            DateAppointmentTV.setText(day1 + "/" + month1 + "/" + year1);

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DateOfAppointment:

                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        R.style.Theme_Evaccination, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the Date of Appointment");
                datePicker.show();
                break;
            case R.id.Time:
                TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                        R.style.Theme_Evaccination,
                        timePickerListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE), false);
                timePicker.setCancelable(false);
                timePicker.setTitle("Select the Time of Appointment");
                timePicker.show();
                break;
            case R.id.Submit:
                bookAppointment();
                break;
        }
    }

    private void bookAppointment() {
        String ChildName = ChildNameS.getSelectedItem().toString().trim();
        String DoA = DateAppointmentTV.getText().toString().trim();
        String Time = TimeTV.getText().toString().trim();
        String Hospital = HospitalNameS.getSelectedItem().toString().trim();

        if (DoA.isEmpty()) {
            DateAppointmentTV.setError("Date of Appointment is required!");
            DateAppointmentTV.requestFocus();
            return;
        }

        if (Time.isEmpty()) {
            TimeTV.setError("Time of Appointment is required!");
            TimeTV.requestFocus();
            return;
        }

        ParentBookAppointment parentBookAppointment = new ParentBookAppointment(DoA,Time,ChildName,Hospital);
        FirebaseDatabase.getInstance().getReference("Parent").child(ParentID).child("Appointment").child(Hospital).setValue(parentBookAppointment);
        getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllAppointment()).commitNow();
    }
}