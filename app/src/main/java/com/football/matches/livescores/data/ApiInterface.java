package com.football.matches.livescores.data;

import com.football.matches.livescores.pojo.CountriesJsonResult;
import com.football.matches.livescores.pojo.EventJsonResult;
import com.football.matches.livescores.pojo.FixturesJsonResult;
import com.football.matches.livescores.pojo.LeagueJsonResult;
import com.football.matches.livescores.pojo.LineupsJsonResult;
import com.football.matches.livescores.pojo.NewsJsonResult;
import com.football.matches.livescores.pojo.PredictionsJsonResult;
import com.football.matches.livescores.pojo.SearchPlayers;
import com.football.matches.livescores.pojo.StandingsJsonResult;
import com.football.matches.livescores.pojo.TeamObjectFromAnotherApi;
import com.football.matches.livescores.pojo.TeamsJsonResult;
import com.football.matches.livescores.pojo.PlayerStatisticsJsonResult;
import com.football.matches.livescores.pojo.TransfersJsonResult;
import com.football.matches.livescores.pojo.VideoResponceObj;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("countries")
    Call<CountriesJsonResult> getCountries();

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("teams")
    Call<TeamsJsonResult> getCountryTeams(@Query("country") String country);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("teams")
    Call<TeamsJsonResult> getSearchedTeams(@Query("search") String search);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("leagues")
    Call<LeagueJsonResult> getTeamLeagues(@Query("team") int id);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("leagues")
    Call<LeagueJsonResult> getSearchedLeagues(@Query("search") String search);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("fixtures")
    Call<FixturesJsonResult> getTeamFixures(@Query("league") int leagueId, @Query("team") int teamId, @Query("season") int season, @Query("timezone") String timeZone);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("standings")
    Call<StandingsJsonResult> getLeagueStandings(@Query("league") int leagueId, @Query("season") int season);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("fixtures")
    Call<FixturesJsonResult> getTeamNextFixture(@Query("team") int teamId, @Query("next") int nextNumber, @Query("timezone") String timeZone);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("predictions")
    Call<PredictionsJsonResult> getPredictions(@Query("fixture") int fixtureNumber);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("fixtures/events")
    Call<EventJsonResult> getScorerEvents(@Query("type") String type, @Query("fixture") int fixtureId);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("fixtures/events")
    Call<EventJsonResult> getEvents(@Query("fixture") int fixtureId);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("fixtures/lineups")
    Call<LineupsJsonResult> getLineUps(@Query("fixture") int fixtureId);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("leagues")
    Call<LeagueJsonResult> getLeagues(@Query("country") String country, @Query("season") String season);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("fixtures")
    Call<FixturesJsonResult> getLeagueFixtures(@Query("league") int leagueId, @Query("season") int season , @Query("timezone") String timeZone);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("fixtures")
    Call<FixturesJsonResult> getGeneralFixtures(@QueryMap Map <String,String> map);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("teams")
    Call<TeamsJsonResult> getLeagueTeams(@Query("league") int leagueId, @Query("season") int season);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("players/topscorers")
    Call<PlayerStatisticsJsonResult> getTopScorerStatistics(@Query("season") int season, @Query("league") int league);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("players")
    Call<PlayerStatisticsJsonResult> getSquadPlayers(@Query("team") int teamId, @Query("season") int season);

    @Headers({"x-rapidapi-host:api-football-beta.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("transfers")
    Call<TransfersJsonResult> getTransfers(@Query("team") int teamId);
    //-------------------------------------------------------------------------------------------------------------------
    //Another Apis:
    @Headers({"x-rapidapi-host:thesportsdb.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("searchplayers.php")
    Call<SearchPlayers> getSearchPlayers(@Query("p") String playerName);

    @Headers({"x-rapidapi-host:thesportsdb.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("searchteams.php")
    Call<TeamObjectFromAnotherApi> getTeamPictureFromAnotherApi(@Query("t") String teamName);

   /* @Headers({"x-rapidapi-host:newscatcher.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("search_free")
    Call <NewsJsonResult> getArticles (@Query("media")boolean media ,@Query("lang")String lang , @Query("q") String search);*/

    @GET("everything")
    Call<NewsJsonResult> getArticles(@Query("q") String[] keyWords, @Query("sortBy") String sort, @Query("pageSize") int pageSize, @Query("language") String lang, @Query("apiKey") String apiKey);

    @GET("everything")
    Call<NewsJsonResult> getArticlesByTitle(@Query("qInTitle") String title, @Query("sortBy") String sort, @Query("pageSize") int pageSize, @Query("language") String lang, @Query("apiKey") String apiKey);

    @Headers({"x-rapidapi-host:free-football-soccer-videos1.p.rapidapi.com", "x-rapidapi-key:a1e12d6030msh16ea349953bc847p168dcejsnc48f3e2917e9"})
    @GET("v1/")
    Call<List<VideoResponceObj>> getVideos();



}
