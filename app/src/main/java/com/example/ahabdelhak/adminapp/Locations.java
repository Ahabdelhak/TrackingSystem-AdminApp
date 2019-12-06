package com.example.ahabdelhak.adminapp;

public class Locations {

    public String email,uid;
    public String lat;
    public String lng;

    public Locations(){

    }


    public Locations(String email, String lat, String lng,String uid) {
        this.email = email;
        this.lat = lat;
        this.lng = lng;
        this.uid=uid;
    }
}
