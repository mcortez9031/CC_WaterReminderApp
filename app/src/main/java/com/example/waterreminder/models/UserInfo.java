package com.example.waterreminder.models;

public class UserInfo {
    String name, email, gender, activityLevel, weather;
    int age, weight;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getActivityLevel() {
        return activityLevel;
    }
    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getWeather() {
        return weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public UserInfo(String name,String email, String gender, int weight,
                    int age, String activityLevel, String weather){
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.weather = weather;
    }
}
