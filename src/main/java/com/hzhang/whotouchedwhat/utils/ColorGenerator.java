package com.hzhang.whotouchedwhat.utils;

import com.hzhang.whotouchedwhat.model.Author;
import com.hzhang.whotouchedwhat.model.Directory;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ColorGenerator {
    private Set<String> usedColor;
    private String[] pallet = new String[]{"#1AFD9C", "#00CACA", "#005AB5", "#FF5151",
            "#B15BFF", "#8E8E8E", "#73BF00", "#FF5809", "#984B4B", "#949449", "#5151A2"};
    private int count = 0;
    public ColorGenerator() {
        usedColor = new HashSet<>();
    }
    private String generateColor() {
        String color;
        if (count < pallet.length) {
            color = pallet[count];
            count++;
        }
        else {
            color = getColor();
            while (usedColor.contains(color)) {
                color = getColor();
            }
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
            String color = generateColor();
            author.setColor(color);
        }
    }
}
