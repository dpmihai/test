package db;

import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 26, 2005 Time: 2:41:08 PM
 */
public class PreferencesManager {

        public static final String JAR_PATH_KEY = "JAR_PATH";
        private static PreferencesManager instance;

        public static synchronized PreferencesManager getInstance() {
            if (instance == null) {
                instance = new PreferencesManager();
            }
            return instance;
        }

        private PreferencesManager() {
        }

        public String loadParameter(String key) {
            Preferences prefs = Preferences.userNodeForPackage(PreferencesManager.class);
            return prefs.get(key, null);
        }

        public void storeParameter(String key, String path) {
            Preferences prefs = Preferences.userNodeForPackage(PreferencesManager.class);
            prefs.put(key, path);
        }

}
