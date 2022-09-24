
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	public void testCharArea3(){
		char[][] grid = new char[][]{
				{'a','a','a'},
				{'b','a','a'},
				{'a','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('c'));
		assertEquals(1, cg.charArea('b'));
		assertEquals(9, cg.charArea('a'));
	}

	public void testCharArea4(){
		//parallelogram shape
		char[][] grid = new char[][]{
				{' ','a','b','b','b'},
				{'a','o','i','b','c'},
				{'c','i','b','a','b'},
				{' ',' ', 'a','b','i'},
				{'b','c','k','i','e'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(16, cg.charArea('a'));
		assertEquals(16, cg.charArea('i'));
		assertEquals(8, cg.charArea(' '));
		assertEquals(25, cg.charArea('b'));
		assertEquals(20, cg.charArea('c'));
	}

	// Empty grid test
	public void testCharArea5(){
		char[][] grid = new char[][]{};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('2'));
	}

	public void testCountPlus1(){
		char[][] grid = new char[][]{
				{'b','a','b'},
				{'a','a','a'},
				{'b','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus2(){
		char[][] grid = new char[][]{
				{'a','a','b','a','a','a','a'},
				{'a','a','b','a','a','a','a'},
				{'b','b','b','b','b','a','a'},
				{'a','b','b','a','a','a','a'},
				{'a','b','b','a','a','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	/** aapaaaaaa
	 *  aapaaaaxa
	 *  pppppaxxx
	 *  aapaayaxa
	 *  aapayyyaa
	 *  zzzzzyzzz
	 *  aaxxayaaa
	 */
	public void testCountPlus3(){
		char[][] grid = new char[][]{
				{'a','a','p','a','a','a','a','a','a'},
				{'a','a','p','a','a','a','a','x','a'},
				{'p','p','p','p','p','a','x','x','x'},
				{'a','a','p','a','a','y','a','x','a'},
				{'a','a','p','a','y','y','y','a','a'},
				{'z','z','z','z','z','y','z','z','z'},
				{'a','a','x','x','a','y','a','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(2, cg.countPlus());
	}

	/**
	 * yyyxyyyy
	 * xaxxxaxx
	 * aaaxaaax
	 * xaxaxaxa
	 */
	public void testCountPlus4(){
		char[][] grid = new char[][]{
				{'y','y','y','x','y','y','y','y'},
				{'x','a','x','x','x','a','x','x'},
				{'a','a','a','x','a','a','a','x'},
				{'x','a','x','a','x','a','x','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(3, cg.countPlus());
	}

	public void testCountPlus5(){
		// all equal, odd length, rectangle 5x7
		char[][] grid = new char[][]{
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}

	public void testCountPlus6(){
		//all equal, odd length, square 7x7
		char[][] grid = new char[][]{
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'},
				{'a','a','a','a','a','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus7(){
		char[][] grid = new char[][]{};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}
}
