import java.util.HashMap;

public class MetropolisEntry {
    private Object[] row;

    /**
     * Constructs a new MetropolisEntry object.
     */
    public MetropolisEntry(Object... objects) {
        row = new Object[MetropolisTableModel.columns.length];
        System.arraycopy(objects, 0, row, 0, row.length);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(Object o) {
        if(o == this) return true;

        if(!(o instanceof MetropolisEntry other)) return false;
        for(int i = 0; i < MetropolisTableModel.columns.length; i++){
            if(!(this.get(i).equals(other.get(i)))) {
                return false;
            }
        }
        return true;
    }

    /*
     * Returns an object in specified column
     */
    public Object get(int column){
        return row[column];
    }

    /**
     * Returns a string representation of the object.
     * Flexible to use for SQL insertion operations
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        for(int i = 0; i < row.length; i++){
            // Numbers dont need quotes
            if(!(row[i] instanceof Number)) builder.append("\"");
            builder.append(row[i].toString());
            if(!(row[i] instanceof Number)) builder.append("\"");
            if(i != row.length - 1) builder.append(",");
        }
        builder.append(")");
        return builder.toString();
    }
}
