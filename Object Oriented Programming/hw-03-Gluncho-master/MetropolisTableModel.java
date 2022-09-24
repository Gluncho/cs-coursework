import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.*;

public class MetropolisTableModel extends AbstractTableModel {
    private List<MetropolisEntry> metros;
    private MetropolisDatabase db;
    public static final String[] columns = {"Metropolis", "Continent", "Population"};
    public static final int NAME = 0;
    public static final int CONTINENT = 1;
    public static final int POPULATION = 2;
    protected MetropolisTableModel(MetropolisDatabase base) {
        super();
        metros = new ArrayList<>();
        this.db = base;
    }

    @Override
    public int getRowCount() {
        return metros.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return metros.get(rowIndex).get(columnIndex);
    }

    public void add(MetropolisEntry entry){
        metros.clear();
        metros.add(entry);
        db.add(entry);
        fireTableDataChanged();
    }

    public void search(MetropolisEntry entry, int populationType, int matchType) {
        metros = db.search(entry, populationType, matchType);
        fireTableDataChanged();
    }
}
