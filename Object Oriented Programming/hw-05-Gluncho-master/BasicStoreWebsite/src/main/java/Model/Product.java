package Model;

import java.util.HashMap;

public class Product {
    private String productId;
    private String name;
    private String imageFilename;
    private double price;

    /**
     * Constructs a new object.
     */
    public Product(String productId, String name, String imageFilename, double price) {
        this.productId = productId;
        this.name = name;
        this.imageFilename = imageFilename;
        this.price = price;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return other.getProductId().equals(this.getProductId());
    }
}
