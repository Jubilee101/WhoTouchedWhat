package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory implements Serializable {
    @JsonProperty("id")
    private String name;
    @JsonProperty("contributor_freq")
    private Map<String, Integer> contributorFreqMap;
    @JsonProperty("subdirectories")
    private List<Directory> subDirectories;

    public Directory() {
        contributorFreqMap = new HashMap<>();
        subDirectories = new ArrayList<>();
    }
    public List<Directory> getSubDirectories() {
        return this.subDirectories;
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

    public void addContributor(String contributorName) {
        contributorFreqMap.put(contributorName,
                contributorFreqMap.getOrDefault(contributorName, 0) + 1);
    }
}
