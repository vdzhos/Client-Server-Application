package database;

public class ProductGroupCriteriaQueryBuilder {

    private ProductGroupCriteriaQuery object = new ProductGroupCriteriaQuery();

    public ProductGroupCriteriaQuery build() {
        return object;
    }

    public ProductGroupCriteriaQueryBuilder setTextInName(String value) {
        object.setTextInName(value);
        return this;
    }

    public ProductGroupCriteriaQueryBuilder setTextInDescription(String value) {
        object.setTextInDescription(value);
        return this;
    }

    public ProductGroupCriteriaQueryBuilder setProperty(String name, String value) throws Exception{
        try{
            switch (name){
                case "textInName":
                    setTextInName(value);
                    break;
                case "textInDescription":
                    setTextInDescription(value);
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