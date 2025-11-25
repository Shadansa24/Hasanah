package com.app.hasanah.DataClass;

public class ActivityDataClass {
    private String date,image;
    private int type,status;

    public ActivityDataClass() {
    }

    public ActivityDataClass(String date, String image, int type, int status) {
        this.date = date;
        this.image = image;
        this.type = type;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
