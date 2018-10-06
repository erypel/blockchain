package StringManipulation;

import java.util.HashMap;
import java.util.Map;

public class Sherlock {
	// Complete the isValid function below.
	static String isValid(String s) {
		if (s == null || s.length() <= 2)
			return "YES";
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		HashMap<Character, Integer> charCount = new HashMap<Character, Integer>();
		/*
		 * go through string and count each character's number of instances
		 */
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (charCount.containsKey(c)) {
				charCount.replace(c, charCount.get(c), charCount.get(c) + 1);
				if (charCount.get(c) > max)
					max = charCount.get(c);
			} else {
				charCount.put(c, 1);
			}
		}

		// HashMap<Integer, Integer> countCount = new HashMap<Integer, Integer>();
		int count1 = 0, count2 = 0;
		for (Map.Entry m : charCount.entrySet())
			min = Math.min(min, (int) m.getValue());

		for (Map.Entry m : charCount.entrySet()) {
			int val = (int) m.getValue();
			if (val == max)
				count1++;
			else if (val == min)
				count2++;
		}
		if (max - min == 0 || (count1 == charCount.size() - 1 && min == 1)
				|| (count2 == charCount.size() - 1 && max - min == 1))
			return "YES";

		return "NO";
	}
}
