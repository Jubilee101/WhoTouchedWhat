package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Directory implements Serializable {
    @JsonProperty("title")
    private String name;
    @JsonProperty("children")
    private List<Directory> subDirectories;
    @JsonProperty("authors")
    List<Author> authors = new ArrayList<>();
    @JsonIgnore
    private String path;
    @JsonIgnore
    AuthorHistory history;

    public Directory() {
        subDirectories = new ArrayList<>();
    }

    public void getLineChanges() {

    }

    public List<Directory> getSubDirectories() {
        return this.subDirectories;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDirectory(Directory directory) {
        subDirectories.add(directory);
    }

}
