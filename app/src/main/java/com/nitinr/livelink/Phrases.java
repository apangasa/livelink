package com.nitinr.livelink;

import java.util.Arrays;
import java.util.HashSet;

public class Phrases {
    public static HashSet<String> phraseSet;

    static {
        String[] ALL_PHRASES = new String[] {
                "on",
                "off",
                "detect",
                "link"
        };

        phraseSet = new HashSet<>(Arrays.asList(ALL_PHRASES));
    }
}
