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

    public ProductCriteriaQueryBuilder setTextInDescription(java.lang.String value) {
        object.setTextInDescription(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setTextInManufacturer(java.lang.String value) {
        object.setTextInManufacturer(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setUpperQuantity(java.lang.Integer value) {
        object.setUpperQuantity(value);
        return this;
    }

    public ProductCriteriaQueryBuilder setProperty(String name, String value) throws Exception{
        try{
            switch (name){
                case "textInName":
                    setTextInName(value);
                    break;
                case "textInDescription":
                    setTextInDescription(value);
                    break;
                case "textInManufacturer":
                    setTextInManufacturer(value);
                    break;
                case "lowerQuantity":
                    setLowerQuantity(Integer.parseInt(value));
                    break;
                case "upperQuantity":
                    setUpperQuantity(Integer.parseInt(value));
                    break;
                case "lowerPrice":
                    setLowerPrice(Double.parseDouble(value));
                    break;
                case "upperPrice":
                    setUpperPrice(Double.parseDouble(value));
                    break;
                default:
                    throw new Exception("No such property!");
            }
        }catch (NumberFormatException e){
            throw new Exception("Incorrect parameter value type!");
        }
        return this;
    }

}