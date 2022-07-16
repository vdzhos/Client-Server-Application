package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class ProductGroup {

    private Long id;
    private String name;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductGroup that = (ProductGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        for (Product product : products) {
//            sb.append(product.getName()).append(",");
//        }
//        sb.deleteCharAt(sb.length()-1).append("]");
        return "ProductGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description = '" + description + '\'' +
//                ", products=" + sb.toString() +
                '}';
    }
}
