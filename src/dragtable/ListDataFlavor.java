package dragtable;

import java.awt.datatransfer.DataFlavor;

/**
 * @author Decebal Suiu
 */
public class ListDataFlavor extends DataFlavor {

    public ListDataFlavor() {
        super(DataFlavor.javaJVMLocalObjectMimeType + 
                ";class=java.util.List", "List");
    }
    
}
