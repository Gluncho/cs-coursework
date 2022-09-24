import junit.framework.TestCase;
import java.util.*;

public class TetrisGridTest extends TestCase {
	
	// Provided simple clearRows() test
	// width 2, height 3 grid
	public void testClear1() {
		boolean[][] before =
		{	
			{true, true, false, },
			{false, true, true, }
		};
		
		boolean[][] after =
		{	
			{true, false, false},
			{false, true, false}
		};
		
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	// should remain the same
	public void testClear2(){
		boolean[][] grid =
		{
				{true, true, true, },
				{true, true, false, },
				{false, false, true}
		};

		TetrisGrid tetris = new TetrisGrid(grid);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(grid, tetris.getGrid()) );
	}

	// last row full
	public void testClear3() {
		boolean[][] before =
		{
				{true, true, true, },
				{false, true, true, },
				{true, false, true}
		};

		boolean[][] after =
		{
				{true, true, false},
				{false, true, false},
				{true, false, false}
		};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	// all full
	public void testClear4() {
		boolean[][] before =
		{
				{true, true, true, true},
				{true, true, true, true},
				{true, true, true, true},
				{true, true, true, true}
		};

		boolean[][] after =
		{
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, false}
		};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	public void testClear5(){
		boolean[][] before =
		{
				{true, true, true, true, true, true, false},
				{false, true, true, true, true, false, true},
				{true, true, true, true, true, false, true},
				{true, true, false, true, true, true, false}
		};

		boolean[][] after =
		{
				{true, true, true, false, false, false, false},
				{false, true, false, true, false, false, false},
				{true, true, false, true, false, false, false},
				{true, false, true, false, false, false, false}
		};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	// Empty grid
	public void testClear6(){
		boolean[][] before =
				{
				};

		boolean[][] after =
				{
				};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	// 1x1 grid
	public void testClear7(){
		boolean[][] before =
				{
						{true}
				};

		boolean[][] after =
				{
						{false}
				};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}
}