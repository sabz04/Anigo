package com.example.anigo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Genre implements Serializable, Parcelable {

    public int id ;
    public int shikiId ;
    public String nameEng ;
    public String nameRus ;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.shikiId);
        dest.writeString(this.nameEng);
        dest.writeString(this.nameRus);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.shikiId = source.readInt();
        this.nameEng = source.readString();
        this.nameRus = source.readString();
    }

    public Genre() {
    }

    protected Genre(Parcel in) {
        this.id = in.readInt();
        this.shikiId = in.readInt();
        this.nameEng = in.readString();
        this.nameRus = in.readString();
    }

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
