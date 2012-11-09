package db.rep.event;

import db.rep.bean.Report;

/**
 * @author Decebal Suiu
 */
public class ReportEvent {

    public static final int GENERAL_CHANGE = 0;
    public static final int OBJECTS_CHANGE = 1;
    public static final int FIELDS_CHANGE = 2;
    
    private Report report;
    private int change;
    
    public ReportEvent(Report report, int change) {
        this.report = report;
        this.change = change;
    }
    
    public Report getReport() {
        return report;        
    }
    
    public int getChange() {
        return change;
    }
    
}
