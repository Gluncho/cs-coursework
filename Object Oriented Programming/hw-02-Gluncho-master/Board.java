// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int maxHeight;
	private int[] widths;
	private int[] heights;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	private int prevMaxHeight;
	private int[] prevWidths;
	private int[] prevHeights;
	private boolean[][] prevGrid;

	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		committed = true;
		this.maxHeight = 0;
		grid = new boolean[width][height];
		widths = new int[height];
		heights = new int[width];
		this.prevMaxHeight = 0;
		prevGrid = new boolean[width][height];
		prevWidths = new int[height];
		prevHeights = new int[width];
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int heightCheck = 0;
			int[] widthsChecker = new int[widths.length];
			for(int i = 0; i < width; i++){
				int columnHeightChecker = 0;
				for(int j = 0; j < height; j++){
					if(!grid[i][j]) continue;
					heightCheck = Math.max(heightCheck, j + 1);
					columnHeightChecker = Math.max(columnHeightChecker, j + 1);
					widthsChecker[j]++;
				}
				if(columnHeightChecker != heights[i]) {
					throw new RuntimeException("WRONG COLUMN HEIGHT ON COLUMN +"+i+": \n EXPECTED "+columnHeightChecker+"\n ACTUAL "+heights[i]);
				}
			}
			if(heightCheck != maxHeight) {
				throw new RuntimeException("WRONG MAX HEIGHT: \n EXPECTED "+heightCheck+"\n ACTUAL "+maxHeight);
			}
			if(!Arrays.equals(widthsChecker, widths)){
				throw new RuntimeException("WRONG ROW WIDTHS: \n EXPECTED: + "+widthsChecker.toString()+"\n ACTUAL: "+widths.toString());
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int dropHeight = 0;
		int[] skirt = piece.getSkirt();
		for(int i = 0; i < piece.getWidth(); i++){
			dropHeight = Math.max(dropHeight, heights[x + i] - skirt[i]);
		}
		return dropHeight;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		return x < 0 || y < 0 || x >= width || y >= height || grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		backupGrid();
		int result = PLACE_OK;
		for(TPoint point : piece.getBody()){
			int current_x = x + point.x;
			int current_y = y + point.y;
			if(current_x < 0 || current_x >= this.width
					|| current_y < 0 || current_y >= this.height) {
				return PLACE_OUT_BOUNDS;
			}
			//if(piece.equals(new Piece(Piece.STICK_STR))) System.out.println(current_x + " " + current_y);
			if(grid[x + point.x][y + point.y]) return PLACE_BAD;
			grid[current_x][current_y] = true;
			widths[current_y]++;
			heights[current_x] = Math.max(heights[current_x], current_y + 1);
			maxHeight = Math.max(maxHeight, current_y + 1);
			if(widths[current_y] == this.width) result = PLACE_ROW_FILLED;
		}
		//System.out.println(toString());
		sanityCheck();
		return result;
	}
	private void backupGrid(){
		for(int i = 0; i < width; i++){
			System.arraycopy(grid[i], 0, prevGrid[i],0, height);
		}
		System.arraycopy(widths, 0, prevWidths, 0, widths.length);
		System.arraycopy(heights, 0, prevHeights, 0, heights.length);
		prevMaxHeight = maxHeight;
	}

	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		if(width == 0) return rowsCleared;
		if(committed) backupGrid();
		committed = false;
		for(int to = 0; to < maxHeight ; to++){
			if(widths[to] != width) continue;
			int from = to + 1;
			while(from < maxHeight && widths[from] == width){
				from++;
			}
			int row;
			for(row = from; row < maxHeight; row++){
				copyRow(to + (row - from), row);
			}
			rowsCleared += (from - to);
			for(int empty = to + (row - from); empty < maxHeight; empty++){
				widths[empty] = 0;
				for(int i = 0; i < width; i++) grid[i][empty] = false;
			}
		}
		int tmp = maxHeight;
		maxHeight = 0;
		for(int i = 0; i < width; i++){
			heights[i] = 0;
			for(int j = tmp - 1; j >=0; j--){
				if(grid[i][j]) {
					maxHeight = Math.max(maxHeight, j + 1);
					heights[i] = Math.max(heights[i], j + 1);
					break;
				}
			}
		}
		sanityCheck();
		return rowsCleared;
	}

	private void copyRow(int to, int from){
		for(int i = 0; i < width; i++){
			grid[i][to] = grid[i][from];
		}
		widths[to] = widths[from];
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed == false){
			int[] temp = widths;
			widths = prevWidths;
			prevWidths = temp;

			temp = heights;
			heights = prevHeights;
			prevHeights = temp;

			boolean[][] tmp = grid;
			grid = prevGrid;
			prevGrid = tmp;

			int tempHeight = maxHeight;
			maxHeight = prevMaxHeight;
			prevMaxHeight = maxHeight;
		}
		committed = true;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		buff.append("-".repeat(Math.max(0, width + 2)));
		return(buff.toString());
	}
}


