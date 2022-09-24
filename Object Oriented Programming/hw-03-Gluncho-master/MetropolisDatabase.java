import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetropolisDatabase {
    private static Connection conn;
    private final String table_name;
    public static final int EXACT_MATCH = 0;
    public static final int PARTIAL_MATCH = 1;
    public static final int POPULATION_LARGER = 0;
    public static final int POPULATION_SMALLER = 1;

    /**
     * Constructs a new object.
     */
    public MetropolisDatabase(String database_name,String table_name, String username, String password) throws ClassNotFoundException, SQLException {
        this.table_name = table_name;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", username, password);
        Statement statement = conn.createStatement();
        statement.execute("USE "+ database_name +";");
        statement.close();
    }


    public List<MetropolisEntry> search(MetropolisEntry entry, int populationType, int matchType) {
        List<MetropolisEntry> ans = new ArrayList<>();
        checkEntryValidity(entry);
        try{
            Statement searchStatement = conn.createStatement();
            String statement = getSearchQuery(entry, populationType, matchType);
            //System.out.println(statement);
            ResultSet resultSet = searchStatement.executeQuery(statement);
            while(resultSet.next()){
                ans.add(convertToMetropolisEntry(resultSet));
            }
            searchStatement.close();
        }catch(SQLException ex) { ex.printStackTrace(); }
        return ans;
    }

    // INSERT INTO metropolises VALUES ("Mumbai", "Asia", 140000)
    public void add(MetropolisEntry entry) {
        checkEntryValidity(entry);
        try{
            Statement addStatement = conn.createStatement();
            String insertQ = "INSERT INTO "+ table_name + " VALUES " + entry +";";
            addStatement.executeUpdate(insertQ);
            addStatement.close();
        }catch(SQLException ex){ex.printStackTrace();}
    }

    private String getSearchQuery(MetropolisEntry entry, int populationType, int matchType) {
        StringBuilder b = new StringBuilder("SELECT * FROM " + table_name);
        String name =  (String) entry.get(MetropolisTableModel.NAME),cont = (String) entry.get(MetropolisTableModel.CONTINENT);
        Long population = (Long) entry.get(MetropolisTableModel.POPULATION);
        if(name.equals("") && cont.equals("") && population == null) {
            b.append(";");
            return b.toString();
        }
        b.append(" WHERE ");
        if(addPopulationInfo(b, population, populationType)) b.append(" AND ");
        addNameContinentInfo(b, name, cont, matchType);
        b.append(";");
        return b.toString();
    }

    /**
     * Adds population info in where clause
     * @param b StringBuilder
     * @param population Numeric value of population of the entry
     * @param populationType - Can either be 0 or 1
     * @return true if population info was added successfully in StringBuilder
     */
    private boolean addPopulationInfo(StringBuilder b, Long population, int populationType) {
        if(population == null) return false;
        b.append(MetropolisTableModel.columns[MetropolisTableModel.POPULATION]);
        if(populationType == POPULATION_LARGER) b.append(">=");
        else if(populationType == POPULATION_SMALLER) b.append("<");
        b.append(population);
        return true;
    }

    /**
     * Adds name and continent info in a where clause.
     */
    private void addNameContinentInfo(StringBuilder b, String name, String continent,int matchType) {
        b.append(MetropolisTableModel.columns[MetropolisTableModel.NAME]);
        appendWhereCondition(b, name, matchType);
        b.append(" AND ").append(MetropolisTableModel.columns[MetropolisTableModel.CONTINENT]);
        appendWhereCondition(b, continent, matchType);
    }

    private void appendWhereCondition(StringBuilder b, String text, int matchType) {
        if(text.equals("")) b.append(" LIKE '%'");
        else if(matchType == EXACT_MATCH) b.append("='").append(text).append("'");
        else if(matchType == PARTIAL_MATCH) b.append(" LIKE '%").append(text).append("%'");
    }


    private void checkEntryValidity(MetropolisEntry entry){
        String name = (String) entry.get(MetropolisTableModel.NAME);
        String continent = (String) entry.get(MetropolisTableModel.CONTINENT);
        Long popu = (Long) entry.get(MetropolisTableModel.POPULATION);
        if(popu != null && popu < 0) throw new NumberFormatException("Population can not be negative");
        checkStringValidity(name);
        checkStringValidity(continent);

    }

    // This method is used to avoid basic SQL injections.
    // It's weak, but it works for most cases for MySQL
    private void checkStringValidity(String str){
        if(str.contains(";") || str.contains("--") || str.contains("\\") || str.contains("\"") || str.contains("'")){
            throw new RuntimeException("These kinds of symbols are not allowed due to security reasons.");
        }
    }

    /**
     * Converts result set entry (current row) in a MetropolisEntry type object.
     * @param rs ResultSet
     * @return MetropolisEntry, obtained by converting resultSet
     */
    private MetropolisEntry convertToMetropolisEntry(ResultSet rs) throws SQLException {
        // Column enumeration starts from 1 in ResultSet class, thats why we need +1's.
        String name = rs.getString(MetropolisTableModel.NAME + 1);
        String continent = rs.getString(MetropolisTableModel.CONTINENT + 1);
        Long population = rs.getLong(MetropolisTableModel.POPULATION + 1);
        return new MetropolisEntry(name, continent, population);
    }
}
