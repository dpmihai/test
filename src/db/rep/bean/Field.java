package db.rep.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Decebal Suiu
 */
public class Field implements Serializable {
    
    public String object;
    public String fieldOrExpression;
    public boolean groupBy;

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("object", object)
                .append("fieldOrExpression", fieldOrExpression)
                .append("groupBy", groupBy)
                .toString();
    }

}
