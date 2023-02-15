package com.example.anigo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Japanese implements Serializable, Parcelable {

    public  int id;
    public  String name;
    public int animeId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.animeId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.animeId = source.readInt();
    }

    public Japanese() {
    }

    protected Japanese(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.animeId = in.readInt();
    }

    public static final Parcelable.Creator<Japanese> CREATOR = new Parcelable.Creator<Japanese>() {
        @Override
        public Japanese createFromParcel(Parcel source) {
            return new Japanese(source);
        }

        @Override
        public Japanese[] newArray(int size) {
            return new Japanese[size];
        }
    };
}
