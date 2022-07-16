package database;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCriteriaQuery {

    private List<Long> groupIds;

    private String textInName;

    private Integer lowerQuantity;
    private Integer upperQuantity;

    private Double lowerPrice;
    private Double upperPrice;

    ProductCriteriaQuery(){}

    public String getQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append(Queries.GET_PRODUCTS_BY_CRITERIA_JOIN);
        boolean firstCondition = true;
        if(groupIds!=null && groupIds.size()!=0) {
            firstCondition(sb,firstCondition);
            sb.append("groupId in (");
            for (Long id: groupIds){
                sb.append(id).append(',');
            }
            sb.deleteCharAt(sb.length()-1).append(")");
            firstCondition = false;
        }
        if(textInName!=null) {
            firstCondition(sb,firstCondition);
            sb.append("product.name like '%")
                    .append(textInName)
                    .append("%'");
            firstCondition = false;
        }
        if(lowerQuantity!=null) {
            firstCondition(sb,firstCondition);
            sb.append("quantity >= ").append(lowerQuantity);
            firstCondition = false;
        }
        if(upperQuantity!=null){
            firstCondition(sb,firstCondition);
            sb.append("quantity <= ").append(upperQuantity);
            firstCondition = false;
        }
        if(lowerPrice!=null){
            firstCondition(sb,firstCondition);
            sb.append("price >= ").append(lowerPrice);
            firstCondition = false;
        }
        if(upperPrice!=null){
            firstCondition(sb,firstCondition);
            sb.append("price <= ").append(upperPrice);
            firstCondition = false;
        }
        return sb.append(';').toString();
    }

    private void firstCondition(StringBuilder sb, boolean firstCondition){
        if(firstCondition) sb.append(" where ");
        else sb.append(" and ");
    }

}
