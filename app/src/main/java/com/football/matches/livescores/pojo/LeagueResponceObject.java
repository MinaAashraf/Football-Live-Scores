package com.football.matches.livescores.pojo;

import java.util.List;

public class LeagueResponceObject {
    League league ;
    List <Season> seasons ;


    public League getLeague() {
        return league;
    }

   public Season getSeason ()
   {
       return seasons.get(seasons.size()-1);
   }
}
