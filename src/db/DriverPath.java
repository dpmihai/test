package db;

import java.io.*;

import org.launcher.LauncherClassLoader;


/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 26, 2005 Time: 4:58:12 PM
 */
public class DriverPath {

    private static File getFile() throws IOException {
        File file = new File("config\\path.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }


    private static boolean findEntry(File file, String entry) {

        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = input.readLine()) != null) {
                if (line.equals(entry)) {
                    System.out.println("found entry : " + entry);
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public static void addEntry(String entry) throws IOException {
        File file = getFile();
        if (findEntry(file, entry)) {
            return;
        }
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(file, true));
            output.write(entry);
            output.write(System.getProperty("line.separator"));
        } finally {
            //flush and close both "output" and its underlying FileWriter
            if (output != null) {
                output.close();
            }
        }
    }

    public static void loadDrivers() throws IOException {
        File file = getFile();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = input.readLine()) != null) {
                File f = new File(line);
                LauncherClassLoader jarClassLoader = (LauncherClassLoader) DriverPath.class.getClassLoader();
                if (f.isDirectory()) {
                    jarClassLoader.loadJars(f.getAbsolutePath());
                } else {
                    jarClassLoader.loadJar(f);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
