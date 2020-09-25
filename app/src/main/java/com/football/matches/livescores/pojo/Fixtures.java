package com.football.matches.livescores.pojo;

public class Fixtures {
    int id ;
    String date , referee ;
    Status status ;
    Venue venue;

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public String getReferee() {
        return referee;
    }

    public Venue getVenue() {
        return venue;
    }
}
