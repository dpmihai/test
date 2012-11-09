package db.rep.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Decebal Suiu
 */
public class DBColumn implements Serializable {
    
    public String name;
    public String alias;
    public boolean select;    
    public boolean visible;
        
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("name", name)
            .append("alias", alias)
            .append("select", select)
            .append("visible", visible)
            .toString();
    }

}
