package com.fmi.favoritemovies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MovieResult implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("resultType")
    private String resultType;
    @JsonProperty("image")
    private String image;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;

    private boolean watched;
    private int rating;

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public MovieResult() { }


    public MovieResult(String id, String resultType, String image, String title, String description) {
        super();
        this.id = id;
        this.resultType = resultType;
        this.image = image;
        this.title = title;
        this.description = description;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("resultType")
    public String getResultType() {
        return resultType;
    }

    @JsonProperty("resultType")
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
