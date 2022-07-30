package com.example.e_vaccination.model;

public class ParentBookAppointment {
    public String DoA,Time,ChildName,HospitalName;
    public ParentBookAppointment(){

    }
    public ParentBookAppointment(String DoA,String Time,String ChildName,String HospitalName){
        this.DoA=DoA;
        this.Time=Time;
        this.ChildName=ChildName;
        this.HospitalName=HospitalName;
    }
}
