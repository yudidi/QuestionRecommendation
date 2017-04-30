package com.sicnu.yudidi.utils.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DP {

	public static List<String> getLastMaxLCS(String[] s1, String[] s2) {
		String[][] dp = new String[s1.length][s2.length];
		// initial boundary.
		dp[0][0] = (s1[0] == s2[0]) ? s1[0] + "," : "";
		for (int i = 1; i < s1.length; i++) {
			if (s1[i] == s2[0]) {
				dp[i][0] = s2[0] + ",";
			} else {
				dp[i][0] = dp[i - 1][0];
			}
		}
		for (int i = 1; i < s2.length; i++) {
			if (s1[0] == s2[i]) {
				dp[0][i] = s1[0] + ",";
			} else {
				dp[0][i] = dp[0][i - 1];
			}
		}
		// fill dp
		for (int i = 1; i < s1.length; i++) {
			for (int j = 1; j < s2.length; j++) {
				if (s1[i] == s2[j]) {
					dp[i][j] = dp[i - 1][j - 1]+s1[i]+",";
				} else {
					dp[i][j] = (dp[i - 1][j].length() > dp[i][j - 1].length()) ? dp[i - 1][j] : dp[i][j - 1];
				}
			}
		}
		// get last LCS
		List<String> lastMaxLcs = new ArrayList<>(Arrays.asList(dp[s1.length-1][s2.length-1].split(",")));
		return  lastMaxLcs;
	}
}
