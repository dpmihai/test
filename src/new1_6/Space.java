package new1_6;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 1, 2007
 * Time: 3:37:48 PM
 */

import java.io.*;

public class Space {
    public static void main(String args[]) {

        File roots[] = File.listRoots();
        for (File root : roots) {
            System.out.printf("%s has %,d of %,d free%n", root.getPath(),
                    root.getUsableSpace(), root.getTotalSpace());
        }
    }
}