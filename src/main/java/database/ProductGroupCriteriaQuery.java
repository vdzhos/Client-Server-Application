package database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductGroupCriteriaQuery{

    private String textInName;
    private String textInDescription;

    ProductGroupCriteriaQuery(){}

    public String getQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append(Queries.GET_GROUPS);
        boolean firstCondition = true;
        if(textInName!=null) {
            firstCondition(sb,firstCondition);
            sb.append("product_group.name like '%")
                    .append(textInName)
                    .append("%'");
            firstCondition = false;
        }
        if(textInDescription!=null) {
            firstCondition(sb,firstCondition);
            sb.append("product_group.description like '%")
                    .append(textInDescription)
                    .append("%'");
        }
        return sb.append(';').toString();
    }

    private void firstCondition(StringBuilder sb, boolean firstCondition){
        if(firstCondition) sb.append(" where ");
        else sb.append(" and ");
    }

}
