package reference.soft;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 12:04:51 PM
 */
import java.awt.Graphics;
import java.awt.Image;
import java.applet.Applet;
import java.lang.ref.SoftReference;

/**
 * An object is softly reachable if it is not strongly reachable and there is a path to it with no weak
 * or phantom references, but one or more soft references. The garbage collector might or might not reclaim
 * a softly reachable object depending on how recently the object was created or accessed, but is required
 * to clear all soft references before throwing an OutOfMemoryError.

   If heap memory is running low, the garbage collector may, at its own discretion, find softly reachable
   objects that have not been accessed in the longest time and clear them (set their reference field to null).

   SoftReference objects work well in applications that, for example, put a large number of images into memory
   and there is no way to know if the application (driven by end user input) will access a given image again.
   If the garbage collector reclaims an image that has not been accessed for a long time, and the application needs
   to access it again, the application reloads the image and displays it.
 */

public class DisplayImage extends Applet {

        SoftReference<Image> sr = null;

        public void init() {
            System.out.println("Initializing");
        }

        public void paint(Graphics g) {
            Image im = (sr == null) ? null : (Image)(sr.get());
            if (im == null) {
                System.out.println("Fetching image");
                im = getImage(getCodeBase(), "truck1.gif");
                sr = new SoftReference<Image>(im);
            }
            System.out.println("Painting");
            g.drawImage(im, 25, 25, this);
            im = null;
            /* Clear the strong reference to the image */
        }

        public void start() {
            System.out.println("Starting");
        }

        public void stop() {
            System.out.println("Stopping");
        }

}