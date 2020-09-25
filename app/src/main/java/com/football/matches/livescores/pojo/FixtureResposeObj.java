package com.football.matches.livescores.pojo;


public class FixtureResposeObj {

    Fixtures fixture;
    MatchTeams teams;
    Goals goals ;
    League league ;

    public Fixtures getFixture() {
        return fixture;
    }

    public MatchTeams getTeams() {
        return teams;
    }

    public Goals getGoals() {
        return goals;
    }

    public League getLeague() {
        return league;
    }
}
