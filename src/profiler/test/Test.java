package profiler.test;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 15, 2006
 * Time: 10:55:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] args) {

        File dir = new File(".");
        String[] list = dir.list();

        for (String file : list) {
            System.out.println(file);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();                
            }
        }

    }

}



