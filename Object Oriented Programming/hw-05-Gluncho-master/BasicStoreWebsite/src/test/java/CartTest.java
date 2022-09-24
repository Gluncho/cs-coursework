import Model.*;
import junit.framework.TestCase;

import java.util.Map;

public class CartTest extends TestCase {
    private Cart cart;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cart = new Cart();
    }

    public void testEmpty(){
        Map<String, Integer> data = cart.getData();
        assertEquals(data.size(), 0);
    }

    public void testMany(){
        Product p = new Product("1", "2", "3", 2.0);
        Product p2 = new Product("123", "2", "34", 5.0);
        cart.addToCart(p);
        cart.addToCart(p);
        cart.addToCart(p);
        cart.addToCart(p2);
        Map<String,Integer> data = cart.getData();
        assertEquals(Integer.valueOf(3), data.get(p));
        assertEquals(Integer.valueOf(1), data.get(p2));
    }
}
