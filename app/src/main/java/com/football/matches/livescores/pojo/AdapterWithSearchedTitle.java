package com.football.matches.livescores.pojo;

import com.football.matches.livescores.Adapters.HomeArticlesAdapter;

public class AdapterWithSearchedTitle {
    HomeArticlesAdapter adapter ;
    String title ;

    public AdapterWithSearchedTitle(HomeArticlesAdapter adapter, String title) {
        this.adapter = adapter;
        this.title = title;
    }

    public HomeArticlesAdapter getAdapter() {
        return adapter;
    }

    public String getTitle() {
        return title;
    }
}
