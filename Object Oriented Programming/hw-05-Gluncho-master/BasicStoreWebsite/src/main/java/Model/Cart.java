package Model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    Map<String, Integer> data;

    /**
     * Constructs a new object.
     */
    public Cart() {
        super();
        data = new HashMap<>();
    }

    public void addToCart(Product product) {
        data.putIfAbsent(product.getProductId(), 0);
        data.put(product.getProductId(), data.get(product.getProductId()) + 1);
    }

    public Map<String,Integer> getData(){
        return data;
    }

    public void updateFrequency(Product product, int frequency){
        if(data.containsKey(product.getProductId())){
            if(frequency <= 0) data.remove(product.getProductId());
            else data.put(product.getProductId(), frequency);
        }
    }
    public void updateFrequency(String id, int frequency){
        if(data.containsKey(id)){
            if(frequency <= 0) data.remove(id);
            else data.put(id, frequency);
        }
    }
}
