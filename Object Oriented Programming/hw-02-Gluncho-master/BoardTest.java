import junit.framework.TestCase;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest extends TestCase {
	Board b,board;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, s2, s2Rotated;
	Piece stick;
	Piece l1,l2;
	Piece square;
	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.

	protected void setUp() throws Exception {
		b = new Board(3, 6);
		board = new Board(4, 9);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		s2 = new Piece(Piece.S2_STR);
		/*
			110
			011
		 */
		/*
			01
			11
			10
		 */
		sRotated = s.computeNextRotation();
		s2Rotated = s2.computeNextRotation();

		square = new Piece(Piece.SQUARE_STR);
		stick = new Piece(Piece.STICK_STR);

		l1 = new Piece(Piece.L1_STR);
		l2 = new Piece(Piece.L2_STR);

		b.place(pyr1, 0, 0);
	}

	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	public void testPlace1(){
		b.commit();
		assertEquals(Board.PLACE_OK, b.place(stick, 0, 1));
		b.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(pyr1,1,4));
		// assertTrue(b.getGrid(1,4)); Bad practice, cause it depends on the implementation
	}

	public void testPlace2(){
		b.commit();
		assertEquals(Board.PLACE_OK, b.place(stick, 2 ,1)); b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(stick,0,1)); b.commit();
	}

	// Test exception catching
	public void testPlace3(){
		assertThrows(RuntimeException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				b.place(stick,1,0);
			}
		});
	}
	public void testClear1(){
		b.commit();
		int result = b.place(stick, 2, 0);
		assertEquals(Board.PLACE_BAD, result);
		result = b.clearRows();
		assertEquals(1, result);
	}

	public void testClear2(){
		b.commit();
		int result = b.place(stick, 2, 1);
		b.commit();
		assertEquals(Board.PLACE_OK, result);
		result = b.place(stick, 0, 1);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.commit();
		result = b.clearRows();
		b.commit();
		assertEquals(2, result);
		result = b.place(pyr1, 0, 3);
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, result);
		result = b.clearRows();
		assertEquals(1, result);
	}

	public void testClear3(){
		Board board = new Board(3,9);
		board.place(stick,0,0); board.commit();
		board.place(stick,2,0); board.commit();
		board.place(stick, 0,4); board.commit();
		board.place(stick, 2, 4); board.commit();
		int result = board.place(pyr3, 0, 7);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		result = board.clearRows();
		assertEquals(2, result);
		assertFalse(board.getGrid(0,7));
	}

	public void testClearRows1() {
		int rowsCleared = b.clearRows();
		assertEquals(1, rowsCleared);
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(1, b.getMaxHeight());
		assertFalse(b.getGrid(0, 0));
		assertTrue(b.getGrid(1, 0));b.commit();
		int placeResult = b.place(stick, 0, 0);
		b.commit();
		assertEquals(Board.PLACE_OK, placeResult);

		Piece square = new Piece(Piece.SQUARE_STR);
		placeResult = b.place(square, 1, 1);
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, placeResult);
		assertEquals(4, b.getColumnHeight(0));

		//System.out.println(b.toString());
		rowsCleared = b.clearRows();
		//System.out.println(b.toString());
		assertEquals(2, rowsCleared);
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(2, b.getMaxHeight());
	}

	public void testClearRows2() {
		Board board = new Board(3, 4);
		int result = board.place(stick, 0, 0); board.commit();
		assertEquals(Board.PLACE_OK, result);
		result = board.place(stick, 1, 0); board.commit();
		assertEquals(Board.PLACE_OK, result);
		result = board.place(stick, 2, 0); board.commit();
		assertEquals(Board.PLACE_ROW_FILLED, result);

		int rowsCleared = board.clearRows();
		assertEquals(4, rowsCleared);
		for (int i = 0; i < board.getHeight(); i++) {
			assertEquals(0, board.getRowWidth(i));
		}
		for (int i = 0; i < board.getWidth(); i++) {
			assertEquals(0, board.getColumnHeight(i));
		}
	}
	public void testDropHeight(){
		Board board = new Board(4,8);
		assertEquals(0, board.dropHeight(stick.computeNextRotation(), 0));
		board.place(stick.computeNextRotation(), 0, 0);board.commit();
		board.place(pyr1, 1, 1);b.commit();
		assertEquals(2, board.dropHeight(s2Rotated, 1));
		assertEquals(1, board.dropHeight(s2Rotated, 0));
		assertEquals(3, board.dropHeight(square, 2));
	}

	public void testUndo1(){
		b.undo();
		assertFalse(b.getGrid(0,0));
		assertFalse(b.getGrid(1,1));
		assertFalse(b.getGrid(0,2));
	}

	public void testUndo2(){
		Board board = new Board(4,8);
		board.place(stick,0,0);board.commit();
		board.place(stick,1,0);board.commit();
		board.place(stick,2,0); board.commit();
		board.place(stick,3,0);
		assertEquals(4, board.clearRows());
		board.undo();
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 8; j++){
				boolean expected = i < 3 && j < 4;
				//System.out.println(i+" "+j);
				assertEquals(expected, board.getGrid(i,j));
			}
		}
		assertEquals(true, board.getGrid(1,-1));
		assertEquals(true, board.getGrid(board.getWidth(), 0));
	}

	public void testClearUndo(){
		Board board = new Board(4,4);
		board.place(stick.computeNextRotation(), 0,2);
		board.commit();
		board.clearRows();
		board.undo();
		assertEquals(3, board.getMaxHeight());
		board.clearRows();
		assertEquals(0, board.getMaxHeight());
		for(int i = 0; i < 4; i++) assertEquals(0, board.getColumnHeight(0));
	}

	public void testToString1(){
		b.commit(); b.place(square,0,2);
		String expected = "|   |\n|   |\n|++ |\n|++ |\n| + |\n|+++|\n-----";
		assertEquals(expected, b.toString());
	}

	public void testToString2(){
		Board board = new Board(4,4);
		board.place(stick,0,0);board.commit();
		board.place(stick,1,0);board.commit();
		board.place(stick, 2, 0); board.commit();
		board.place(stick, 3, 0);board.commit();
		String row = "|++++|\n";
		String expected = row + row + row + row + "------";
		assertEquals(expected, board.toString());
	}

	public void testSanityCheck(){
		b.commit();
		assertDoesNotThrow(new Executable() {
			@Override
			public void execute() throws Throwable {
				b.clearRows();
				b.commit();
				b.place(stick,1,1);
			}
		});
	}

}
