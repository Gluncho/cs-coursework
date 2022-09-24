// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int min_x = Integer.MAX_VALUE, min_y =Integer.MAX_VALUE;
		int max_x = Integer.MIN_VALUE, max_y = Integer.MIN_VALUE;
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[0].length; j++){
				if(grid[i][j] != ch) continue;
				min_x = Math.min(min_x, i);
				min_y = Math.min(min_y, j);
				max_x = Math.max(max_x, i);
				max_y = Math.max(max_y, j);
			}
		}
		if(min_x == Integer.MAX_VALUE) //no occurrences of char are found
			return 0;
		return (max_x - min_x + 1) * (max_y - min_y + 1);
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int ans = 0;
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[0].length; j++){
				int count_upper = countEqualIfExtended(i, j, -1, 0);
				int count_lower = countEqualIfExtended(i, j, 1, 0);
				int count_left = countEqualIfExtended(i, j, 0, -1);
				int count_right = countEqualIfExtended(i, j, 0, 1);
				if(count_upper == count_lower && count_lower == count_left
					&& count_left == count_right && count_right > 1) ans++;
			}
		}
		return ans;
	}

	/**
	 * Returns number of chars equal to grid[i][j]
	 *
	 */
	private int countEqualIfExtended(int i, int j, int x_incr, int y_incr){
		int counter = 0;
		for(int a = i, b = j; a >= 0 && b >= 0 && a < grid.length && b < grid[0].length; a+=x_incr, b+=y_incr){
			if(grid[i][j] != grid[a][b]) break;
			counter++;
		}
		return counter;
	}
}
