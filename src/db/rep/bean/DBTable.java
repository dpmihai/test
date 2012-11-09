package db.rep.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Decebal Suiu
 */
public class DBTable implements Serializable {
            
    public String name;
    public String alias;
    public String type;
    public List columns;    
        
    public void checkAllColumns(boolean checked) {
        int size = columns.size();
        for (int i = 0; i < size; i++) {
    	    ((DBColumn) columns.get(i)).select = checked;
        }
    }
    
    public int countCheckedColumns() {
        int counter = 0;
        int size = columns.size();
        for (int i = 0; i < size; i++) {
    	    if (((DBColumn) columns.get(i)).select) {
    	        counter++;
    	    }
        }        
        
        return counter;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        	.append("name", name)
        	.append("alias", alias)
        	.append("type", type)
        	.append("columns", columns).toString();
    }
    
}
