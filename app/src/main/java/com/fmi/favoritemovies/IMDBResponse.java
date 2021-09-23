package com.fmi.favoritemovies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IMDBResponse {
    @JsonProperty("searchType")
    private String searchType;
    @JsonProperty("expression")
    private String expression;
    @JsonProperty("results")
    private List<MovieResult> results = null;
    @JsonProperty("errorMessage")
    private String errorMessage;


    public IMDBResponse() {}


    public IMDBResponse(String searchType, String expression, List<MovieResult> results, String errorMessage) {
        super();
        this.searchType = searchType;
        this.expression = expression;
        this.results = results;
        this.errorMessage = errorMessage;
    }

    @JsonProperty("searchType")
    public String getSearchType() {
        return searchType;
    }

    @JsonProperty("searchType")
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    @JsonProperty("expression")
    public String getExpression() {
        return expression;
    }

    @JsonProperty("expression")
    public void setExpression(String expression) {
        this.expression = expression;
    }

    @JsonProperty("results")
    public List<MovieResult> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<MovieResult> results) {
        this.results = results;
    }

    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("errorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
