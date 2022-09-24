import junit.framework.TestCase;
import org.junit.jupiter.api.function.Executable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class MetropolisTableModelTest extends TestCase {
    // NAME OF THE !!!TEST!!! TABLE
    private static final String TABLE_NAME = "TEST_TABLE_NAME";
    private static final String DB_NAME = "TEST_DATABASE_NAME";
    private static final String USER_NAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    public static final int EXACT_MATCH = 0;
    public static final int PARTIAL_MATCH = 1;
    public static final int POPULATION_LARGER = 0;
    public static final int POPULATION_SMALLER = 1;

    private final MetropolisEntry[] entries = {new MetropolisEntry("Mumbai", "Asia", 20400000L),
            new MetropolisEntry("New York", "North America", 21295000L),
            new MetropolisEntry("San Francisco", "North America", 5780000L),
            new MetropolisEntry("London", "Europe", 8580000L),
            new MetropolisEntry("Rome", "Europe", 2715000L),
            new MetropolisEntry("Melbourne", "Australia", 3900000L),
            new MetropolisEntry("Tbilisi", "Europe", 1000000L),
            new MetropolisEntry("Quteia", "Simon", 0L),
            new MetropolisEntry("TryingToSQLInject\"--;DROP TABLE METROPOLISES;", "Australia", 100L),
            new MetropolisEntry("Batumi", "Europe", -1000L)};

    private MetropolisTableModel model;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NAME, USER_NAME, PASSWORD);
        Statement statement = conn.createStatement();
        statement.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
        statement.execute("CREATE TABLE " + TABLE_NAME + " (metropolis CHAR(64),\n" +
                " continent CHAR(64),\n" +
                " population BIGINT);");
        statement.close();
        model = new MetropolisTableModel(new MetropolisDatabase(DB_NAME, TABLE_NAME, USER_NAME, PASSWORD));
    }

    // utility to convert model's row entry to MetropolisEntry object
    private MetropolisEntry getMetropolisEntryAt(int index, MetropolisTableModel model) {
        List<Object> list = new ArrayList<>();
        for(int j = 0; j < MetropolisTableModel.columns.length; j++){
            list.add(model.getValueAt(index, j));
        }
        return new MetropolisEntry(list.toArray());
    }

    public void testEmpty() {
        assertEquals(0, model.getRowCount());
        assertEquals(MetropolisTableModel.columns.length, model.getColumnCount());
        for(int i = 0; i < MetropolisTableModel.columns.length; i++){
            assertEquals(MetropolisTableModel.columns[i], model.getColumnName(i));
        }
    }

    public void testAdd1() {
        model.add(new MetropolisEntry("Mumbai", "Asia", 20400000L));
        assertEquals(1, model.getRowCount());
        assertEquals(MetropolisTableModel.columns.length, model.getColumnCount());
        assertEquals("Mumbai", model.getValueAt(0, 0));
        assertEquals("Asia", model.getValueAt(0, 1));
        assertEquals(20400000L, model.getValueAt(0, 2));
    }

    public void testAdd2() {
        for (int i = 0; i < 4; i++) {
            model.add(entries[i]);
            for (int j = 0; j < MetropolisTableModel.columns.length; j++) {
                assertEquals(entries[i].get(j), model.getValueAt(0, j));
            }
        }
    }

    public void testSearchWithoutOptions() {
        List<MetropolisEntry> addedList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            model.add(entries[i]);
            addedList.add(entries[i]);
        }
        model.search(new MetropolisEntry("", "", null), POPULATION_LARGER, EXACT_MATCH);
        assertEquals(4, model.getRowCount());
        for (int i = 0; i < 4; i++) {
            MetropolisEntry searchedEntry = getMetropolisEntryAt(i, model);
            assertTrue(addedList.contains(searchedEntry));
        }
        model.add(entries[4]);
        assertEquals(1, model.getRowCount());
        MetropolisEntry firstEntry = getMetropolisEntryAt(0,model);
        assertEquals(entries[4], firstEntry);
    }

    public void testSearchLargerPartial(){
        for(int i = 0; i < 8; i++){
            model.add(entries[i]);
        }
        model.search(new MetropolisEntry("a", "", 400L), POPULATION_LARGER, PARTIAL_MATCH);
        assertEquals(2, model.getRowCount());
        model.search(new MetropolisEntry("T", "", 20000L), POPULATION_LARGER, PARTIAL_MATCH);
        assertEquals(1, model.getRowCount());
        assertEquals(entries[6], getMetropolisEntryAt(0, model));
    }

    public void testSearchSmallerExact(){
        for(int i = 0; i < 8; i++) model.add(entries[i]);
        model.search(new MetropolisEntry("", "Simon", 10L), POPULATION_SMALLER, EXACT_MATCH);
        assertEquals(1, model.getRowCount());
        assertEquals(entries[7], getMetropolisEntryAt(0, model));
    }

    public void testInvalid(){
        assertThrows(RuntimeException.class, () -> model.add(entries[8]));
        assertThrows(NumberFormatException.class, () -> model.add(entries[9]));
    }
}
