package com.example.poster.easysellingsellers.model;

/**
 * Created by POSTER on 15.07.2017.
 */

public class UploadPhotoModel {
    private String userUid;
    private String photoUrl;

    public UploadPhotoModel(String userUid, String photoUrl) {
        this.userUid = userUid;
        this.photoUrl = photoUrl;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
