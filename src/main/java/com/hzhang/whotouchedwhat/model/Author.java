package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {
    @JsonProperty("author_name")
    String name;
    @JsonProperty("commit_count")
    long commitCount;
    @JsonProperty("y_axis")
    String yAxis;
    @JsonProperty("color")
    String color;
    public Author(String name, long count) {
        this.name = name;
        commitCount = count;
        yAxis = "Authors";
    }
    public String getName(){
        return this.name;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public Author() {
    }
}
