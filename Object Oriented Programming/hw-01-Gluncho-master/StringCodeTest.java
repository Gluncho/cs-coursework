// StringCodeTest
// Some test code is provided for the early HW1 problems,
// and much is left for you to add.

import junit.framework.TestCase;

public class StringCodeTest extends TestCase {
	//
	// blowup
	//
	public void testBlowup1() {
		// basic cases
		assertEquals("xxaaaabb", StringCode.blowup("xx3abb"));
		assertEquals("xxxZZZZ", StringCode.blowup("2x3Z"));
	}
	
	public void testBlowup2() {
		// things with digits
		
		// digit at end
		assertEquals("axxx", StringCode.blowup("a2x3"));
		
		// digits next to each other
		assertEquals("a33111", StringCode.blowup("a231"));
		
		// try a 0
		assertEquals("aabb", StringCode.blowup("aa0bb"));
	}
	
	public void testBlowup3() {
		// weird chars, empty string
		assertEquals("AB&&,- ab", StringCode.blowup("AB&&,- ab"));
		assertEquals("", StringCode.blowup(""));
		
		// string with only digits
		assertEquals("", StringCode.blowup("2"));
		assertEquals("33", StringCode.blowup("23"));
	}

	public void testBlowup4(){
		assertEquals("1", StringCode.blowup("11"));
		assertEquals("2334440000", StringCode.blowup("123405"));
	}
	//
	// maxRun
	//
	public void testRun1() {
		assertEquals(2, StringCode.maxRun("hoopla"));
		assertEquals(3, StringCode.maxRun("hoopllla"));
	}
	
	public void testRun2() {
		assertEquals(3, StringCode.maxRun("abbcccddbbbxx"));
		assertEquals(0, StringCode.maxRun(""));
		assertEquals(3, StringCode.maxRun("hhhooppoo"));
	}
	
	public void testRun3() {
		// "evolve" technique -- make a series of test cases
		// where each is change from the one above.
		assertEquals(1, StringCode.maxRun("123"));
		assertEquals(2, StringCode.maxRun("1223"));
		assertEquals(2, StringCode.maxRun("112233"));
		assertEquals(3, StringCode.maxRun("1112233"));
	}
	public void testRun4(){
		assertEquals(1, StringCode.maxRun("1"));
		assertEquals(2, StringCode.maxRun("11"));
		assertEquals(1, StringCode.maxRun("1212121212"));
		assertEquals(2, StringCode.maxRun("121212122"));
	}

	//
	// stringIntersect
	//
	public void testIntersect1(){
		assertEquals(true, StringCode.stringIntersect("aabdef", "bdexyz", 3));
		assertEquals(true, StringCode.stringIntersect("aaaa", "aa", 2));
		assertEquals(false, StringCode.stringIntersect("aabcd", "aabc", 5));
	}

	// edge cases
	public void testIntersect2(){
		assertEquals(true, StringCode.stringIntersect("", "abc", 0));
		assertEquals(true, StringCode.stringIntersect("", "", 0));
		assertEquals(false, StringCode.stringIntersect("","abc", 1));
		assertEquals(true, StringCode.stringIntersect("abc", "bc", 2));
		assertEquals(false, StringCode.stringIntersect("abc", "bc", 3));
		assertEquals(false, StringCode.stringIntersect("a", "b", 1));
	}
}
