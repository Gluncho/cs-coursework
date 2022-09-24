import junit.framework.TestCase;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

public class SudokuTest extends TestCase {

    // utility to check the validity of sudoku grid
    private boolean isValidSudoku(int[][] grid) {
        // validate rows and columns
        for (int i = 0; i < Sudoku.SIZE; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();
            for (int j = 0; j < Sudoku.SIZE; j++) {
                if (grid[i][j] == 0 || grid[j][i] == 0) return false;
                if (rowSet.contains(grid[i][j]) || colSet.contains(grid[j][i])) return false;
                rowSet.add(grid[i][j]);
                colSet.add(grid[j][i]);
            }
        }
        // validate parts
        for (int i = 0; i < Sudoku.SIZE; i += Sudoku.PART) {
            for (int j = 0; j < Sudoku.SIZE; j += Sudoku.PART) {
                if (!isValidPart(i, j, grid)) return false;
            }
        }
        return true;
    }

    private boolean isValidPart(int i, int j, int[][] grid) {
        Set<Integer> partSet = new HashSet<>();
        for (int row = i; row < i + 3; row++) {
            for (int col = j; col < j + 3; col++) {
                if (partSet.contains(grid[row][col])) return false;
                partSet.add(grid[row][col]);
            }
        }
        return true;
    }

    public void testSpot() {
        Sudoku s = new Sudoku(Sudoku.easyGrid);
        Sudoku.Spot spot = s.new Spot(0, 3);
        List<Integer> availableNums = spot.getAssignableNumbers();
        assertEquals(1, availableNums.size());
        assertEquals(7, (int) availableNums.get(0));
        spot.set(7);
        // now there's no number we can assign to this spot
        availableNums = spot.getAssignableNumbers();
        assertEquals(0, availableNums.size());
    }

    public void testTextToGrid() {
        final String text = """
                530070000
                 600195000
                 098000060
                 800060003
                 400803001
                 700020006
                 060000280
                 000419005
                 000080079""";
        assertDoesNotThrow(() -> {
            int[][] grid = Sudoku.textToGrid(text);
            Sudoku sudoku = new Sudoku(text);
        });
        // ruin text
        final String ruinedText = """
                -130070000
                a5600195000
                 098000060
                 800060003
                 400803001
                 700020006
                 060000280
                 000419005
                 000080079""";
        assertThrows(RuntimeException.class, () -> {
            int[][] grid = Sudoku.textToGrid(ruinedText);
        });
    }

    public void testMain(){
        assertDoesNotThrow(() -> Sudoku.main(new String[2]));
    }

    public void testEasyGrid() {
        Sudoku s = new Sudoku(Sudoku.easyGrid);
        s.solve();
        int[][] solvedGrid = Sudoku.textToGrid(s.getSolutionText());
        assertEquals(solvedGrid.length, Sudoku.SIZE);
        assertTrue(isValidSudoku(solvedGrid));
    }

    public void testMediumGrid() {
        Sudoku s = new Sudoku(Sudoku.mediumGrid);
        s.solve();
        int[][] solvedGrid = Sudoku.textToGrid(s.getSolutionText());
        assertEquals(solvedGrid.length, Sudoku.SIZE);
        assertTrue(isValidSudoku(solvedGrid));
    }

    public void testHardGrid() {
        Sudoku s = new Sudoku(Sudoku.hardGrid);
        s.solve();
        int[][] solvedGrid = Sudoku.textToGrid(s.getSolutionText());
        assertEquals(solvedGrid.length, Sudoku.SIZE);
        assertTrue(isValidSudoku(solvedGrid));
    }

    public void testHardGrid2() {
        Sudoku s = new Sudoku(Sudoku.hardGrid2);
        assertEquals(6, s.solve());
        int[][] solvedGrid = Sudoku.textToGrid(s.getSolutionText());
        assertEquals(solvedGrid.length, Sudoku.SIZE);
        assertTrue(isValidSudoku(solvedGrid));
    }

    public void testToStringStaysSame(){
        Sudoku s = new Sudoku(Sudoku.hardGrid);
        String str = s.toString();
        s.solve();
        assertEquals(str, s.toString());
    }

}
