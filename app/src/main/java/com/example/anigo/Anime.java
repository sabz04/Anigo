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

    public Date nextEpisodeAt;

    public int shikiId;

    /*public Date AiredOn;*/

    public  int franchizeId;

    public  int typeId;

    public  int duration;

    public int episodes;

    public Date releasedOn;

    public com.example.anigo.Franchize franchize;

    public Type type;

    public Image[] images;

    /*public Image[] images;*/
    public Japanese[] japaneses;

   /* public Japanese[] japaneses;*/

}
