package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {
    @JsonProperty("author_name")
    String name;
    @JsonProperty("commit_count")
    int commitCount;
    @JsonProperty("y_axis")
    String yAxis;
    public Author(String name, int count) {
        this.name = name;
        commitCount = count;
        yAxis = "Authors";
    }
}
