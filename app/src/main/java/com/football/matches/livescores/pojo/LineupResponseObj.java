package com.football.matches.livescores.pojo;

import java.util.List;

public class LineupResponseObj {
    Team team ;
    Coach coach;
    String formation ;
    List <LineUp> startXI;
    List <LineUp> substitutes;

    public Team getTeam() {
        return team;
    }

    public Coach getCoach() {
        return coach;
    }

    public String getFormation() {
        return formation;
    }

    public List<LineUp> getStartXI() {
        return startXI;
    }

    public List<LineUp> getSubstitutes() {
        return substitutes;
    }
}
