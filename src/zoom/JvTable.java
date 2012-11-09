package zoom;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:19:46 PM
 * To change this template use File | Settings | File Templates.
 */
class JvTable extends JTable
{
    private Font originalFont;
    private int originalRowHeight;
    private float zoomFactor = 1.0f;


    public JvTable(TableModel dataModel)
    {
        super(dataModel);
    }


    @Override
    public void setFont(Font font)
    {
        originalFont = font;
        // When setFont() is first called, zoomFactor is 0.
        if (zoomFactor != 0.0 && zoomFactor != 1.0)
        {
            float scaledSize = originalFont.getSize2D() * zoomFactor;
            font = originalFont.deriveFont(scaledSize);
        }

        super.setFont(font);
    }


    @Override
    public void setRowHeight(int rowHeight)
    {
        originalRowHeight = rowHeight;
        int h = (int) Math.ceil(originalRowHeight * zoomFactor);
        if (h <= 0) {
            h = originalRowHeight;
        }
        super.setRowHeight(h);
    }


    public float getZoom()
    {
        return zoomFactor;
    }


    public void setZoom(float zoomFactor)
    {

        if (zoomFactor <= 0) {
            return;
        }

        if (this.zoomFactor == zoomFactor)
            return;

        if (originalFont == null)
            originalFont = getFont();
        if (originalRowHeight == 0)
            originalRowHeight = getRowHeight();

        float oldZoomFactor = this.zoomFactor;
        this.zoomFactor = zoomFactor;
        Font font = originalFont;
        if (zoomFactor != 1.0)
        {
            float scaledSize = originalFont.getSize2D() * zoomFactor;
            font = originalFont.deriveFont(scaledSize);
        }

        super.setFont(font);
        int h = (int) Math.ceil(originalRowHeight * zoomFactor);
        if (h <= 0) {
            h = originalRowHeight;
        }
        super.setRowHeight(h);
        getTableHeader().setFont(font);

        firePropertyChange("zoom", oldZoomFactor, zoomFactor);
    }


    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
    {
        Component comp = super.prepareRenderer(renderer, row, column);
        comp.setFont(this.getFont());
        return comp;
    }


    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column)
    {
        Component comp = super.prepareEditor(editor, row, column);
        comp.setFont(this.getFont());
        return comp;
    }
}