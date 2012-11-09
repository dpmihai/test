package db.rep.listener;

import db.rep.event.ReportEvent;

/**
 * @author Decebal Suiu
 */
public interface ReportListener {

    public void reportChanged(ReportEvent event);
    
}
