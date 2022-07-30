package com.example.e_vaccination;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_vaccination.hospital.HospitalLogin;
import com.example.e_vaccination.parent.ParentLogin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button hospital,parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hospital=(Button)findViewById(R.id.menu_hospital);
        parent=(Button) findViewById(R.id.menu_parent);

        hospital.setOnClickListener(this);
        parent.setOnClickListener(this);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.menu_hospital:
                startActivity(new Intent(this, HospitalLogin.class));
                break;
            case R.id.menu_parent:
                startActivity(new Intent(this, ParentLogin.class));
                break;
        }
    }
}