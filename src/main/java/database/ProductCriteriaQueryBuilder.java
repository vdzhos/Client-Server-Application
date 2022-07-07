package database;

public class ProductCriteriaQueryBuilder {

    private ProductCriteriaQuery object = new ProductCriteriaQuery();

    public ProductCriteriaQuery build() {
        return object;
    }

    public ProductCriteriaQueryBuilder setLowerQuantity(java.lang.Integer value) {
        object.setLowerQuantity(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setLowerPrice(java.lang.Double value) {
        object.setLowerPrice(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setGroupIds(java.util.List<java.lang.Long> value) {
        object.setGroupIds(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setUpperPrice(java.lang.Double value) {
        object.setUpperPrice(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setTextInName(java.lang.String value) {
        object.setTextInName(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setUpperQuantity(java.lang.Integer value) {
        object.setUpperQuantity(value);
        return this;
    }

}