package com.football.matches.livescores.pojo;

import java.util.List;

public class VideoResponceObj {

    String title , embed , date ;
    List <Video> videos ;

    public String getTitle() {
        return title;
    }

    public String getEmbed() {
        return embed;
    }

    public String getDate() {
        return date;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
