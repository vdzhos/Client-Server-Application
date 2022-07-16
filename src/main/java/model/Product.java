package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Product {

    private Long id;
    private String name;
    private String description;
    private String manufacturer;
    private Double price;
    private Integer quantity;
    private Long groupId;

    private String groupName;

    public Product(Long id, String name, String description, String manufacturer, Double price, Integer quantity, Long groupId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(quantity, product.quantity) && Objects.equals(description, product.description) && Objects.equals(groupId, product.groupId) && Objects.equals(groupName, product.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity, description, groupId, groupName);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
