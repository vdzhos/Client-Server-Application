package model;

import lombok.Getter;
import lombok.Setter;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Getter
@Setter
public class Storage {

    private static long productId = 0;
    private static long groupId = 0;

    private static Storage instance;

    private Map<String, Product> productMap;
    private Map<String, ProductGroup> groupMap;

    public static Storage getInstance(){
        if(instance == null){
            instance = new Storage();
        }
        return instance;
    }

    private Storage() {
        initData();
    }

    public void initData(){
        productMap = new HashMap<>();
        groupMap = new HashMap<>();
        productId = 0;
        groupId = 0;
        ProductGroup group0 = new ProductGroup(groupId++,"Group0");
        ProductGroup group1 = new ProductGroup(groupId++,"Group1");
        groupMap.put(group0.getName(),group0);
        groupMap.put(group1.getName(),group1);
        for (int i = 0; i < 3; i++) {
            Product product = createRandomProduct(productId++,group0);
            addProductToStorageAndGroup(product,group0);
        }
        for (int i = 0; i < 5; i++) {
            Product product = createRandomProduct(productId++,group1);
            addProductToStorageAndGroup(product,group1);
        }
    }

    private Product createRandomProduct(long counter, ProductGroup group){
        double price = Utils.getRandomDouble(1000,2);
        int quantity = new Random().nextInt(101);
        return new Product(counter,"Product"+counter,price,quantity,group);
    }

    private void addProductToStorageAndGroup(Product product, ProductGroup group){
        productMap.put(product.getName(),product);
        group.getProducts().add(product);
    }

    public int getProductQuantity(String productName) throws Exception {
        if(!productMap.containsKey(productName)) throw new Exception("No product with such name!");
        return productMap.get(productName).getQuantity();
    }

    public void decreaseProductQuantity(String productName, int quantity) throws Exception {
        if(!productMap.containsKey(productName)) throw new Exception("No product with such name!");
        Product product = productMap.get(productName);
        synchronized (product){
            int currentQuantity = product.getQuantity();
            if(quantity>currentQuantity)
                throw new Exception("Product quantity is not sufficient!");
            product.setQuantity(currentQuantity-quantity);
        }
    }

    public void increaseProductQuantity(String productName, int quantity) throws Exception {
        if(!productMap.containsKey(productName)) throw new Exception("No product with such name!");
        Product product = productMap.get(productName);
        synchronized (product){
            product.setQuantity(product.getQuantity()+quantity);
        }
    }

    public synchronized void addProductGroup(String groupName) throws Exception {
        if(groupMap.containsKey(groupName)) throw new Exception("Group with such name already exists!");
        ProductGroup group = new ProductGroup(groupId++,groupName);
        groupMap.put(groupName,group);
    }

    public synchronized void addProductToGroup(String productName, String groupName, int quantity, double price) throws Exception {
        if(!groupMap.containsKey(groupName)) throw new Exception("No group with such name!");
        if(productMap.containsKey(productName)) throw new Exception("Product with such name already exists!");
        if(quantity<0) throw new Exception("Quantity of a product must not be less than zero!");
        if(price<=0) throw new Exception("Price of a product must be greater than zero!");
        ProductGroup group = groupMap.get(groupName);
        Product product = new Product(productId++,productName,price,quantity,group);
        addProductToStorageAndGroup(product,group);
    }

    public void setProductPrice(String productName, double price) throws Exception {
        if(!productMap.containsKey(productName)) throw new Exception("No product with such name!");
        if(price<=0) throw new Exception("Price of a product must be greater than 0!");
        Product product = productMap.get(productName);
        product.setPrice(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return Objects.equals(productMap, storage.productMap) && Objects.equals(groupMap, storage.groupMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productMap, groupMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Storage:\n");
        sb.append("---------Products---------\n");
        for (Product product: productMap.values()) {
            sb.append("     ").append(product).append("\n");
        }
        sb.append("---------Product Groups---------\n");
        for (ProductGroup group: groupMap.values()) {
            sb.append("     ").append(group).append("\n");
        }
        return sb.toString();
    }


}
