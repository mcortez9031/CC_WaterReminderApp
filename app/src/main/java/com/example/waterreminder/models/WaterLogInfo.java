package com.example.waterreminder.models;

public class WaterLogInfo {
    int waterIntake;
    String dateTime;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public WaterLogInfo(String id, int waterIntake, String dateTime){
        this.id = id;
        this.waterIntake = waterIntake;
        this.dateTime = dateTime;
    }
}