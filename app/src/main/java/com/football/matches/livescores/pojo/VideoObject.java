package com.football.matches.livescores.pojo;

import java.util.ArrayList;
import java.util.List;

public class VideoObject {
    List<String> htmlEmbeds ;
    List<String> titles ;
    String date ;

    public VideoObject(List<String> htmlEmbeds, List<String> titles, String date) {
        this.htmlEmbeds = htmlEmbeds;
        this.titles = titles;
        this.date = date;
    }

    public List<String> getHtmlEmbeds() {
        return htmlEmbeds;
    }

    public List<String> getTitles() {
        return titles;
    }

    public String getDate() {
        return date;
    }
}
