package com.football.matches.livescores.ui;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.football.matches.livescores.data.ApiClient;
import com.football.matches.livescores.data.ApiInterface;
import com.football.matches.livescores.pojo.Article;
import com.football.matches.livescores.pojo.ArticlesWithqTitle;
import com.football.matches.livescores.pojo.CountriesJsonResult;
import com.football.matches.livescores.pojo.Country;
import com.football.matches.livescores.pojo.NewsJsonResult;
import com.football.matches.livescores.pojo.PlayerData;
import com.football.matches.livescores.pojo.SearchPlayers;
import com.football.matches.livescores.pojo.TeamFromAnotherApi;
import com.football.matches.livescores.pojo.TeamObjectFromAnotherApi;
import com.football.matches.livescores.pojo.TeamsJsonResult;
import com.football.matches.livescores.pojo.EventJsonResult;
import com.football.matches.livescores.pojo.EventResponceObj;
import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.FixturesJsonResult;
import com.football.matches.livescores.pojo.LeagueResponceObject;
import com.football.matches.livescores.pojo.LineupResponseObj;
import com.football.matches.livescores.pojo.LineupsJsonResult;
import com.football.matches.livescores.pojo.PredictionsJsonResult;
import com.football.matches.livescores.pojo.PredictionsResponceObj;
import com.football.matches.livescores.pojo.StandingObj;
import com.football.matches.livescores.pojo.StandingsJsonResult;
import com.football.matches.livescores.pojo.Team;
import com.football.matches.livescores.pojo.LeagueJsonResult;
import com.football.matches.livescores.pojo.TeamResponseObject;
import com.football.matches.livescores.pojo.PlayerStatisticsJsonResult;
import com.football.matches.livescores.pojo.PlayerStatisticsObj;
import com.football.matches.livescores.pojo.Transfer;
import com.football.matches.livescores.pojo.TransferResponceObj;
import com.football.matches.livescores.pojo.TransfersJsonResult;
import com.football.matches.livescores.pojo.VideoResponceObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<List<Country>> countries;
    private MutableLiveData<List<Team>> teams;
    private MutableLiveData<List<Team>> searchedTeams;
    private MutableLiveData<List<Team>> leagueTeams;
    private MutableLiveData<List<FixtureResposeObj>> teamFixures;
    private MutableLiveData<List<LeagueResponceObject>> teamLeagues;
    private MutableLiveData<List<LeagueResponceObject>> searchedLeagues;
    private MutableLiveData<List<LeagueResponceObject>> countryLeagues;
    private MutableLiveData<List<List<StandingObj>>> leagueStandings;
    private MutableLiveData<List<FixtureResposeObj>> teamNextFixture;
    private MutableLiveData<List<PredictionsResponceObj>> predictions;
    private MutableLiveData<List<EventResponceObj>> scorersEvents;
    private MutableLiveData<List<EventResponceObj>> events;
    private MutableLiveData<List<LineupResponseObj>> lineUps;
    private MutableLiveData<List<List<FixtureResposeObj>>> leagueFixtures;
    private MutableLiveData<List<List<FixtureResposeObj>>> generalFixtures;
    private MutableLiveData<List<PlayerStatisticsObj>> topScorerStatistics;
    private MutableLiveData<List<List<TransferResponceObj>>> tranfers;
    private MutableLiveData<List<PlayerData>> searchedPlayers;
    private MutableLiveData<List<PlayerStatisticsObj>> squadPlayers;
    private MutableLiveData<List<TeamFromAnotherApi>> teamImageFromAnotherApi;
    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<List<List<Article>>> articlesByTitleList;
    private MutableLiveData<List<VideoResponceObj>> videos;


    Application application;

    public MyViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.generalFixtures = new MutableLiveData<>();
        this.articlesByTitleList = new MutableLiveData<>();
        this.teamImageFromAnotherApi = new MutableLiveData<>();
        tranfers = new MutableLiveData<>();
        leagueFixtures = new MutableLiveData<>();
    }

    public MutableLiveData<List<Country>> getCountries() {
        if (countries == null) {
            countries = new MutableLiveData<>();
            loadCountries();
        }
        return countries;
    }

    public static String mycountryName = "";

    public MutableLiveData<List<Team>> getTeams(String countryName) {
        if (teams == null || !mycountryName.equals(countryName)) {
            mycountryName = countryName;
            teams = new MutableLiveData<List<Team>>();
            loadTeams();
        }
        return teams;
    }

    String search = "";

    public MutableLiveData<List<Team>> getSearchedTeams(String search) {
        if (searchedTeams == null || !this.search.equals(search)) {
            this.search = search;
            searchedTeams = new MutableLiveData<>();
            loadSearchedTeams();
        }
        return searchedTeams;
    }

    int leg_Id = -1;

    public MutableLiveData<List<Team>> getLeagueTeams(int leagueId) {
        if (leagueTeams == null || leg_Id != leagueId) {
            this.leg_Id = leagueId;
            leagueTeams = new MutableLiveData<>();
            loadLeagueTeams();
        }
        return leagueTeams;
    }

    private int leagueId = -1, teamId = -1;

    public MutableLiveData<List<FixtureResposeObj>> getTeamFixures(int leagueId, int teamId) {
        teamFixures = new MutableLiveData<>();
        this.leagueId = leagueId;
        this.teamId = teamId;
        loadTeamFixures();
        return teamFixures;
    }

    int tmId = -1;

    public MutableLiveData<List<LeagueResponceObject>> getTeamLeagues(int teamId) {
        if (teamLeagues == null || tmId != teamId) {
            teamLeagues = new MutableLiveData<>();
            tmId = teamId;
            loadTeamLeagues();
        }
        return teamLeagues;
    }

    private int standingLeagueId = -1;

    public MutableLiveData<List<List<StandingObj>>> getLeagueStandings(int leagueId) {
        if (leagueStandings == null || standingLeagueId != leagueId) {
            leagueStandings = new MutableLiveData<>();
            standingLeagueId = teamId;
            standingLeagueId = leagueId;
            loadTeamStandings();
        }
        return leagueStandings;
    }

    int teamidnext = -1;

    public MutableLiveData<List<FixtureResposeObj>> getTeamNextFixture(int teamId) {
        if (teamNextFixture == null || teamidnext != teamId) {
            teamNextFixture = new MutableLiveData<>();
            teamidnext = teamId;
            loadTeamNextFixture();
        }
        return teamNextFixture;
    }

    int fixtureNumber = -1;

    public MutableLiveData<List<PredictionsResponceObj>> getPredictions(int fixtureNumber) {
        if (predictions == null || this.fixtureNumber != fixtureNumber) {
            predictions = new MutableLiveData<>();
            this.fixtureNumber = fixtureNumber;
            loadPredictions();
        }
        return predictions;
    }

    int fixtureId = -1;

    public MutableLiveData<List<EventResponceObj>> getScorersEvents(int fixtureId) {
        if (scorersEvents == null || this.fixtureId != fixtureId) {
            scorersEvents = new MutableLiveData<>();
            this.fixtureId = fixtureId;
            loadScorerEvents();
        }
        return scorersEvents;
    }

    public MutableLiveData<List<EventResponceObj>> getEvents(int fixtureId) {
        events = new MutableLiveData<>();
        this.fixtureId = fixtureId;
        loadEvents();
        return events;
    }

    public MutableLiveData<List<LineupResponseObj>> getLineUps(int fixtureId) {
        if (lineUps == null || this.fixtureId != fixtureId) {
            lineUps = new MutableLiveData<>();
            this.fixtureId = fixtureId;
            loadLineUps();
        }
        return lineUps;
    }

    String country = "";

    public MutableLiveData<List<LeagueResponceObject>> getCountryLeagues(String country) {
        if (countryLeagues == null || !this.country.equals(country)) {
            countryLeagues = new MutableLiveData<>();
            this.country = country;
            loadLeagues();
        }
        return countryLeagues;
    }

    public MutableLiveData<List<LeagueResponceObject>> getSearchedLeagues(String search) {
        if (searchedLeagues == null || !this.search.equals(search)) {
            this.search = search;
            searchedLeagues = new MutableLiveData<>();
            loadSearchedLeagues();
        }
        return searchedLeagues;
    }

    int legId = -1;

    public MutableLiveData<List<List<FixtureResposeObj>>> getLeagueFixtures(int leagueId) {
        if (this.legId != leagueId) {
            this.legId = leagueId;
            loadLeagueFixtures();
        }
        return leagueFixtures;
    }

    Map<String, String> map = new HashMap<>();

    public MutableLiveData<List<List<FixtureResposeObj>>> getGeneralFixtures(Map<String, String> map) {
        if (!this.map.equals(map)) {
            this.map = new HashMap<>(map);
            loadGeneralFixtures();
        }
        return generalFixtures;
    }

    int leag_id = -1;

    public MutableLiveData<List<PlayerStatisticsObj>> getTopScorerStatistics(int leagueId) {
        if (topScorerStatistics == null || leag_id != leagueId) {
            leag_id = leagueId;
            topScorerStatistics = new MutableLiveData<>();
            loadTopScorerStatistics();
        }
        return topScorerStatistics;
    }

    public MutableLiveData<List<PlayerData>> getSearchedPlayers(String search) {
        if (searchedPlayers == null || !this.search.equals(search)) {
            this.search = search;
            searchedPlayers = new MutableLiveData<>();
            loadSearchedPlayers();
        }
        return searchedPlayers;
    }

    int tId = -1;

    public MutableLiveData<List<PlayerStatisticsObj>> getSquadPlayers(int teamId) {
        if (squadPlayers == null || tmId != teamId) {
            tId = teamId;
            squadPlayers = new MutableLiveData<>();
            loadSquadPlayers();
        }
        return squadPlayers;
    }

    String teamName = "";

    public MutableLiveData<List<TeamFromAnotherApi>> getTeamImageFromAnotherApi(String teamName) {
        if (!this.teamName.equals(teamName)) {
            this.teamName = teamName;
            loadTeamImage();
        }
        return teamImageFromAnotherApi;
    }

    String[] searchTopic = {""};
    int pageSize = 50;

    public MutableLiveData<List<Article>> getArticles(String[] search, int pageSize) {
        if (articles == null) {
            searchTopic = search.clone();
            this.pageSize = pageSize;
            articles = new MutableLiveData<>();
            loadArticles();
        }
        return articles;
    }

    String searchTitle = "";

    public MutableLiveData<List<List<Article>>> getArticlesByTitle(String searchTitle, int pageSize) {
        this.pageSize = pageSize;
        this.searchTitle = searchTitle;
        loadArticlesByTitle();
        return articlesByTitleList;
    }


    public MutableLiveData<List<VideoResponceObj>> getVideos() {
        if (videos == null) {
            videos = new MutableLiveData<>();
            loadVideos();
        }
        return videos;
    }

    int t_id;

    public MutableLiveData<List<List<TransferResponceObj>>> getTranfers(int teamId) {
        this.t_id = teamId;
        loadTranfers();
        return tranfers;
    }


    private void loadSquadPlayers() {
        Call<PlayerStatisticsJsonResult> call = ApiClient.getInstance().getSquadPlayers(tId);
        call.enqueue(new Callback<PlayerStatisticsJsonResult>() {
            @Override
            public void onResponse(Call<PlayerStatisticsJsonResult> call, Response<PlayerStatisticsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                squadPlayers.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<PlayerStatisticsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadGeneralFixtures() {
        Call<FixturesJsonResult> call = ApiClient.getInstance().getGeneralFixtures(map);
        call.enqueue(new Callback<FixturesJsonResult>() {
            @Override
            public void onResponse(Call<FixturesJsonResult> call, Response<FixturesJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<List<FixtureResposeObj>> totalList = new ArrayList<>();
                if (generalFixtures.getValue() != null && !generalFixtures.getValue().isEmpty())
                    totalList.addAll(generalFixtures.getValue());
                totalList.add(response.body().getResponse());
                generalFixtures.setValue(totalList);
            }

            @Override
            public void onFailure(Call<FixturesJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadLeagueFixtures() {
        Call<FixturesJsonResult> call = ApiClient.getInstance().getLeagueMatches(this.legId);
        call.enqueue(new Callback<FixturesJsonResult>() {
            @Override
            public void onResponse(Call<FixturesJsonResult> call, Response<FixturesJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<List<FixtureResposeObj>> totalList = new ArrayList<>();
                if (leagueFixtures.getValue() != null && !leagueFixtures.getValue().isEmpty())
                    totalList.addAll(leagueFixtures.getValue());
                totalList.add(response.body().getResponse());
                leagueFixtures.setValue(totalList);
            }

            @Override
            public void onFailure(Call<FixturesJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadLeagues() {
        Call<LeagueJsonResult> call = ApiClient.getInstance().getLeagues(country);
        call.enqueue(new Callback<LeagueJsonResult>() {
            @Override
            public void onResponse(Call<LeagueJsonResult> call, Response<LeagueJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                countryLeagues.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<LeagueJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSearchedLeagues() {
        Call<LeagueJsonResult> call = ApiClient.getInstance().getSearchedLeagues(search);
        call.enqueue(new Callback<LeagueJsonResult>() {
            @Override
            public void onResponse(Call<LeagueJsonResult> call, Response<LeagueJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                searchedLeagues.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<LeagueJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadLineUps() {
        Call<LineupsJsonResult> call = ApiClient.getInstance().getLineUps(fixtureId);
        call.enqueue(new Callback<LineupsJsonResult>() {
            @Override
            public void onResponse(Call<LineupsJsonResult> call, Response<LineupsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                lineUps.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<LineupsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadEvents() {
        Call<EventJsonResult> call = ApiClient.getInstance().getEvents(fixtureId);
        call.enqueue(new Callback<EventJsonResult>() {
            @Override
            public void onResponse(Call<EventJsonResult> call, Response<EventJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                events.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<EventJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadScorerEvents() {
        Call<EventJsonResult> call = ApiClient.getInstance().getScorersEvents(fixtureId);
        call.enqueue(new Callback<EventJsonResult>() {
            @Override
            public void onResponse(Call<EventJsonResult> call, Response<EventJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                scorersEvents.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<EventJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadCountries() {
        Call<CountriesJsonResult> call = ApiClient.getInstance().getcountries();
        call.enqueue(new Callback<CountriesJsonResult>() {
            @Override
            public void onResponse(Call<CountriesJsonResult> call, Response<CountriesJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                countries.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<CountriesJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTeams() {
        Call<TeamsJsonResult> call = ApiClient.getInstance().getCountryTeams(mycountryName);
        call.enqueue(new Callback<TeamsJsonResult>() {
            @Override
            public void onResponse(Call<TeamsJsonResult> call, Response<TeamsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<Team> teamsList = new ArrayList<>();
                for (TeamResponseObject object : response.body().getResponse()) {
                    teamsList.add(object.getTeam());
                }
                teams.setValue(teamsList);
            }

            @Override
            public void onFailure(Call<TeamsJsonResult> call, Throwable t) {
                Toast.makeText(application, "There is a problem", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void loadSearchedTeams() {
        Call<TeamsJsonResult> call = ApiClient.getInstance().getSearchedTeams(search);
        call.enqueue(new Callback<TeamsJsonResult>() {
            @Override
            public void onResponse(Call<TeamsJsonResult> call, Response<TeamsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<Team> teams = new ArrayList<>();
                for (TeamResponseObject object : response.body().getResponse())
                    teams.add(object.getTeam());
                searchedTeams.setValue(teams);
            }

            @Override
            public void onFailure(Call<TeamsJsonResult> call, Throwable t) {
                Toast.makeText(application, "There is a problem", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTeamFixures() {
        Call<FixturesJsonResult> call = ApiClient.getInstance().getTeamMatches(leagueId, teamId);
        call.enqueue(new Callback<FixturesJsonResult>() {
            @Override
            public void onResponse(Call<FixturesJsonResult> call, Response<FixturesJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                teamFixures.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<FixturesJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTeamLeagues() {
        Call<LeagueJsonResult> call = ApiClient.getInstance().getTeamLeagues(tmId);
        call.enqueue(new Callback<LeagueJsonResult>() {
            @Override
            public void onResponse(Call<LeagueJsonResult> call, Response<LeagueJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                teamLeagues.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<LeagueJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTeamStandings() {
        Call<StandingsJsonResult> call = ApiClient.getInstance().getTeamStandings(standingLeagueId);
        call.enqueue(new Callback<StandingsJsonResult>() {
            @Override
            public void onResponse(Call<StandingsJsonResult> call, Response<StandingsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                leagueStandings.setValue(response.body().getResponse().get(0).getLeague().getStandings());
            }

            @Override
            public void onFailure(Call<StandingsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void loadTeamNextFixture() {
        Call<FixturesJsonResult> call = ApiClient.getInstance().getTeamNextMatch(teamidnext);
        call.enqueue(new Callback<FixturesJsonResult>() {
            @Override
            public void onResponse(Call<FixturesJsonResult> call, Response<FixturesJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                teamNextFixture.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<FixturesJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadPredictions() {
        Call<PredictionsJsonResult> call = ApiClient.getInstance().getPredictions(fixtureNumber);
        call.enqueue(new Callback<PredictionsJsonResult>() {
            @Override
            public void onResponse(Call<PredictionsJsonResult> call, Response<PredictionsJsonResult> response) {
                if (!response.isSuccessful())
                    return;
                predictions.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<PredictionsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadLeagueTeams() {
        Call<TeamsJsonResult> call = ApiClient.getInstance().getLeagueTeams(leg_Id);
        call.enqueue(new Callback<TeamsJsonResult>() {
            @Override
            public void onResponse(Call<TeamsJsonResult> call, Response<TeamsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<Team> teams = new ArrayList<>();
                for (TeamResponseObject object : response.body().getResponse())
                    teams.add(object.getTeam());
                leagueTeams.setValue(teams);
            }

            @Override
            public void onFailure(Call<TeamsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTopScorerStatistics() {
        Call<PlayerStatisticsJsonResult> call = ApiClient.getInstance().getTopScorerStatistics(leag_id);
        call.enqueue(new Callback<PlayerStatisticsJsonResult>() {
            @Override
            public void onResponse(Call<PlayerStatisticsJsonResult> call, Response<PlayerStatisticsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                topScorerStatistics.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<PlayerStatisticsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSearchedPlayers() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://thesportsdb.p.rapidapi.com/").addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<SearchPlayers> call = apiInterface.getSearchPlayers(search);
        call.enqueue(new Callback<SearchPlayers>() {
            @Override
            public void onResponse(Call<SearchPlayers> call, Response<SearchPlayers> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                searchedPlayers.setValue(response.body().getPlayer());
            }

            @Override
            public void onFailure(Call<SearchPlayers> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTeamImage() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://thesportsdb.p.rapidapi.com/").addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<TeamObjectFromAnotherApi> call = apiInterface.getTeamPictureFromAnotherApi(this.teamName);
        call.enqueue(new Callback<TeamObjectFromAnotherApi>() {
            @Override
            public void onResponse(Call<TeamObjectFromAnotherApi> call, Response<TeamObjectFromAnotherApi> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<TeamFromAnotherApi> totalList = new ArrayList<>();
                if (teamImageFromAnotherApi.getValue() != null && !teamImageFromAnotherApi.getValue().isEmpty())
                    totalList.addAll(teamImageFromAnotherApi.getValue());
                if (response.body().getTeams() != null)
                    totalList.add(response.body().getTeams().get(0));
                teamImageFromAnotherApi.setValue(totalList);
            }

            @Override
            public void onFailure(Call<TeamObjectFromAnotherApi> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadArticles() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://newsapi.org/v2/").addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<NewsJsonResult> call = apiInterface.getArticles(searchTopic, "publishedAt", pageSize, "en", "082b95fc4353449ba67390f97ba07d9c");
        call.enqueue(new Callback<NewsJsonResult>() {
            @Override
            public void onResponse(Call<NewsJsonResult> call, Response<NewsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                articles.setValue(response.body().getArticles());
            }

            @Override
            public void onFailure(Call<NewsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadArticlesByTitle() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://newsapi.org/v2/").addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<NewsJsonResult> call = apiInterface.getArticlesByTitle(searchTitle, "publishedAt", pageSize, "en", "082b95fc4353449ba67390f97ba07d9c");
        call.enqueue(new Callback<NewsJsonResult>() {
            @Override
            public void onResponse(Call<NewsJsonResult> call, Response<NewsJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<List<Article>> toalArticles = new ArrayList<>();
                if (articlesByTitleList.getValue() != null && !articlesByTitleList.getValue().isEmpty())
                    toalArticles.addAll(articlesByTitleList.getValue());
                toalArticles.add(response.body().getArticles());
                articlesByTitleList.setValue(toalArticles);
            }

            @Override
            public void onFailure(Call<NewsJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void loadVideos() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://free-football-soccer-videos1.p.rapidapi.com/").addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<VideoResponceObj>> call = apiInterface.getVideos();
        call.enqueue(new Callback<List<VideoResponceObj>>() {
            @Override
            public void onResponse(Call<List<VideoResponceObj>> call, Response<List<VideoResponceObj>> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                videos.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<VideoResponceObj>> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    private void loadTranfers() {
        Call<TransfersJsonResult> call = ApiClient.getInstance().getTransfers(this.t_id);
        call.enqueue(new Callback<TransfersJsonResult>() {
            @Override
            public void onResponse(Call<TransfersJsonResult> call, Response<TransfersJsonResult> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 429)
                        Toast.makeText(application, "Sorry ,the functionality is not working due to API limitations", Toast.LENGTH_LONG).show();
                    return;
                }
                List<List<TransferResponceObj>> totalList = new ArrayList<>();
                if (tranfers.getValue() != null && !tranfers.getValue().isEmpty())
                    totalList.addAll(tranfers.getValue());
                totalList.add(response.body().getResponse());
                tranfers.setValue(totalList);
            }

            @Override
            public void onFailure(Call<TransfersJsonResult> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

