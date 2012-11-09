package image;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 2, 2006
 * Time: 3:50:29 PM
 */
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * ImageScaler
 *
 * This class loads all images in a given directory and scales them to the
 * given sizes, saving the results as JPEG files in a new "scaled/" subdirectory
 * of the original directory.
 */
public class ImageScaler {

    // Default w/h values; overriden by command-line -width/-height parameters
    static int IMAGE_W = 16;
    static int IMAGE_H = 16;

    public static void main(String args[]) {
        // Default directory is current directory, overridden by -dir parameter
        String imagesDir = ".";
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-dir") && ((i + 1) < args.length)) {
                imagesDir = args[++i];
            } else if (args[i].equals("-width") && ((i + 1) < args.length)) {
                IMAGE_W = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-height") && ((i + 1) < args.length)) {
                IMAGE_H = Integer.parseInt(args[++i]);
            }
        }
        // new subdirectory for scaled images
        String scaledImagesDir = imagesDir + File.separator + "scaled";
        // directory that holds original images
        File cwd = new File(imagesDir);
        // directory for scaled images
        File subdir = new File(scaledImagesDir);
        subdir.mkdir();
        File files[] = cwd.listFiles();
        // temporary image for every scaled instance
        BufferedImage scaledImg = new BufferedImage(IMAGE_W, IMAGE_H,
                                                    BufferedImage.TYPE_INT_RGB);
        Graphics2D gScaledImg = scaledImg.createGraphics();
        // Note the use of BILNEAR filtering to enable smooth scaling
        gScaledImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (int i = 0; i < files.length; ++i) {
            try {
                // For every file in the directory, assume it's an image and
                // load it
                BufferedImage img = ImageIO.read(files[i]);
                // If we get here, we must have read the image file successfully.
                // Create a new File in the scaled subdirectory
                File scaledImgFile = new File(scaledImagesDir + File.separator +
                                              files[i].getName());
                // Scale the original image into the temporary image
                gScaledImg.drawImage(img, 0, 0, IMAGE_W, IMAGE_H, null);
                // Save the scaled version out to the file
                ImageIO.write(scaledImg, "jpeg", scaledImgFile);
            } catch (Exception e) {
                System.out.println("Problem with " + files[i]);
            }
        }
    }
}