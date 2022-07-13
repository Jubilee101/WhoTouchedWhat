package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AuthorHistory implements Serializable {
    @JsonProperty("contributor_freq")
    private Map<String, Integer> contributorFreqMap;

    public AuthorHistory() {
        contributorFreqMap = new HashMap<>();
    }

    public void addContributor(String contributorName) {
        contributorFreqMap.put(contributorName,
                contributorFreqMap.getOrDefault(contributorName, 0) + 1);
    }
}
