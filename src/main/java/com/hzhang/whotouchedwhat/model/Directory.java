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
}
