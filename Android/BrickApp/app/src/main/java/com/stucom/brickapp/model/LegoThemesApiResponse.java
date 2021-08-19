package com.stucom.brickapp.model;

import java.util.ArrayList;
import java.util.List;

public class LegoThemesApiResponse {
    private int count;
    private String next;
    private String previous;
    private List<LegoTheme> results;

    public LegoThemesApiResponse(int count, String next, String previous, List<LegoTheme> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<LegoTheme> getResults() {
        return results;
    }

    public void setResults(List<LegoTheme> results) {
        this.results = results;
    }

    public LegoThemes getFirstLevelThemes() {
        LegoThemes themes = new LegoThemes();
        for(LegoTheme theme : results) {
            if (theme.getParentId() == null) {
                themes.add(theme);
            }
        }
        return themes;
    }
}
