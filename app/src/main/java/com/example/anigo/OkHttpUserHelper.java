package com.example.anigo;

import com.google.gson.Gson;

public class OkHttpUserHelper {

    public  static String ConvertToJsonByGson(Object obj){
        return new Gson().toJson(obj);
    }
}
