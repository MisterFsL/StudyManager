package be.formation.studymanager.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;



public class Lesson implements Serializable{
    private String name;
    private String trainer;
    private String category;
    private int id;
    private int hours;
    private double latitude;
    private double longitude;

    public Lesson() {
    }

    public Lesson(String name, String trainer, String category, int hours) {
        this.name = name;
        this.trainer = trainer;
        this.category = category;
        this.hours = hours;
    }

    //region accesors


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        return id == lesson.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "name='" + name + '\'' +
                ", trainer='" + trainer + '\'' +
                ", category='" + category + '\'' +
                ", id=" + id +
                ", hours=" + hours +
                '}';
    }
}
