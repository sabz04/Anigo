package com.example.anigo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class UserResponse implements Serializable {
    public String token;
    public User user;
}
