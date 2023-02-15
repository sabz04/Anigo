package com.example.anigo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Image implements Serializable, Parcelable {
    public int id;
    public String original;
    public String preview;

    public int animeId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.original);
        dest.writeString(this.preview);
        dest.writeInt(this.animeId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.original = source.readString();
        this.preview = source.readString();
        this.animeId = source.readInt();
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.id = in.readInt();
        this.original = in.readString();
        this.preview = in.readString();
        this.animeId = in.readInt();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
