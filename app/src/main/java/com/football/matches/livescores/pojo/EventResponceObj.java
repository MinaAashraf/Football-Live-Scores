package com.football.matches.livescores.pojo;

public class EventResponceObj {
    Time time ;
    Team team;
    Player player;
    Assist assist;
    String type;

    public String getDetail() {
        return detail;
    }

    String detail;

    public String getType() {
        return type;
    }

    public Assist getAssist() {
        return assist;
    }

    public Time getTime() {
        return time;
    }

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }
}
