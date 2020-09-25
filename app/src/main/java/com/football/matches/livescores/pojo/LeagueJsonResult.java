package com.football.matches.livescores.pojo;

import com.football.matches.livescores.pojo.LeagueResponceObject;

import java.util.List;

public class LeagueJsonResult {
    List <LeagueResponceObject> response ;

    public List<LeagueResponceObject> getResponse() {
        return response;
    }

    public void setResponse(List<LeagueResponceObject> response) {
        this.response = response;
    }
}
