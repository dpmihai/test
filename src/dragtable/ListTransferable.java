package dragtable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * @author Decebal Suiu
 */
public class ListTransferable implements Transferable {

    private DataFlavor dataFlavor;
    private List data;

    public ListTransferable(List data, DataFlavor dataFlavor) {
        this.data = data;
        this.dataFlavor = dataFlavor;
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return data;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { dataFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (DataFlavor flavor1 : flavors) {
            if (flavor1.equals(flavor)) {
                return true;
            }
        }
        
        return false;
    }
    
}
