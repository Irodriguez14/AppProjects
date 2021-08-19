package com.stucom.brickapp.model;

import com.google.gson.annotations.SerializedName;

public class LegoSet {
    @SerializedName("set_num")
    private String setNum;
    private String name;
    private int year;
    @SerializedName("theme_id")
    private int themeId;
    @SerializedName("num_parts")
    private int numParts;
    @SerializedName("set_img_url")
    private String setImgUrl;
    @SerializedName("set_url")
    private String setUrl;
    @SerializedName("last_modified_dt")
    private String lastModifiedDt;

    public LegoSet(String setNum, String name, int year, int themeId, int numParts, String setImgUrl, String setUrl, String lastModifiedDt) {
        this.setNum = setNum;
        this.name = name;
        this.year = year;
        this.themeId = themeId;
        this.numParts = numParts;
        this.setImgUrl = setImgUrl;
        this.setUrl = setUrl;
        this.lastModifiedDt = lastModifiedDt;
    }

    public String getSetNum() {
        return setNum;
    }

    public void setSetNum(String setNum) {
        this.setNum = setNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getNumParts() {
        return numParts;
    }

    public void setNumParts(int numParts) {
        this.numParts = numParts;
    }

    public String getSetImgUrl() {
        return setImgUrl;
    }

    public void setSetImgUrl(String setImgUrl) {
        this.setImgUrl = setImgUrl;
    }

    public String getSetUrl() {
        return setUrl;
    }

    public void setSetUrl(String setUrl) {
        this.setUrl = setUrl;
    }

    public String getLastModifiedDt() {
        return lastModifiedDt;
    }

    public void setLastModifiedDt(String lastModifiedDt) {
        this.lastModifiedDt = lastModifiedDt;
    }
}
