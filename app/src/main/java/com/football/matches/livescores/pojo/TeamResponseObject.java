package com.football.matches.livescores.pojo;

import com.google.gson.annotations.SerializedName;

public class TeamResponseObject {
    @SerializedName("team")
    Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}
