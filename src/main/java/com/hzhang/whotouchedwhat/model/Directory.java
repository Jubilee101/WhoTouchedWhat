package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Directory implements Serializable {
    @JsonProperty("title")
    private String name;
    @JsonProperty("children")
    private List<Directory> subDirectories;
    @JsonProperty("file_path")
    private String path;

    public String getRepoAddress() {
        return repoAddress;
    }

    public void setRepoAddress(String repoAddress) {
        this.repoAddress = repoAddress;
    }

    @JsonProperty("repo_address")
    private String repoAddress;
    public Directory() {
        subDirectories = new ArrayList<>();
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
