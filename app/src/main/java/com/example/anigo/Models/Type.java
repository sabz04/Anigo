package com.example.anigo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Type implements Serializable, Parcelable {
    public  int id;
    public  String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
    }

    public Type() {
    }

    protected Type(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel source) {
            return new Type(source);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };
}
