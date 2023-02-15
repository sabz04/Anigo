package com.example.anigo.Models;

import com.example.anigo.Models.Anime;

import java.io.Serializable;

public class AnimeResponse implements Serializable {
    public Anime[] animes;
    public int currentPage;
    public int pages;
    public int currentPageItemCount;
}
