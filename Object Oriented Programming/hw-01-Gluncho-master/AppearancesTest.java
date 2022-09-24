import junit.framework.TestCase;

import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}

	private <T> Set<T> asSet(T... elems){
		Set<T> ans = new HashSet<>();
		for(T elem : elems){
			ans.add(elem);
		}
		return ans;
	}

	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	// Test empty list
	public void testSameCount3(){
		List<String> a = new ArrayList<>();
		assertEquals(0, Appearances.sameCount(a, Arrays.asList("2")));
		assertEquals(0, Appearances.sameCount(a,a));
	}

	public void testSameCount4(){
		Set<String> a = asSet("a", "b", "c", "d");
		Set<String> b = asSet("b", "c", "d", "e");
		assertEquals(3, Appearances.sameCount(a,b));
	}
}
