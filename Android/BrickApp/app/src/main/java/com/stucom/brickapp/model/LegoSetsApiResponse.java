package com.stucom.brickapp.model;

import java.util.List;

public class LegoSetsApiResponse {
    private int count;
    private String next;
    private String previous;
    private List<LegoSet> results;

    public LegoSetsApiResponse(int count, String next, String previous, List<LegoSet> results) {
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

    public List<LegoSet> getResults() {
        return results;
    }

    public void setResults(List<LegoSet> results) {
        this.results = results;
    }
}
