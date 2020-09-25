package com.football.matches.livescores.data;

import android.app.Application;
import android.widget.Toast;

import com.football.matches.livescores.pojo.CountriesJsonResult;
import com.football.matches.livescores.pojo.
        EventJsonResult;
import com.football.matches.livescores.pojo.
        FixturesJsonResult;
import com.football.matches.livescores.pojo.
        LeagueJsonResult;
import com.football.matches.livescores.pojo.
        LineupsJsonResult;
import com.football.matches.livescores.pojo.NewsJsonResult;
import com.football.matches.livescores.pojo.
        PredictionsJsonResult;
import com.football.matches.livescores.pojo.
        StandingsJsonResult;
import com.football.matches.livescores.pojo.
        TeamsJsonResult;
import com.football.matches.livescores.pojo.PlayerStatisticsJsonResult;
import com.football.matches.livescores.pojo.TransfersJsonResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.QueryMap;

public class ApiClient {


    public String baseUrl = "https://api-football-beta.p.rapidapi.com/";
    static private ApiInterface apiInterface;
    private static ApiClient instance;
    static public int SEASON ;

    private ApiClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH)+1 >= 8)
            SEASON = calendar.get(Calendar.YEAR);
        else
            SEASON = calendar.get(Calendar.YEAR)-1;
    }

    public static ApiClient getInstance() {
        if (instance == null)
            instance = new ApiClient();

        return instance;
    }

    public Call<CountriesJsonResult> getcountries() {
        return apiInterface.getCountries();
    }

    public Call<TeamsJsonResult> getCountryTeams(String country) {

        return apiInterface.getCountryTeams(country);
    }

    public Call<TeamsJsonResult> getSearchedTeams(String search) {

        return apiInterface.getSearchedTeams(search);
    }

    public Call<LeagueJsonResult> getTeamLeagues(int teamId) {
        return apiInterface.getTeamLeagues(teamId);
    }

    public Call<LeagueJsonResult> getSearchedLeagues(String search) {
        return apiInterface.getSearchedLeagues(search);
    }

    static final String TIMEZONE = TimeZone.getDefault().getID();

    public Call<FixturesJsonResult> getTeamMatches(int leagueId, int teamId) {
        return apiInterface.getTeamFixures(leagueId, teamId, SEASON, TIMEZONE);
    }


    public Call<FixturesJsonResult> getLeagueMatches(int leagueId) {
        return apiInterface.getLeagueFixtures(leagueId, SEASON , TIMEZONE);
    }

    public Call<FixturesJsonResult> getGeneralFixtures(Map<String, String> map) {
        map.put("season", String.valueOf(SEASON));
        map.put("timezone", TIMEZONE);
        return apiInterface.getGeneralFixtures(map);
    }

    public Call<StandingsJsonResult> getTeamStandings(int leagueId) {
        int season = Calendar.getInstance().get(Calendar.YEAR) - 1;
        return apiInterface.getLeagueStandings(leagueId, season);
    }

    public Call<FixturesJsonResult> getTeamNextMatch(int teamId) {
        return apiInterface.getTeamNextFixture(teamId, 1, TIMEZONE);
    }

    public Call<PredictionsJsonResult> getPredictions(int fixtureNumber) {
        return apiInterface.getPredictions(fixtureNumber);
    }

    public Call<EventJsonResult> getScorersEvents(int fixtureId) {
        return apiInterface.getScorerEvents("goal", fixtureId);
    }


    public Call<EventJsonResult> getEvents(int fixtureId) {
        return apiInterface.getEvents(fixtureId);
    }


    public Call<LineupsJsonResult> getLineUps(int fixtureId) {
        return apiInterface.getLineUps(fixtureId);
    }

    public Call<LeagueJsonResult> getLeagues(String country) {
        return apiInterface.getLeagues(country, "" + SEASON);
    }

    public Call<TeamsJsonResult> getLeagueTeams(int leagueId) {
        return apiInterface.getLeagueTeams(leagueId, SEASON);
    }

    public Call<PlayerStatisticsJsonResult> getTopScorerStatistics(int leagueId) {
        return apiInterface.getTopScorerStatistics(SEASON, leagueId);
    }

    public Call<PlayerStatisticsJsonResult> getSquadPlayers(int teamId) {
        return apiInterface.getSquadPlayers(teamId, SEASON);
    }

    public Call<TransfersJsonResult> getTransfers(int teamId) {
        return apiInterface.getTransfers(teamId);
    }

}
