package com.hzhang.whotouchedwhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorHistory implements Serializable {
    private Map<String, Long> contributorFreqMap;
    List<Author> authors = new ArrayList<>();

    public AuthorHistory() {
        contributorFreqMap = new HashMap<>();
    }

    public void addContributor(String contributorName, int count) {
        contributorFreqMap.put(contributorName,
                contributorFreqMap.getOrDefault(contributorName, (long)0) + count);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void convertMapToList() {
        for (Map.Entry<String, Long> entry : contributorFreqMap.entrySet()){
            authors.add(new Author(entry.getKey(), entry.getValue()));
        }

        authors.sort((o1, o2) -> {
            if (o1.commitCount == o2.commitCount) {
                return 0;
            }
            else {
                return o1.commitCount > o2.commitCount ? -1 : 1;
            }
        });
    }
}
