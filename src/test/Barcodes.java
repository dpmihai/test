package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.BarcodeEAN;

public class Barcodes {
	
	public static void main(String[] args) throws IOException {
//        String path = "images/cougar.jpg";
//        BufferedImage src = ImageIO.read(new File(path));
//        // Convert Image to BufferedImage if required.
//        BufferedImage image = toBufferedImage(src);
//        save(image, "jpg");  // png okay, j2se 1.4+
//        save(image, "bmp");  // j2se 1.5+
//                             // gif okay in j2se 1.6+
        
        BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCodeType(BarcodeEAN.EAN8);        
        codeEAN.setCode("97802016158834444");        
        //codeEAN.setBarHeight(150);          
        Image imageEAN = codeEAN.createAwtImage(Color.BLACK, Color.WHITE);
        save(toBufferedImage(imageEAN), "png");
    }
 
    private static void save(BufferedImage image, String ext) {
        String fileName = "D:/barcode";
        File file = new File(fileName + "." + ext);
        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                               ": " + e.getMessage());
        }
    }
 
    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }

}
