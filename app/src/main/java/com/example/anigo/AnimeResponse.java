package com.example.anigo;

import java.io.Serializable;

public class AnimeResponse implements Serializable {
    public Anime[] animes;
    public int currentPage;
    public int pages;
    public int currentPageItemCount;
}
