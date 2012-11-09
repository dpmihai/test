package test;

/**
 * User: mihai.panaitescu
 * Date: 13-May-2010
 * Time: 13:37:24
 */
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherProperty;
import org.apache.poi.ddf.EscherComplexProperty;
import org.apache.poi.ddf.EscherProperties;

import java.util.List;
import java.util.Iterator;

/**
 * Encapsulate the following information about a picture on an Excel
 * spreadsheet;
 *
 * The name of the picture.
 * The numbers of rows and columns that together identify the cells at the top
 * left and bottom right hand corners of the picture.
 * Any offset within those cells. Apparantly, these offset will always be in the
 * range from 0 to 1023.
 *
 * @author MB
 */
public class ShapeInformation {

    private EscherOptRecord optRecord = null;
    private EscherClientAnchorRecord anchrRecord = null;
    private EscherProperty fileProperty = null;
    private String encoding = null;
    private String pictureFilename = null;
    private int level = 0;

    public ShapeInformation(int level) {
        this(level, "UTF-16LE");
    }

    public ShapeInformation(int level, String encoding) {
        this.level = level;
        this.encoding = encoding;
    }

    public void setOptRecord(EscherOptRecord optRecord) {
        this.optRecord = optRecord;
        List propList = this.optRecord.getEscherProperties();
        Iterator iter = propList.iterator();
        while(iter.hasNext()) {
            EscherProperty property = (EscherProperty)iter.next();
            if(property.getPropertyNumber() == EscherProperties.BLIP__BLIPFILENAME) {
                if(property.isComplex()) {
                    EscherComplexProperty complexProp = (EscherComplexProperty)property;
                    try {
                        this.pictureFilename = new String(complexProp.getComplexData(), "UTF-16LE").trim();
                    }
                    catch(java.io.UnsupportedEncodingException ueEx) {
                        this.pictureFilename = null;
                    }
                }
            }
        }
    }

    public void setAnchorRecord(EscherClientAnchorRecord anchrRecord) {
        this.anchrRecord = anchrRecord;
    }

    public String getPictureFilename() {
        return(this.pictureFilename);
    }

    /**
     * Get the row number for the top left hand corner of the picture.
     *
     * @return Get the row number for the top left hand corner of the picture.
     */
    public short getRow1() {
        return(this.anchrRecord.getRow1());
    }

    /**
     * Get the row number for the bottom right hand corner of the picture.
     *
     * @return Get the row number for the bottom right hand corner of the
     *         picture.
     */
    public short getRow2() {
        return(this.anchrRecord.getRow2());
    }

    /**
     * Get the column number for the top left hand corner of the picture.
     *
     * @return Get the column number for the top left hand corner of the
     *         picture.
     */
    public short getCol1() {
        return(this.anchrRecord.getCol1());
    }

    /**
     * Get the column number for the bottom right hand corner of the picture.
     *
     * @return Get the column number for the bottom right hand corner of the
     *         picture.
     */
    public short getCol2() {
        return(this.anchrRecord.getCol2());
    }

    /**
     * Get the amount the image is offset from the top edge of the cell at
     * the top left hand corner of the image. This number will always be
     * between 0 and 1023.
     *
     * @return Get the offset form the top of the cell at the top left hand
     *         corner of the image.
     */
    public short getOffsetRow1() {
        return(this.anchrRecord.getDy1());
    }

    /**
     * Get the amount the image is offset from the bottom of the cell at
     * the bottom right hand corner of the image. This number will always be
     * between 0 and 1023.
     *
     * @return Get the offset from the bottom of the cell at the bottom right
     *         hand corner of the image.
     */
    public short getOffsetRow2() {
        return(this.anchrRecord.getDy2());
    }

    /**
     * Get the amount the image is offset from the right hand edge of the cell
     * at the top left hand corner of the image.
     *
     * @return Get the offset from the right hand edge of the cell at the top
     *         left hand corner of the image.
     */
    public short getOffsetCol1() {
        return(this.anchrRecord.getDx1());
    }

    /**
     * Get the amount the image is offset from the right hand edge of the cell
     * at the bottom right hand corner of the image.
     *
     * @return Get the offset from the top of the cell at the bottom right hand
     *         corner of the image.
     */
    public short getOffsetCol2() {
        return(this.anchrRecord.getDx2());
    }
}