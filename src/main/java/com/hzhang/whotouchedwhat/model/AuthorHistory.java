package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorHistory implements Serializable {
    private Map<String, Integer> contributorFreqMap;
    @JsonProperty("authors")
    List<Author> authors = new ArrayList<>();

    public AuthorHistory() {
        contributorFreqMap = new HashMap<>();
    }

    public void addContributor(String contributorName) {
        contributorFreqMap.put(contributorName,
                contributorFreqMap.getOrDefault(contributorName, 0) + 1);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void convertMapToList() {
        for (Map.Entry<String, Integer> entry : contributorFreqMap.entrySet()){
            authors.add(new Author(entry.getKey(), entry.getValue()));
        }
    }
}
