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

    /* public Japanese[] japaneses;*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nameEng);
        dest.writeString(this.nameRus);
        dest.writeString(this.description);
        dest.writeFloat(this.scoreShiki);
        dest.writeByte(this.ongoing ? (byte) 1 : (byte) 0);
        dest.writeLong(this.nextEpisodeAt != null ? this.nextEpisodeAt.getTime() : -1);
        dest.writeInt(this.shikiId);
        dest.writeInt(this.franchizeId);
        dest.writeInt(this.typeId);
        dest.writeInt(this.duration);
        dest.writeInt(this.episodes);
        dest.writeLong(this.releasedOn != null ? this.releasedOn.getTime() : -1);
        dest.writeSerializable(this.franchize);
        dest.writeParcelable(this.type, flags);
        dest.writeTypedArray(this.images, flags);
        dest.writeTypedArray(this.japaneses, flags);
        dest.writeTypedArray(this.screenshots, flags);
        dest.writeTypedArray(this.genres, flags);
        dest.writeTypedArray(this.studios, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.nameEng = source.readString();
        this.nameRus = source.readString();
        this.description = source.readString();
        this.scoreShiki = source.readFloat();
        this.ongoing = source.readByte() != 0;
        long tmpNextEpisodeAt = source.readLong();
        this.nextEpisodeAt = tmpNextEpisodeAt == -1 ? null : new Date(tmpNextEpisodeAt);
        this.shikiId = source.readInt();
        this.franchizeId = source.readInt();
        this.typeId = source.readInt();
        this.duration = source.readInt();
        this.episodes = source.readInt();
        long tmpReleasedOn = source.readLong();
        this.releasedOn = tmpReleasedOn == -1 ? null : new Date(tmpReleasedOn);
        this.franchize = (Franchize) source.readSerializable();
        this.type = source.readParcelable(Type.class.getClassLoader());
        this.images = source.createTypedArray(Image.CREATOR);
        this.japaneses = source.createTypedArray(Japanese.CREATOR);
        this.screenshots = source.createTypedArray(Screenshot.CREATOR);
        this.genres = source.createTypedArray(Genre.CREATOR);
        this.studios = source.createTypedArray(Studio.CREATOR);
    }

    public Anime() {
    }

    protected Anime(Parcel in) {
        this.id = in.readInt();
        this.nameEng = in.readString();
        this.nameRus = in.readString();
        this.description = in.readString();
        this.scoreShiki = in.readFloat();
        this.ongoing = in.readByte() != 0;
        long tmpNextEpisodeAt = in.readLong();
        this.nextEpisodeAt = tmpNextEpisodeAt == -1 ? null : new Date(tmpNextEpisodeAt);
        this.shikiId = in.readInt();
        this.franchizeId = in.readInt();
        this.typeId = in.readInt();
        this.duration = in.readInt();
        this.episodes = in.readInt();
        long tmpReleasedOn = in.readLong();
        this.releasedOn = tmpReleasedOn == -1 ? null : new Date(tmpReleasedOn);
        this.franchize = (Franchize) in.readSerializable();
        this.type = in.readParcelable(Type.class.getClassLoader());
        this.images = in.createTypedArray(Image.CREATOR);
        this.japaneses = in.createTypedArray(Japanese.CREATOR);
        this.screenshots = in.createTypedArray(Screenshot.CREATOR);
        this.genres = in.createTypedArray(Genre.CREATOR);
        this.studios = in.createTypedArray(Studio.CREATOR);
    }

    public static final Parcelable.Creator<Anime> CREATOR = new Parcelable.Creator<Anime>() {
        @Override
        public Anime createFromParcel(Parcel source) {
            return new Anime(source);
        }

        @Override
        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };
}
