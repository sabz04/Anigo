package com.example.anigo.RequestsHelper;

public class RequestOptions {
    public static String MainHost = "http://192.168.0.102:81";
    public static String request_url_login              = MainHost + "/api/Login";
    public static String request_url_register           = MainHost + "/api/Login/Register";
    public static String request_url_animes_get         = MainHost + "/api/Anime/GetAnimes?page=%d&search=%s";
    public static String request_url_anime_get          = MainHost + "/api/Anime/GetAnime?id=";
    public static String request_url_user_get           = MainHost + "/api/Administration/GetUserByCredentials?login=%s&password=%s";
    public static String request_url_screens_get        = MainHost + "/api/Anime/GetAnimeScreens?id=";
    public static String request_url_add_to_favs        = MainHost + "/api/User/AddFavourite";
    public static String request_url_check_in_fav       = MainHost + "/api/User/CheckFav?anime_id=%d&user_id=%d";
    public static String request_url_delete_from_fav    = MainHost + "/api/User/DeleteFromFav?anime_id=%d&user_id=%d";
    public static String request_url_get_favs           = MainHost + "/api/User/GetFavs?page=%d&user_id=%d";
    public static String request_url_get_code           = MainHost + "/api/Login/GetCode?email=";
    public static String request_url_change_pass        = MainHost + "/api/Login/ChangePass?email=%s&password=%s";
    public static String request_url_get_popular        = MainHost + "/api/Anime/GetPopular?page=%d";
    public static String request_url_get_comments       = MainHost + "/api/Comment/GetAnimeComments?animeId=%d&page=%d";
    public static String request_url_add_comment        = MainHost + "/api/Comment/CreateAnimeComment";
    public static String request_url_add_like           = MainHost + "/api/Comment/LikeComment";
    public static String request_url_remove_comment     = MainHost + "/api/Comment/RemoveComment/";
    public static String request_url_like_delete        = MainHost + "/api/Comment/LikeDelete?commentId=%d&userId=%d";
}
