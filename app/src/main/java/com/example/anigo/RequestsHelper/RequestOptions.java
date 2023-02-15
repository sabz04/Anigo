package com.example.anigo.RequestsHelper;

public class RequestOptions {
    public static String request_url_login              = "http://192.168.0.102:81/api/Login";
    public static String request_url_register           = "http://192.168.0.102:81/api/Login/Register";
    public static String request_url_animes_get         = "http://192.168.0.102:81/api/Anime/GetAnimes?page=%d&search=%s";
    public static String request_url_anime_get          = "http://192.168.0.102:81/api/Anime/GetAnime?id=";
    public static String request_url_user_get           = "http://192.168.0.102:81/api/Administration/GetUserByCredentials?login=%s&password=%s";
    public static String request_url_screens_get        = "http://192.168.0.102:81/api/Anime/GetAnimeScreens?id=";
    public static String request_url_add_to_favs        = "http://192.168.0.102:81/api/User/AddFavourite";
    public static String request_url_check_in_fav       = "http://192.168.0.102:81/api/User/CheckFav?anime_id=%d&user_id=%d";
    public static String request_url_delete_from_fav    = "http://192.168.0.102:81/api/User/DeleteFromFav?anime_id=%d&user_id=%d";
    public static String request_url_get_favs           = "http://192.168.0.102:81/api/User/GetFavs?page=%d&user_id=%d";
    public static String request_url_get_code           = "http://192.168.0.102:81/api/Login/GetCode?email=";
    public static String request_url_change_pass        = "http://192.168.0.102:81/api/Login/ChangePass?email=%s&password=%s";
    public static String request_url_get_popular        = "http://192.168.0.102:81/api/Anime/GetPopular?page=%d";
}
