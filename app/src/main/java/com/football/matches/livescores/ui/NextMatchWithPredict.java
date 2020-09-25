package com.football.matches.livescores.ui;

import com.football.matches.livescores.pojo.FixtureResposeObj;
import com.football.matches.livescores.pojo.GenralTeamNews;
import com.football.matches.livescores.pojo.PredictionPercent;

public class NextMatchWithPredict extends GenralTeamNews {
    FixtureResposeObj fixtureResposeObj ;
    PredictionPercent percent ;

    public NextMatchWithPredict(FixtureResposeObj fixtureResposeObj, PredictionPercent percent) {
        this.fixtureResposeObj = fixtureResposeObj;
        this.percent = percent;
    }

    public FixtureResposeObj getFixtureResposeObj() {
        return fixtureResposeObj;
    }

    public PredictionPercent getPercent() {
        return percent;
    }
}
