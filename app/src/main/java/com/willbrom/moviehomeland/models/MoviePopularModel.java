package com.willbrom.moviehomeland.models;

import java.io.Serializable;
import java.util.List;

public class MoviePopularModel implements Serializable {
    private int page;
    private int total_results;
    private int total_pages;
    private List<MovieResultsModel> results;

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<MovieResultsModel> getResults() {
        return results;
    }
}
