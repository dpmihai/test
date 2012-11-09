package db.rep.bean;

/**
 * @author Decebal Suiu
 */
public class DBRelation {
    
    public static final String INNER_JOIN = "INNER JOIN";
    public static final String OUTER_JOIN = "OUTER JOIN";
    
    public String objectLeft;
    public String fieldLeft; 
    public String objectRight;
    public String fieldRight;
    public String join;

}
