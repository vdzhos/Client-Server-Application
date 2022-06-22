package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Storage {

    private Map<String, Product> productMap = new HashMap<>();
    private Map<String, ProductGroup> productGroupMap = new HashMap<>();

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
}
