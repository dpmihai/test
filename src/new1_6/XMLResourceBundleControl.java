package new1_6;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 1, 2007
 * Time: 3:16:43 PM
 */

import java.io.*;
import java.net.*;
import java.util.*;

//resource bundles in XML
public class XMLResourceBundleControl extends ResourceBundle.Control {
    
    //loaded resource bundles are cached by the system.
    //Thus, the second time you fetch a bundle from the same class loader, the loading of the
    //bundle is instantaneous, as it never left memory. If you wish to reset the cache and clear
    //out loaded bundles, call the static clearCache() method of ResourceBundle.

    private static String XML = "xml";

    public List<String> getFormats(String baseName) {
        return Collections.singletonList(XML);
    }

    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
                                    boolean reload) throws IllegalAccessException, InstantiationException, IOException {

        if ((baseName == null) || (locale == null) || (format == null) || (loader == null)) {
            throw new NullPointerException();
        }
        ResourceBundle bundle = null;
        if (format.equals(XML)) {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, format);
            //System.out.println("bundleName="+bundleName + "  resName="+resourceName);
            URL url = loader.getResource("new1_6/" + resourceName);
            System.out.println("url=" + url);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    if (reload) {
                        connection.setUseCaches(false);
                    }
                    InputStream stream = connection.getInputStream();
                    if (stream != null) {
                        BufferedInputStream bis = new BufferedInputStream(stream);
                        bundle = new XMLResourceBundle(bis);
                        bis.close();
                    }
                }
            }
        }
        return bundle;
    }


    private static class XMLResourceBundle extends ResourceBundle {
        private Properties props;

        XMLResourceBundle(InputStream stream) throws IOException {
            props = new Properties();
            props.loadFromXML(stream);
        }

        protected Object handleGetObject(String key) {
            return props.getProperty(key);
        }

        public Enumeration<String> getKeys() {
            Set<String> handleKeys = props.stringPropertyNames();
            return Collections.enumeration(handleKeys);
        }
    }

    public static void main(String args[]) {
        ResourceBundle bundle = ResourceBundle.getBundle("Strings", new XMLResourceBundleControl());
        String string = bundle.getString("Key");
        System.out.println("Key: " + string);
    }
}