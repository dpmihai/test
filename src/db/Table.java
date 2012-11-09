package db;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 26, 2005 Time: 11:20:38 AM
 */
public class Table {

    private String name;
    private String type;
    private List indexes;

    public Table(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Table(String name, String type, List indexes) {
        this.name = name;
        this.type = type;
        this.indexes = indexes;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List getIndexes() {
        return indexes;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;

        final Table table = (Table) o;

        if (indexes != null ? !indexes.equals(table.indexes) : table.indexes != null) return false;
        if (name != null ? !name.equals(table.name) : table.name != null) return false;
        if (type != null ? !type.equals(table.type) : table.type != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + (type != null ? type.hashCode() : 0);
        result = 29 * result + (indexes != null ? indexes.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Table name=").append(name).
           append(" Table type=").append(type);
        if ((indexes != null) && (indexes.size()>0) ) { 
           sb.append(" Indexes=").append(indexes);
        }
        return new String(sb.toString());
    }
}
