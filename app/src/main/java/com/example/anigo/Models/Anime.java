package com.example.anigo.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Anime implements Serializable, Parcelable {

    public int id;

    public  String nameEng;

    public String nameRus;

    public String description;

    public float scoreShiki;

    public boolean ongoing;

    public Date nextEpisodeAt;

    public int shikiId;

    /*public Date AiredOn;*/

    public  int franchizeId;

    public  int typeId;

    public  int duration;

    public int episodes;

    public Date releasedOn;

    public Franchize franchize;

    public Type type;

    public Image[] images;

    /*public Image[] images;*/
    public Japanese[] japaneses;

    public Screenshot[] screenshots;

    public Genre[] genres;

    public Studio[] studios;

    public AnimeRate[] animeRates;


    protected Anime(Parcel in) {
        id = in.readInt();
        nameEng = in.readString();
        nameRus = in.readString();
        description = in.readString();
        scoreShiki = in.readFloat();
        ongoing = in.readByte() != 0;
        shikiId = in.readInt();
        franchizeId = in.readInt();
        typeId = in.readInt();
        duration = in.readInt();
        episodes = in.readInt();
        type = in.readParcelable(Type.class.getClassLoader());
        images = in.createTypedArray(Image.CREATOR);
        japaneses = in.createTypedArray(Japanese.CREATOR);
        screenshots = in.createTypedArray(Screenshot.CREATOR);
        genres = in.createTypedArray(Genre.CREATOR);
        studios = in.createTypedArray(Studio.CREATOR);
    }

    public static final Creator<Anime> CREATOR = new Creator<Anime>() {
        @Override
        public Anime createFromParcel(Parcel in) {
            return new Anime(in);
        }

        @Override
        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nameEng);
        parcel.writeString(nameRus);
        parcel.writeString(description);
        parcel.writeFloat(scoreShiki);
        parcel.writeByte((byte) (ongoing ? 1 : 0));
        parcel.writeInt(shikiId);
        parcel.writeInt(franchizeId);
        parcel.writeInt(typeId);
        parcel.writeInt(duration);
        parcel.writeInt(episodes);
        parcel.writeParcelable(type, i);
        parcel.writeTypedArray(images, i);
        parcel.writeTypedArray(japaneses, i);
        parcel.writeTypedArray(screenshots, i);
        parcel.writeTypedArray(genres, i);
        parcel.writeTypedArray(studios, i);
    }
}
