import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
    // You can create data to be used in the your
    // test cases like this. For each run of a test method,
    // a new PieceTest object is created and setUp() is called
    // automatically by JUnit.
    // For example, the code below sets up some
    // pyramid and s pieces in instance variables
    // that can be used in tests.
    private Piece pyr1, pyr2, pyr3, pyr4;
    private Piece s, sRotated;
    private Piece l1, l2, l1Rotated, l2Rotated;
    private Piece stick;
    private Piece[] pieces;

    protected void setUp() throws Exception {
        super.setUp();
        pieces = Piece.getPieces();

        pyr1 = new Piece(Piece.PYRAMID_STR);
        pyr2 = pyr1.computeNextRotation();
        pyr3 = pyr2.computeNextRotation();
        pyr4 = pyr3.computeNextRotation();

        s = new Piece(Piece.S1_STR);
        sRotated = s.computeNextRotation();

        stick = new Piece(Piece.STICK_STR);

        l1 = new Piece(Piece.L1_STR);
        l2 = new Piece(Piece.L2_STR);
        l1Rotated = new Piece("0 0  1 0  2 0  2 1");
        l2Rotated = new Piece("0 1  1 1  2 0  2 1");
    }

    private String shufflePieceString(String pieceString) {
        List<TPoint> points = new ArrayList<TPoint>();
        StringTokenizer tok = new StringTokenizer(pieceString);
        try {
            while (tok.hasMoreTokens()) {
                int x = Integer.parseInt(tok.nextToken());
                int y = Integer.parseInt(tok.nextToken());

                points.add(new TPoint(x, y));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid String:" + pieceString);
        }
        Collections.shuffle(points);
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < points.size(); i++) {
            TPoint tp = points.get(i);
            builder.append(tp.x + " " + tp.y);
            if (i != points.size() - 1) builder.append("  ");
        }
        return builder.toString();
    }
    // Here are some sample tests to get you started

    public void testSampleSize() {
        // Check size of pyr piece
        assertEquals(3, pyr1.getWidth());
        assertEquals(2, pyr1.getHeight());

        // Now try after rotation
        // Effectively we're testing size and rotation code here
        assertEquals(2, pyr2.getWidth());
        assertEquals(3, pyr2.getHeight());
        // Now try with some other piece, made a different way
        Piece l = new Piece(Piece.STICK_STR);
        assertEquals(1, l.getWidth());
        assertEquals(4, l.getHeight());

        assertEquals(2, l1.getWidth());
        assertEquals(3, l1.getHeight());

        assertEquals(2, l2.getWidth());
        assertEquals(3, l2.getHeight());
    }


    // Test the skirt returned by a few pieces
    public void testSampleSkirt() {
        // Note must use assertTrue(Arrays.equals(... as plain .equals does not work
        // right for arrays.
        assertTrue(Arrays.equals(new int[]{0, 0, 0}, pyr1.getSkirt()));
        assertTrue(Arrays.equals(new int[]{1, 0, 1}, pyr3.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0}, l1.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0}, l2.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0, 0}, l1Rotated.getSkirt()));
        assertTrue(Arrays.equals(new int[]{1, 1, 0}, l2Rotated.getSkirt()));
        assertTrue(Arrays.equals(new int[]{0, 0, 1}, s.getSkirt()));
        assertTrue(Arrays.equals(new int[]{1, 0}, sRotated.getSkirt()));
    }

    public void testEquals() {

        for (int i = 0; i < 20; i++) {
            assertEquals(pyr1, (new Piece(shufflePieceString(Piece.PYRAMID_STR))));
            assertEquals(s, (new Piece(shufflePieceString(Piece.S1_STR))));
        }
        assertEquals(pyr1, pyr4.computeNextRotation());
    }

    public void testRotation() {
        // Test L type figures
        Piece rotated = l1.computeNextRotation();
        Piece rotated2 = l2.computeNextRotation();
        assertEquals(l1Rotated, rotated);
        assertEquals(l2Rotated, rotated2);

        // Rotate L 4 times
        rotated = rotated.computeNextRotation().computeNextRotation().computeNextRotation();
        rotated2 = rotated2.computeNextRotation().computeNextRotation().computeNextRotation();
        assertEquals(l1, rotated);
        assertEquals(l2, rotated2);

        // Test Stick
        Piece stick_rotated_twice = stick.computeNextRotation().computeNextRotation();
        assertEquals(stick, stick_rotated_twice);
        Piece expected = stick;
        Piece actual = stick_rotated_twice.computeNextRotation().computeNextRotation();
        assertEquals(expected, actual);
    }

    public void testFastRotation() {
        // Test Square
        assertEquals(pieces[Piece.SQUARE], pieces[Piece.SQUARE].fastRotation());
        assertTrue(pieces[Piece.STICK].fastRotation().fastRotation().fastRotation().fastRotation().equals(pieces[Piece.STICK]));
        // Test L type figures
        assertEquals(l1Rotated, pieces[Piece.L1].fastRotation());
        assertEquals(l2Rotated, pieces[Piece.L2].fastRotation());

        // Test Pyramid
        Piece rotated_once = pieces[Piece.PYRAMID].fastRotation();
        Piece rotated_twice = rotated_once.fastRotation();
        Piece rotated_thrice = rotated_twice.fastRotation();
        Piece pyramid = rotated_thrice.fastRotation();
        assertEquals(pyr2, rotated_once);
        assertEquals(pyr3, rotated_twice);
        assertEquals(pyr4, rotated_thrice);
        assertEquals(pyr1, pyramid);
    }

}
