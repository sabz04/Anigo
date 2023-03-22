package com.example.anigo.Models;

import java.util.Date;

public class AnimeComment {
    public int id;
    public int userId ;
    public int animeId ;
    public String comment;
    public  User user;
    public Date date;
    public CommentLiked[] commentLikeds;
}
