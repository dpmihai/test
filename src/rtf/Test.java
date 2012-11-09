package rtf;

import javax.swing.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2007
 * Time: 2:00:56 PM
 */
public class Test {


    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large!");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }


    public static String getRTFImageStringFromIcon(ImageIcon ii) {
        String ret = "";
        byte[] b;
        try {
            JLabel lbl = new JLabel("");
            int width = ii.getIconWidth();
            int height = ii.getIconHeight();
//            lbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            lbl.setHorizontalAlignment(0);
            lbl.setHorizontalTextPosition(0);
            lbl.setSize(width, height);
            lbl.setIcon(ii);
            b = JPEGUtil.getComponentAsJPEG(lbl);
            ret = "{\\pict\\jpegblip\\picw" + width + "\\pich" + height + "\n";
            ret = ret + JPEGUtil.toHexString(b) + "}\n";
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getRTFImageStringFromByte(byte[] b) {
        if (b == null) {
            return "";
        }
        return getRTFImageStringFromIcon(new ImageIcon(b));
    }

    public static void writeFile(File file, byte[] content) throws IOException {
        OutputStream os = new FileOutputStream(file);
        os.write(content);
        os.flush();
        os.close();
    }


    public static void getRTFImageStringFromFile(String path, String outPath) {

        try {
            File file = new File(path);
            byte[] b = getBytesFromFile(file);

            String s = getRTFImageStringFromByte(b);
            writeFile(new File(outPath), s.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }


    public static void main(String[] args) {
        String path = "C:\\Documents and Settings\\mihai.panaitescu\\My Documents\\My Pictures\\footer_eurisko.jpg";
        String outPath = "C:\\Documents and Settings\\mihai.panaitescu\\My Documents\\My Pictures\\footer_eurisko.txt";
        getRTFImageStringFromFile(path, outPath);
    }

}
