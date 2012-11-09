package db;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 26, 2005 Time: 11:25:05 AM
 */
public class Index {

    private String indexName;
    private String columnName;

    public Index(String indexName, String columnName) {
        this.indexName = indexName;
        this.columnName = columnName;
    }

    public String getIndexName() {
        return indexName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Index)) return false;

        final Index index = (Index) o;

        if (columnName != null ? !columnName.equals(index.columnName) : index.columnName != null) return false;
        if (indexName != null ? !indexName.equals(index.indexName) : index.indexName != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (indexName != null ? indexName.hashCode() : 0);
        result = 29 * result + (columnName != null ? columnName.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Index name=").append(indexName).
           append(" Column name=").append(columnName);
        return new String(sb.toString());
    }
}
