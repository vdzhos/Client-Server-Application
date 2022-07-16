package utils;

public class EndPoints {

    public static final String PRODUCTS_REGEX = "^/api/products[/ ]*$";
    public static final String GROUPS_REGEX = "^/api/groups[/ ]*$";
    public static final String PRODUCTS_WITH_ID_REGEX = "^/api/products/\\d*[/ ]*$";
    public static final String PRODUCT_QUANTITY_BY_ID_REGEX = "^/api/products/quantity/\\d*[/ ]*$";
    public static final String PRODUCT_INCREASE_QUANTITY_BY_ID_REGEX = "^/api/products/increase/\\d*[/ ]*$";
    public static final String PRODUCT_DECREASE_QUANTITY_BY_ID_REGEX = "^/api/products/decrease/\\d*[/ ]*$";
    public static final String GROUPS_WITH_ID_REGEX = "^/api/groups/\\d*[/ ]*$";
    public static final String TOTAL_PRICE_REGEX = "^/api/statistics/total_price[/ ]*$";
    public static final String TOTAL_PRICE_WITH_ID_REGEX = "^/api/statistics/total_price/\\d*[/ ]*$";

    public static final String PRODUCTS = "/api/products";
    public static final String GROUPS = "/api/groups";
    public static final String LOGIN = "/login";
    public static final String STATISTICS = "/api/statistics";



}
