import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		Map<T, Integer> a_freq = new HashMap<>();
		Map<T, Integer> b_freq = new HashMap<>();
		fillMap(a_freq, a);
		fillMap(b_freq, b);
		int same_count = 0;
		for(T a_elem : a_freq.keySet()){
			if(!b_freq.containsKey(a_elem)) continue;
			same_count += a_freq.get(a_elem) == b_freq.get(a_elem) ? 1 : 0;
		}
		return same_count;
	}
	private static <T> void fillMap(Map<T, Integer> freq, Collection<T> list){
		for(T elem : list){
			if(freq.containsKey(elem)) freq.put(elem, freq.get(elem) + 1);
			else freq.put(elem, 1);
		}
	}
}
