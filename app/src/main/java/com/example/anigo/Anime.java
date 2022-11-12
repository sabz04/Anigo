package com.example.anigo;


import java.io.Serializable;
import java.util.Date;

public class Anime  implements Serializable {

    public int id;

    public  String nameEng;

    public String nameRus;

    public String description;

    public float scoreShiki;

    public boolean ongoing;

    public Date nexEpisodeAt;

    public int shikiId;

    public Date airedOn;

    public  int franchiseId;

    public  int typeId;

    public  int duration;

    public int episodes;

    public Date releasedOn;

    public Franchise franchise;

    public Type type;

    public Image[] images;

    public Japanese[] japaneses;

}
