package com.football.matches.livescores.pojo;

public class StandingObj {
    private int rank , points , goalsDiff;
    private Team team ;
    private StandingsAll all ;
    public int getRank() {
        return rank;
    }

    public int getPoints() {
        return points;
    }

    public int getGoalsDiff() {
        return goalsDiff;
    }

    public Team getTeam() {
        return team;
    }

    public StandingsAll getAll() {
        return all;
    }
}
