//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	private boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		if(grid.length == 0) return;
		for(int i = grid[0].length - 1; i >= 0; i--){
			if(isFull(i)) shiftColumns(i);
		}
	}

	private boolean isFull(int col){
		for(int i = 0; i < grid.length; i++){
			if(!grid[i][col]) return false;
		}
		return true;
	}

	private void shiftColumns(int col){
		while(col + 1 <= grid[0].length - 1){
			copyColumn(col + 1, col);
			col++;
		}
		addEmptyColumn();
	}

	private void copyColumn(int col_from, int col_to){
		for(int i = 0; i < grid.length; i++){
			grid[i][col_to] = grid[i][col_from];
		}
	}

	private void addEmptyColumn(){
		for(int i = 0; i < grid.length; i++){
			grid[i][grid[0].length - 1] = false;
		}
	}

	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid;
	}
}
