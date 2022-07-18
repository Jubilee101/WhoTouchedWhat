package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory implements Serializable {
    @JsonProperty("title")
    private String name;
    @JsonProperty("children")
    private List<Directory> subDirectories;
    @JsonProperty("authors")
    List<Author> authors;
    @JsonIgnore
    private String path;

    public Directory() {
        subDirectories = new ArrayList<>();
    }

    public List<Author> getLineChanges() {
        if (this.authors == null) {
            this.authors = new ArrayList<>();
            AuthorHistory history = new AuthorHistory();
            for (Directory child : subDirectories) {
                List<Author> childHistory = child.getLineChanges();
                for (Author author : childHistory) {
                    history.addContributor(author.name, author.commitCount);
                }
            }
            history.convertMapToList();
            authors = history.getAuthors();
        }
        return this.authors;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
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
