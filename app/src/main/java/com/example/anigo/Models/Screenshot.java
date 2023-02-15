package com.example.anigo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Screenshot implements Serializable, Parcelable {
    public int id ;
    public String image ;
    public int animeId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.image);
        dest.writeInt(this.animeId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.image = source.readString();
        this.animeId = source.readInt();
    }

    public Screenshot() {
    }

    protected Screenshot(Parcel in) {
        this.id = in.readInt();
        this.image = in.readString();
        this.animeId = in.readInt();
    }

    public static final Creator<Screenshot> CREATOR = new Creator<Screenshot>() {
        @Override
        public Screenshot createFromParcel(Parcel source) {
            return new Screenshot(source);
        }

        @Override
        public Screenshot[] newArray(int size) {
            return new Screenshot[size];
        }
    };
}
