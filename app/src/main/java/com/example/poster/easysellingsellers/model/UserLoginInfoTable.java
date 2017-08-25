package com.example.poster.easysellingsellers.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 018 18.08.17.
 */

public class UserLoginInfoTable {
    private String name;
    private String lastName;
    private String surname;
    private String photoUrl;
    private long mobileNumber;
    private String email;
    private String uID;
    private String companyUid;

    public UserLoginInfoTable() {
    }

    public UserLoginInfoTable(String name, String lastName, String surname, String photoUrl, long mobileNumber, String email, String uID, String companyUid) {
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.photoUrl = photoUrl;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.uID = uID;
        this.companyUid = companyUid;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("lastName", lastName);
        result.put("surname", surname);
        result.put("photoUrl", photoUrl);
        result.put("mobileNumber", mobileNumber);
        result.put("email", email);
        result.put("uID", uID);
        result.put("companyUid", companyUid);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getCompanyUid() {
        return companyUid;
    }

    public void setCompanyUid(String companyUid) {
        this.companyUid = companyUid;
    }
}
