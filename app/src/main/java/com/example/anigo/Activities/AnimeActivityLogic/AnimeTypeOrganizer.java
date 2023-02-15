package com.example.anigo.Activities.AnimeActivityLogic;

public class AnimeTypeOrganizer {

    private static String anime_TV = "tv";

    private static String anime_OVA = "ova";

    private static String anime_movie = "movie";

    private static String anime_special = "special";

    private static String anime_music = "music";

    private static String anime_ona = "ona";



    public static String Organizer(String type){
        if(type.contains(anime_TV)) return "ТВ-сериал";
        if(type.contains(anime_movie)) return "Фильм";
        if(type.contains(anime_OVA)) return "ОВА";
        if(type.contains(anime_special)) return "Спешл";
        if(type.contains(anime_music)) return "Музыка";
        if(type.contains(anime_ona)) return "ОНА";
        return "nothing";
    }

}
