package com.stucom.brickapp.model;

import com.google.gson.annotations.SerializedName;

public class LegoTheme {
    private int id;
    @SerializedName("parent_id")
    private Integer parentId;
    private String name;

    public LegoTheme(int id, Integer parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
