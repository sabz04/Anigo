package com.example.anigo.Models;

public class AnimeCommentAddClass {
    int userId;
    int animeId;
    String comment;

    public AnimeCommentAddClass(int userId, int animeId, String commentAnime) {
        this.userId = userId;
        this.animeId = animeId;
        this.comment = commentAnime;
    }
}
