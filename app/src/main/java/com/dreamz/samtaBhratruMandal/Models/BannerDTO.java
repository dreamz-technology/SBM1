package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class
BannerDTO {

    @SerializedName("heading")
    private String heading;

    @SerializedName("imageURL")
    private String imageURL;

    @SerializedName("id")
    private int id;

    public String getHeading() {
        return heading;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getId() {
        return id;
    }
}