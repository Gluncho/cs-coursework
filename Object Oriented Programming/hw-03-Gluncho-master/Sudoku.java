import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");

	public static final int[][] hardGrid2 = Sudoku.stringsToGrid(
			"3 0 0 0 0 0 0 8 0",
			"0 0 1 0 9 3 0 0 0",
			"0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2",
			"0 0 0 0 4 0 0 0 0",
			"5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0",
			"0 0 0 5 3 0 9 0 0",
			"0 3 0 0 0 0 0 5 1");
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	
	private List<Spot> spots;
	private int[][] grid;
	private String solutionText;
	private long elapsed;

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = ints;
		solutionText = null;
		spots = new ArrayList<>();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(grid[i][j] == 0){
					Spot spot = new Spot(i,j);
					spot.set(grid[i][j]);
					spots.add(spot);
				}
			}
		}
		Collections.sort(spots);

	}

	public Sudoku(String text){
		this(textToGrid(text));
	}

	public class Spot implements Comparable{
		private final int row;
		private final int col;

		/**
		 * Constructs a new object.
		 */
		public Spot(int row, int col) {
			super();
			this.row = row;
			this.col = col;
		}

		public void set(int value){
			grid[row][col] = value;
		}

		/** Returns the list of all numbers from 1 to 9 which are valid for the current spot
		 *
		 */
		public List<Integer> getAssignableNumbers(){
			boolean[] is_valid = new boolean[SIZE + 1];
			Arrays.fill(is_valid,true);
			for(int i = 0; i < SIZE; i++){
				if(grid[row][i] != 0) is_valid[grid[row][i]] = false;
				if(grid[i][col] != 0) is_valid[grid[i][col]] = false;
			}
			int upperLeftX = row - row % PART;
			int upperLeftY = col - col % PART;
			for(int i = upperLeftX; i < upperLeftX + PART; i++){
				for(int j = upperLeftY; j < upperLeftY + PART; j++){
					if(grid[i][j] != 0) is_valid[grid[i][j]] = false;
				}
			}
			List<Integer> available = new ArrayList<>();
			for(int i = 1; i <= SIZE; i++){
				if(is_valid[i]) available.add(i);
			}
			return available;
		}

		/**
		 * Compares this object with the specified object for order.  Returns a
		 * negative integer, zero, or a positive integer as this object is less
		 * than, equal to, or greater than the specified object.
		 */
		@Override
		public int compareTo(Object o) {
			if(o == this) return 0;

			if(!(o instanceof Spot other)) throw new RuntimeException("Cannot compare Spot to "+o.getClass());
			return this.getAssignableNumbers().size() - other.getAssignableNumbers().size();
		}
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long start = System.currentTimeMillis();
		int numSolutions = solveRec(0);
		elapsed = System.currentTimeMillis() - start;
		return numSolutions;
	}

	
	public String getSolutionText() {
		return solutionText;
	}
	
	public long getElapsed() {
		return elapsed;
	}

	private int solveRec(int cur){
		if(cur == spots.size()){
			// trick to check whether solution is already found
			if(solutionText == null) solutionText = this.toString();
			return 1;
		}
		int answer = 0;
		Spot spot = spots.get(cur);
		for(int validNum : spot.getAssignableNumbers()){
			spot.set(validNum);
			answer += solveRec(cur + 1);
			spot.set(0);
			if(answer >= MAX_SOLUTIONS) return MAX_SOLUTIONS;
		}
		return answer;
	}

	/**
	 * Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				builder.append(grid[i][j]);
				if(j != SIZE - 1) builder.append(" ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
