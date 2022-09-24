import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertNotEquals;

public class MetropolisEntryTest extends TestCase {
    Object[] entry1, entry2, entry3, entry4;
    MetropolisEntry entry;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        entry1 = new Object[]{"Mumbai", "Asia", 20400000};
        entry2 = new Object[]{"London", "Europe", 8580000};
        entry3 = new Object[]{"Hello", "From the other side", 14590};
        entry4 = new Object[]{"1", "2", 3};
    }

    public void test1(){
        entry = new MetropolisEntry(entry1);
        assertEquals(entry1[0], entry.get(0));
        assertEquals(entry1[1], entry.get(1));
        assertEquals(entry1[2], entry.get(2));
    }

    public void testDoesntThrow(){
        Assertions.assertDoesNotThrow(() -> {
            entry = new MetropolisEntry(entry2);
            String str = entry.toString();
            entry.get(1);
        });
    }

    public void testThrows(){
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            entry = new MetropolisEntry(entry3);
            entry.get(10);
        });
    }

    public void testToString(){
        entry = new MetropolisEntry(entry4);
        String ans = "(\"1\",\"2\",3)";
        assertEquals(ans, entry.toString());
        entry = new MetropolisEntry("A", "CDE", 4);
        String expected = "(\"A\",\"CDE\",4)";
        assertEquals(expected, entry.toString());
    }

    public void testEquals(){
        entry = new MetropolisEntry(entry1);
        MetropolisEntry entryCopy = new MetropolisEntry(entry1);
        MetropolisEntry entryNotEqual = new MetropolisEntry(entry2);
        assertEquals(entryCopy, entry);
        assertNotEquals(entryNotEqual, entry);
    }
}
