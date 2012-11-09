package image;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 2, 2006
 * Time: 3:57:53 PM
 * To change this template use File | Settings | File Templates.
 */
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * JpegConverter
 *
 * This class loads all BMP images in a given directory and saves each as a JPEG
 * file in the same directory.  This code is specific to BMP, but it could be
 * easily extended to read images of any type that ImageIO handles.
 */
public class JPEGConverter {

    public static void main(String args[]) {
        // Default directory is current directory, overridden by -dir parameter
        String imagesDir = ".";
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-dir") && ((i + 1) < args.length)) {
                imagesDir = args[++i];
            }
        }
        // directory that holds original images
        File cwd = new File(imagesDir);
        File files[] = cwd.listFiles();
        for (int i = 0; i < files.length; ++i) {
            String fileName = files[i].getName();
            // converstion to lower case just for ease of replacing the
            // filename extension
            String fileNameLC = fileName.toLowerCase();
            if (fileName.endsWith("bmp")) {
                try {
                    // Replace original "bmp" filename extension with "jpg"
                    int extensionIndex = fileNameLC.lastIndexOf("bmp");
                    String fileNameBase = fileName.substring(0, extensionIndex);
                    BufferedImage img = ImageIO.read(files[i]);
                    // create new JPEG file
                    File convertedImgFile =
                            new File(imagesDir + File.separator +
                                     fileNameBase + "jpg");
                    // store original file out in JPEG format
                    ImageIO.write(img, "jpeg", convertedImgFile);
                } catch (Exception e) {
                    System.out.println("Problem with " + files[i]);
                }
            }
        }
    }
}
