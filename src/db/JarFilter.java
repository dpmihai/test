package db;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 26, 2005 Time: 2:39:06 PM
 */
public class JarFilter extends FileFilter {

    public final static String jar = "jar";

    /**
     * Accept all directories and all rtf files.
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return isJar(f);
    }

    /**
     * Get the extension of a file.
     * @param f file
     * @return file extension
     */
    private static String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Test if file is jar
     * @param f file
     * @return true if file is jar, false otherwise
     */
    public static boolean isJar(File f) {
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(jar)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * The description of this filter
     * @return filter description
     */
    public String getDescription() {
        return "Jar File";
    }
}

