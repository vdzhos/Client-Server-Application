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

    private static Storage instance;

    private Map<String, Product> productMap;
    private Map<String, ProductGroup> productGroupMap;

    public static Storage getInstance(){
        if(instance == null){
            instance = new Storage();
        }
        return instance;
    }

    private Storage() {
        productMap = new HashMap<>();
        productGroupMap = new HashMap<>();
        initData();
    }

    private void initData(){
        ProductGroup group1 = new ProductGroup(1L,"Group1");
        ProductGroup group2 = new ProductGroup(2L,"Group2");
        productGroupMap.put(group1.getName(),group1);
        productGroupMap.put(group2.getName(),group2);
        long counter = 1;
        for (int i = 0; i < 3; i++) {
            Product product = createRandomProduct(counter++,group1);
            addProductToStorageAndGroup(product,group1);
        }
        for (int i = 0; i < 5; i++) {
            Product product = createRandomProduct(counter++,group2);
            addProductToStorageAndGroup(product,group2);
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

    public int getProductQuantity(){
        return 0;
    }

    public boolean decreaseProductQuantity(){
        return false;
    }

    public boolean increaseProductQuantity(){
        return false;
    }

    public boolean addProductGroup(){
        return false;
    }

    public boolean addProductToGroup(){
        return false;
    }

    public boolean setProductPrice(){
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return Objects.equals(productMap, storage.productMap) && Objects.equals(productGroupMap, storage.productGroupMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productMap, productGroupMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Storage:\n");
        sb.append("---------Products---------\n");
        for (Product product: productMap.values()) {
            sb.append("     ").append(product).append("\n");
        }
        sb.append("---------Product Groups---------\n");
        for (ProductGroup group: productGroupMap.values()) {
            sb.append("     ").append(group).append("\n");
        }
        return sb.toString();
    }
}
