package registry;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jul 22, 2008
 * Time: 11:20:24 AM
 *
 * // http://today.java.net/pub/a/today/2008/07/10/distributing-web-start-via-cd-rom.html
 */
public class JavaPath {

    private Vector<String> getRegEntries(String key) {
        Vector<String> results = new Vector<String>();

        try {
            Process proc = Runtime.getRuntime().exec("REG QUERY " + key);
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader br = new BufferedReader(isr);
            String result = "";
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                results.add(line);
            }

            return results;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String getInstalledPath(
            int majorMin, int minorMin, int revMin,
            int majorMax, int minorMax, int revMax) {

        String installedPath = null;
        int latestVersion = 0;

        String keyRoot = "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft" +
                         "\\Java Runtime Environment";
        Vector results = getRegEntries("\"" + keyRoot + "\" /s");
        int numEntries = results.size();
        for (int i = 0; i < numEntries; i++) {
            String key = results.get(i++).toString();
            int pos = key.indexOf("Java Runtime Environment");
            if (pos > 0) {
                pos += "Java Runtime Environment".length() + 1;
                String version = key.substring(pos);
                String parts[] = version.split("[._]");
                int majorVersion, minorVersion, revision;
                majorVersion = Integer.parseInt(parts[1]);
                if (parts.length > 3) {
                    minorVersion = Integer.parseInt(parts[2]);
                } else {
                    minorVersion = 0;
                }
                if (parts.length > 4) {
                    revision = Integer.parseInt(parts[3]);
                } else {
                    revision = 0;
                }
                if (((majorVersion == -1) || (majorVersion >= majorMin)) &&
                        ((majorVersion == -1) || (majorVersion <= majorMax))) {
                    if (((minorMin == -1) || (minorVersion >= minorMin)) &&
                            ((minorMax == -1) || (minorVersion <= minorMax))) {
                        if (((revMin == -1) || (revision >= revMin)) &&
                                ((revMax == -1) || (revision <= revMax))) {
                            // Prefer the neweset acceptable version
                            int thisVersion = majorVersion * 10000 + minorVersion * 100 + revision;
                            if (thisVersion > latestVersion) {
                                String value = null;
                                while (i < numEntries) {
                                    value = results.get(i++).toString().trim();
                                    if (value.startsWith("JavaHome"))
                                        break;
                                }

                                installedPath = value.substring(value.indexOf("REG_SZ") + 6).trim();
                                latestVersion = thisVersion;
                            }
                        }
                    }
                }
            }
        }

//        Vector<String> all = getRegEntries("\"HKEY_LOCAL_MACHINE\\SOFTWARE\\ej-technologies\\install4j\\installations\" /s");
//        System.out.println("LOOK");
//        for  (String s : all) {
//            System.out.println("s="+s);
//        }


        return installedPath;
    }

    public static void main(String[] args) {
        JavaPath jp = new JavaPath();
        String path = jp.getInstalledPath(3, 0, 0, 5, 0, 0);
        System.out.println("Path=" +path);
    }


}
