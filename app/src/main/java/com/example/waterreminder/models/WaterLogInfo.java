package com.example.waterreminder.models;

public class WaterLogInfo {
    int waterIntake;
    String dateTime;

    public int getWaterIntake() {
        return waterIntake;
    }
    public String getDateTime(){
        return dateTime;
    }

    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
    }
    public void setDateTime(String date){
        this.dateTime = date;
    }

    public WaterLogInfo(int waterIntake, String dateTime){
        this.waterIntake = waterIntake;
        this.dateTime = dateTime;

    }
}