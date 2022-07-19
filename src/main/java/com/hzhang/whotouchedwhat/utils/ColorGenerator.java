package com.hzhang.whotouchedwhat.utils;

import com.hzhang.whotouchedwhat.model.Author;
import com.hzhang.whotouchedwhat.model.Directory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ColorGenerator {
    private Set<String> usedColor;
    private Map<String, String> authorColor;
    public ColorGenerator() {
        usedColor = new HashSet<>();
        authorColor = new HashMap<>();
    }
    private String generateColor() {
        String color = getColor();
        while (usedColor.contains(color)) {
            color = getColor();
        }
        usedColor.add(color);
        return color;
    }
    private String getColor() {
        String red;
        String green;
        String blue;

        Random random = new Random();
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //patch zero
        red = red.length() == 1 ? "0" + red : red;
        green = green.length() == 1 ? "0" + green : green;
        blue = blue.length() == 1 ? "0" + blue : blue;
        String color = "#" + red + green + blue;
        return color;
    }
    public void assignColor(Directory node) {
        List<Author> authors = node.getAuthors();
        for (Author author : authors) {
            if (authorColor.containsKey(author.getName())){
                author.setColor(authorColor.get(author.getName()));
            }
            else {
                String color = generateColor();
                authorColor.put(author.getName(), color);
                author.setColor(color);
            }
        }
    }
}
