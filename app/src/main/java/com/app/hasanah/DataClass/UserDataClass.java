package com.app.hasanah.DataClass;

public class UserDataClass {
    private String userName,email,dob,photo,user_id;

    public UserDataClass() {
    }

    public UserDataClass(String userName, String email, String dob, String photo, String user_id) {
        this.userName = userName;
        this.email = email;
        this.dob = dob;
        this.photo = photo;
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
