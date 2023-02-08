package com.example.anigo;

public class RequestOptions {
    public static String request_url_login = "http://192.168.0.102:81/api/Login";
    public static String request_url_register = "http://192.168.0.102:81/api/Login/Register";
    public static String request_url_animes_get = "http://192.168.0.102:81/api/Anime/GetAnimes?page=%o&search=%s";
    public static String request_url_anime_get = "http://192.168.0.102:81/api/Anime/GetAnime?id=";
    public static String request_url_user_get = "http://192.168.0.102:81/api/Administration/GetUserByCredentials?login=%s&password=%s";
    public static String request_url_screens_get = "http://192.168.0.102:81/api/Anime/GetAnimeScreens?id=";

}
