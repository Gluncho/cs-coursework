package Dao;

import Model.Product;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection conn;
    private List<Product> products;
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "rootroot";
    private static final String DATABASE_NAME = "oopdatabase";

    /**
     * Constructs a new object.
     */
    public ProductDAO()  {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ignored) {}

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", USER_NAME, PASSWORD);
            Statement stm = conn.createStatement();
            stm.execute("USE " + DATABASE_NAME + ";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Product> getAllProducts(){
        if(products == null){
            products = new ArrayList<>();
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM products");
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    String id = rs.getString(1);
                    String name = rs.getString(2);
                    String imageName = rs.getString(3);
                    double price = rs.getDouble(4);
                    Product product = new Product(id, name, imageName, price);
                    products.add(product);
                }
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return products;
    }

    public Product getProduct(String product_id) {
        if(products == null) getAllProducts();
        for(Product p : products){
            if(p.getProductId().equals(product_id)) return new Product(p.getProductId(), p.getName(), p.getImageFilename(), p.getPrice());
        }
        return null;
    }

    public static void main(String[] args) {
        new ProductDAO().getAllProducts();
    }
}
