package com.app.hasanah.WebApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RespnseName implements Serializable
{

    @SerializedName("image_base64")
    @Expose
    private String image_base64;
    @SerializedName("is_pose")
    @Expose
    private boolean is_pose;

    public RespnseName(String image_base64, boolean is_pose) {
        this.image_base64 = image_base64;
        this.is_pose = is_pose;
    }

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    public boolean isIs_pose() {
        return is_pose;
    }

    public void setIs_pose(boolean is_pose) {
        this.is_pose = is_pose;
    }
}