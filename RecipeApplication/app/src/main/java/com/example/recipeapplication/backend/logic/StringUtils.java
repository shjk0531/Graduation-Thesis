package com.example.recipeapplication.backend.logic;

import android.util.Log;

public class StringUtils {
    public static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }

    public static boolean hasJongseongN(String str, String ingredientName) {
        int startIndex = str.indexOf(ingredientName);
        if (startIndex >= 1 && startIndex < str.length()) {
            char beforeChar = str.charAt(startIndex - 1);
            if (beforeChar >= 0xAC00) {
                char jong = (char)((beforeChar - 0xAC00) % 28);
                return (int)jong == 4;
            } else if (beforeChar == ' ') {
                int spaceIndex = startIndex - 2;
                while (spaceIndex >= 0 && str.charAt(spaceIndex) == ' ') {
                    spaceIndex--;
                }
                if (spaceIndex >= 0) {
                    beforeChar = str.charAt(spaceIndex);
                    if (beforeChar >= 0xAC00) {
                        char jong = (char)((beforeChar - 0xAC00) % 28);
                        return (int)jong == 4;
                    }
                }
            }
        }
        return false;
    }

}

