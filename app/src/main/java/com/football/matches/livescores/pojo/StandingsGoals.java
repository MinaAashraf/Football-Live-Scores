package com.football.matches.livescores.pojo;

import com.google.gson.annotations.SerializedName;

public class StandingsGoals {
    @SerializedName("for")
    private int forTeam ;

    private int against;

    public int getForTeam() {
        return forTeam;
    }

    public int getAgainst() {
        return against;
    }
}
