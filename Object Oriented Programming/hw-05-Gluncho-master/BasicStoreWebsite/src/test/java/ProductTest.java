import Model.Product;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class ProductTest extends TestCase {

    public void testGetters(){
        Product p = new Product("1", "2", "3", 2.0);
        assertEquals(p.getProductId(), "1");
        assertEquals(p.getName(), "2");
        assertEquals(p.getImageFilename(), "3");
        assertEquals(p.getPrice(), 2.0);
    }

    public void testEquals(){
        Product p = new Product("1", "2", "3", 2.0);
        Product equal = new Product("1", "2", "3", 2.0);
        Product differentPrice = new Product("1", "2", "3", 1.0);
        Product differentId = new Product("123", "2", "3", 2.0);
        Product differentName = new Product("1", "23", "3", 2.0);
        Product differentImageFileName = new Product("1", "2", "34", 2.0);
        assertEquals(p, equal);
        assertNotSame(p, differentPrice);
        assertNotSame(p, differentId);
        assertNotSame(p, differentName);
        assertNotSame(p, differentImageFileName);
    }

}
