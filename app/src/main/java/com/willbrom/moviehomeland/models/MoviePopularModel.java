package com.willbrom.moviehomeland.models;

import java.util.List;

public class MoviePopularModel {
    private int page;
    private int totalResults;
    private int totalPages;
    private List<MovieResults> results;

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<MovieResults> getResults() {
        return results;
    }
}
