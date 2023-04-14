package com.example.anigo.Models;

import java.util.ArrayList;
import java.util.Date;

public class Result {
    public String id;
    public String type;
    public String link;
    public String title;
    public String title_orig;
    public String other_title;
    public Translation translation;
    public int year;
    public int last_season;
    public int last_episode;
    public int episodes_count;
    public String kinopoisk_id;
    public String imdb_id;
    public String worldart_link;
    public String shikimori_id;
    public String quality;
    public boolean camrip;
    public boolean lgbt;
    public ArrayList<Object> blocked_countries;
    public BlockedSeasons blocked_seasons;
    public Date created_at;
    public Date updated_at;
    public ArrayList<String> screenshots;
}
