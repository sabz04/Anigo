package com.example.anigo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Studio implements Serializable, Parcelable {
    public int id ;
    public int shikiId;
    public String name;
    public String image;
    public boolean real;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.shikiId);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeByte(this.real ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.shikiId = source.readInt();
        this.name = source.readString();
        this.image = source.readString();
        this.real = source.readByte() != 0;
    }

    public Studio() {
    }

    protected Studio(Parcel in) {
        this.id = in.readInt();
        this.shikiId = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.real = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Studio> CREATOR = new Parcelable.Creator<Studio>() {
        @Override
        public Studio createFromParcel(Parcel source) {
            return new Studio(source);
        }

        @Override
        public Studio[] newArray(int size) {
            return new Studio[size];
        }
    };
}
