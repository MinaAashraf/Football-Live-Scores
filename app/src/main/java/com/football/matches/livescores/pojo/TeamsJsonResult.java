package com.football.matches.livescores.pojo;

import com.football.matches.livescores.pojo.TeamResponseObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamsJsonResult {
    @SerializedName("response")
    List <TeamResponseObject> response;

    public List<TeamResponseObject> getResponse() {
        return response;
    }

    public void setResponse(List<TeamResponseObject> response) {
        this.response = response;
    }
}
