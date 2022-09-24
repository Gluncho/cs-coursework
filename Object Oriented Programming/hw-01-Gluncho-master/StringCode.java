// CS108 HW1 -- String static methods

import java.util.*;

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if(str.equals("")) return 0;
		int currAmount = 1, ans = 1;
		char curr = str.charAt(0);
		for(int i = 1; i < str.length(); i++) {
			if (str.charAt(i) == str.charAt(i - 1)) {
				currAmount++;
				if (currAmount > ans) ans = currAmount;
			} else {
				curr = str.charAt(i);
				currAmount = 1;
			}
		}
		return ans;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String ans = "";
		for(int i = 0; i < str.length(); i++){
			char current = str.charAt(i);
			if(!Character.isDigit(current)){
				ans += current;
				continue;
			}
			if(i == str.length() - 1) continue;
			int amount = current - '0';
			for(int j = 0; j < amount; j++) ans += str.charAt(i + 1);
		}
		return ans; // YOUR CODE HERE
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		Set<String> set = new HashSet<>();
		for(int i = 0; i <= a.length() - len; i++){
			set.add(a.substring(i,i + len));
		}
		for(int i = 0; i <= b.length() - len; i++){
			if(set.contains(b.substring(i, i + len))) return true;
		}
		return false;
	}
}
