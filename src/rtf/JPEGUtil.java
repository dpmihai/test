package rtf;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2007
 * Time: 2:01:34 PM
 */
public class JPEGUtil {

    public static byte[] getComponentAsJPEG(JLabel lbl) {
        byte[] b = new byte[0];
        Dimension size = lbl.getSize();
        BufferedImage myImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = myImage.createGraphics();
        lbl.paint(g2);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(myImage);
            b = out.toByteArray();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }


    public static String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            // look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            // look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    // table to convert a nibble to a hex char.
    static char[] hexChar = {
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'};

    public static ImageIcon objectFromByte(byte[] foto) {
        if (foto == null)
            return null;
        return new ImageIcon(foto);
    }
}
