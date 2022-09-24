import java.sql.SQLException;

public class Metropolis {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ignored) {
//        }
        MetropolisDatabase db = new MetropolisDatabase("DB_NAME","metropolises" ,"USERNAME", "PASSWORD");
        MetropolisFrame frame = new MetropolisFrame(new MetropolisTableModel(db));
    }
}
