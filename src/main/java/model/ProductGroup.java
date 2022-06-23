package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class ProductGroup {

    private Long id;
    private String name;

    private List<Product> products;

    public ProductGroup(Long id, String name) {
        this.id = id;
        this.name = name;
        products = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductGroup that = (ProductGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, products);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Product product : products) {
            sb.append(product.getName()).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append("]");
        return "ProductGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + sb.toString() +
                '}';
    }
}
