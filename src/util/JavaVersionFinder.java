package util;


import java.util.*;
import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Helper class to fetch the stdout and stderr outputs from started Runtime execs
 * Modified from http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4
 *****************************************************************************/
class RuntimeStreamer extends Thread {
    InputStream is;
    String lines;

    RuntimeStreamer(InputStream is) {
        this.is = is;
        this.lines = "";
    }
    public String contents() {
        return this.lines;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader    br  = new BufferedReader(isr);
            String line = null;
            while ( (line = br.readLine()) != null) {
                this.lines += line + "\n";
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }

    /**
     * Execute a command and wait for it to finish
     * @return The resulting stdout and stderr outputs concatenated
     ****************************************************************************/
    public static String execute(String[] cmdArray) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(cmdArray);
            RuntimeStreamer outputStreamer = new RuntimeStreamer(proc.getInputStream());
            RuntimeStreamer errorStreamer  = new RuntimeStreamer(proc.getErrorStream());
            outputStreamer.start();
            errorStreamer.start();
            proc.waitFor();
            return outputStreamer.contents() + errorStreamer.contents();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
    public static String execute(String cmd) {
        String[] cmdArray = { cmd };
        return RuntimeStreamer.execute(cmdArray);
    }
}

/**
 * Helper struct to hold information about one installed java version
 ****************************************************************************/
class JavaInfo {
    public String  path;        //! Full path to java.exe executable file
    public String  version;     //! Version string. "Unkown" if the java process returned non-standard version string
    public boolean is64bits;    //! true for 64-bit javas, false for 32

    /**
     * Calls 'javaPath -version' and parses the results
     * @param javaPath: path to a java.exe executable
     ****************************************************************************/
    public JavaInfo(String javaPath) {
        String versionInfo = RuntimeStreamer.execute( new String[] { javaPath, "-version" } );
        String[] tokens = versionInfo.split("\"");

        if (tokens.length < 2) this.version = "Unkown";
        else this.version = tokens[1];
        this.is64bits = versionInfo.toUpperCase().contains("64-BIT");
        this.path     = javaPath;
    }

    /**
     * @return Human-readable contents of this JavaInfo instance
     ****************************************************************************/
    public String toString() {
        return this.path + ":\n  Version: " + this.version + "\n  Bitness: " + (this.is64bits ? "64-bits" : "32-bits");
    }
}

/**
 * Windows-specific java versions finder
 *****************************************************************************/
public class JavaVersionFinder {

    /**
     * @return: A list of javaExec paths found under this registry key (rooted at HKEY_LOCAL_MACHINE)
     * @param wow64  0 for standard registry access (32-bits for 32-bit app, 64-bits for 64-bits app)
     *               or WinRegistry.KEY_WOW64_32KEY to force access to 32-bit registry view,
     *               or WinRegistry.KEY_WOW64_64KEY to force access to 64-bit registry view
     * @param previous: Insert all entries from this list at the beggining of the results
     *************************************************************************/
    private static List<String> searchRegistry(String key, int wow64, List<String> previous) {
        List<String> result = previous;
        try {
            List<String> entries = WinRegistry.readStringSubKeys(WinRegistry.HKEY_LOCAL_MACHINE, key, wow64);
            for (int i = 0; entries != null && i < entries.size(); i++) {
                String val = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, key + "\\" + entries.get(i), "JavaHome", wow64);
                if (!result.contains(val + "\\bin\\java.exe")) {
                    result.add(val + "\\bin\\java.exe");
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    /**
     * @return: A list of JavaInfo with informations about all javas installed on this machine
     * Searches and returns results in this order:
     *   HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Runtime Environment (32-bits view)
     *   HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Runtime Environment (64-bits view)
     *   HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Development Kit     (32-bits view)
     *   HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Development Kit     (64-bits view)
     *   WINDIR\system32
     *   WINDIR\SysWOW64
     ****************************************************************************/
    public static List<JavaInfo> findJavas() {
        List<String> javaExecs = new ArrayList<String>();

        javaExecs = JavaVersionFinder.searchRegistry("SOFTWARE\\JavaSoft\\Java Runtime Environment", WinRegistry.KEY_WOW64_32KEY, javaExecs);
        javaExecs = JavaVersionFinder.searchRegistry("SOFTWARE\\JavaSoft\\Java Runtime Environment", WinRegistry.KEY_WOW64_64KEY, javaExecs);
        javaExecs = JavaVersionFinder.searchRegistry("SOFTWARE\\JavaSoft\\Java Development Kit",     WinRegistry.KEY_WOW64_32KEY, javaExecs);
        javaExecs = JavaVersionFinder.searchRegistry("SOFTWARE\\JavaSoft\\Java Development Kit",     WinRegistry.KEY_WOW64_64KEY, javaExecs);

        javaExecs.add(System.getenv("WINDIR") + "\\system32\\java.exe");
        javaExecs.add(System.getenv("WINDIR") + "\\SysWOW64\\java.exe");

        List<JavaInfo> result = new ArrayList<JavaInfo>();
        for (String javaPath: javaExecs) {
            if (!(new File(javaPath).exists())) continue;
            result.add(new JavaInfo(javaPath));
        }
        return result;
    }

    /**
     * @return: The path to a java.exe that has the same bitness as the OS
     * (or null if no matching java is found)
     ****************************************************************************/
    public static String getOSBitnessJava() {
        String arch      = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
        boolean isOS64 = arch.endsWith("64") || (wow64Arch != null && wow64Arch.endsWith("64"));

        List<JavaInfo> javas = JavaVersionFinder.findJavas();
        for (int i = 0; i < javas.size(); i++) {
            if (javas.get(i).is64bits == isOS64) return javas.get(i).path;
        }
        return null;
    }

    /**
     * Standalone testing - lists all Javas in the system
     ****************************************************************************/
    public static void main(String [] args) {
        List<JavaInfo> javas = JavaVersionFinder.findJavas();
        for (int i = 0; i < javas.size(); i++) {
            System.out.println("\n" + javas.get(i));
        }
    }
}

/*
 * Pure Java Windows Registry access.
 * Modified by petrucio@stackoverflow(828681) to add support for
 * reading (and writing but not creating/deleting keys) the 32-bits
 * registry view from a 64-bits JVM (KEY_WOW64_32KEY)
 * and 64-bits view from a 32-bits JVM (KEY_WOW64_64KEY).
 *****************************************************************************/



class WinRegistry {
  public static final int HKEY_CURRENT_USER = 0x80000001;
  public static final int HKEY_LOCAL_MACHINE = 0x80000002;
  public static final int REG_SUCCESS = 0;
  public static final int REG_NOTFOUND = 2;
  public static final int REG_ACCESSDENIED = 5;

  public static final int KEY_WOW64_32KEY = 0x0200;
  public static final int KEY_WOW64_64KEY = 0x0100;

  private static final int KEY_ALL_ACCESS = 0xf003f;
  private static final int KEY_READ = 0x20019;
  private static Preferences userRoot = Preferences.userRoot();
  private static Preferences systemRoot = Preferences.systemRoot();
  private static Class<? extends Preferences> userClass = userRoot.getClass();
  private static Method regOpenKey = null;
  private static Method regCloseKey = null;
  private static Method regQueryValueEx = null;
  private static Method regEnumValue = null;
  private static Method regQueryInfoKey = null;
  private static Method regEnumKeyEx = null;
  private static Method regCreateKeyEx = null;
  private static Method regSetValueEx = null;
  private static Method regDeleteKey = null;
  private static Method regDeleteValue = null;

  static {
    try {
      regOpenKey     = userClass.getDeclaredMethod("WindowsRegOpenKey",     new Class[] { int.class, byte[].class, int.class });
      regOpenKey.setAccessible(true);
      regCloseKey    = userClass.getDeclaredMethod("WindowsRegCloseKey",    new Class[] { int.class });
      regCloseKey.setAccessible(true);
      regQueryValueEx= userClass.getDeclaredMethod("WindowsRegQueryValueEx",new Class[] { int.class, byte[].class });
      regQueryValueEx.setAccessible(true);
      regEnumValue   = userClass.getDeclaredMethod("WindowsRegEnumValue",   new Class[] { int.class, int.class, int.class });
      regEnumValue.setAccessible(true);
      regQueryInfoKey=userClass.getDeclaredMethod("WindowsRegQueryInfoKey1",new Class[] { int.class });
      regQueryInfoKey.setAccessible(true);
      regEnumKeyEx   = userClass.getDeclaredMethod("WindowsRegEnumKeyEx",   new Class[] { int.class, int.class, int.class });  
      regEnumKeyEx.setAccessible(true);
      regCreateKeyEx = userClass.getDeclaredMethod("WindowsRegCreateKeyEx", new Class[] { int.class, byte[].class });
      regCreateKeyEx.setAccessible(true);  
      regSetValueEx  = userClass.getDeclaredMethod("WindowsRegSetValueEx",  new Class[] { int.class, byte[].class, byte[].class });  
      regSetValueEx.setAccessible(true); 
      regDeleteValue = userClass.getDeclaredMethod("WindowsRegDeleteValue", new Class[] { int.class, byte[].class });  
      regDeleteValue.setAccessible(true); 
      regDeleteKey   = userClass.getDeclaredMethod("WindowsRegDeleteKey",   new Class[] { int.class, byte[].class });  
      regDeleteKey.setAccessible(true); 
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private WinRegistry() {  }

  /**
   * Read a value from key and value name
   * @param hkey   HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
   * @param key
   * @param valueName
   * @param wow64  0 for standard registry access (32-bits for 32-bit app, 64-bits for 64-bits app)
   *               or KEY_WOW64_32KEY to force access to 32-bit registry view,
   *               or KEY_WOW64_64KEY to force access to 64-bit registry view
   * @return the value
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static String readString(int hkey, String key, String valueName, int wow64) 
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException 
  {
    if (hkey == HKEY_LOCAL_MACHINE) {
      return readString(systemRoot, hkey, key, valueName, wow64);
    }
    else if (hkey == HKEY_CURRENT_USER) {
      return readString(userRoot, hkey, key, valueName, wow64);
    }
    else {
      throw new IllegalArgumentException("hkey=" + hkey);
    }
  }

  /**
   * Read value(s) and value name(s) form given key 
   * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
   * @param key
   * @param wow64  0 for standard registry access (32-bits for 32-bit app, 64-bits for 64-bits app)
   *               or KEY_WOW64_32KEY to force access to 32-bit registry view,
   *               or KEY_WOW64_64KEY to force access to 64-bit registry view
   * @return the value name(s) plus the value(s)
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static Map<String, String> readStringValues(int hkey, String key, int wow64) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    if (hkey == HKEY_LOCAL_MACHINE) {
      return readStringValues(systemRoot, hkey, key, wow64);
    }
    else if (hkey == HKEY_CURRENT_USER) {
      return readStringValues(userRoot, hkey, key, wow64);
    }
    else {
      throw new IllegalArgumentException("hkey=" + hkey);
    }
  }

  /**
   * Read the value name(s) from a given key
   * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
   * @param key
   * @param wow64  0 for standard registry access (32-bits for 32-bit app, 64-bits for 64-bits app)
   *               or KEY_WOW64_32KEY to force access to 32-bit registry view,
   *               or KEY_WOW64_64KEY to force access to 64-bit registry view
   * @return the value name(s)
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static List<String> readStringSubKeys(int hkey, String key, int wow64) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    if (hkey == HKEY_LOCAL_MACHINE) {
      return readStringSubKeys(systemRoot, hkey, key, wow64);
    }
    else if (hkey == HKEY_CURRENT_USER) {
      return readStringSubKeys(userRoot, hkey, key, wow64);
    }
    else {
      throw new IllegalArgumentException("hkey=" + hkey);
    }
  }

  /**
   * Create a key
   * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
   * @param key
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static void createKey(int hkey, String key) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    int [] ret;
    if (hkey == HKEY_LOCAL_MACHINE) {
      ret = createKey(systemRoot, hkey, key);
      regCloseKey.invoke(systemRoot, new Object[] { new Integer(ret[0]) });
    }
    else if (hkey == HKEY_CURRENT_USER) {
      ret = createKey(userRoot, hkey, key);
      regCloseKey.invoke(userRoot, new Object[] { new Integer(ret[0]) });
    }
    else {
      throw new IllegalArgumentException("hkey=" + hkey);
    }
    if (ret[1] != REG_SUCCESS) {
      throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
    }
  }

  /**
   * Write a value in a given key/value name
   * @param hkey
   * @param key
   * @param valueName
   * @param value
   * @param wow64  0 for standard registry access (32-bits for 32-bit app, 64-bits for 64-bits app)
   *               or KEY_WOW64_32KEY to force access to 32-bit registry view,
   *               or KEY_WOW64_64KEY to force access to 64-bit registry view
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static void writeStringValue
    (int hkey, String key, String valueName, String value, int wow64) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    if (hkey == HKEY_LOCAL_MACHINE) {
      writeStringValue(systemRoot, hkey, key, valueName, value, wow64);
    }
    else if (hkey == HKEY_CURRENT_USER) {
      writeStringValue(userRoot, hkey, key, valueName, value, wow64);
    }
    else {
      throw new IllegalArgumentException("hkey=" + hkey);
    }
  }

  /**
   * Delete a given key
   * @param hkey
   * @param key
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static void deleteKey(int hkey, String key) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    int rc = -1;
    if (hkey == HKEY_LOCAL_MACHINE) {
      rc = deleteKey(systemRoot, hkey, key);
    }
    else if (hkey == HKEY_CURRENT_USER) {
      rc = deleteKey(userRoot, hkey, key);
    }
    if (rc != REG_SUCCESS) {
      throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
    }
  }

  /**
   * delete a value from a given key/value name
   * @param hkey
   * @param key
   * @param value
   * @param wow64  0 for standard registry access (32-bits for 32-bit app, 64-bits for 64-bits app)
   *               or KEY_WOW64_32KEY to force access to 32-bit registry view,
   *               or KEY_WOW64_64KEY to force access to 64-bit registry view
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static void deleteValue(int hkey, String key, String value, int wow64) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    int rc = -1;
    if (hkey == HKEY_LOCAL_MACHINE) {
      rc = deleteValue(systemRoot, hkey, key, value, wow64);
    }
    else if (hkey == HKEY_CURRENT_USER) {
      rc = deleteValue(userRoot, hkey, key, value, wow64);
    }
    if (rc != REG_SUCCESS) {
      throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
    }
  }

  //========================================================================
  private static int deleteValue(Preferences root, int hkey, String key, String value, int wow64)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
        new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS | wow64)
    });
    if (handles[1] != REG_SUCCESS) {
      return handles[1];  // can be REG_NOTFOUND, REG_ACCESSDENIED
    }
    int rc =((Integer) regDeleteValue.invoke(root, new Object[] { 
          new Integer(handles[0]), toCstr(value) 
          })).intValue();
    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    return rc;
  }

  //========================================================================
  private static int deleteKey(Preferences root, int hkey, String key) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    int rc =((Integer) regDeleteKey.invoke(root, new Object[] {
        new Integer(hkey), toCstr(key)
    })).intValue();
    return rc;  // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
  }

  //========================================================================
  private static String readString(Preferences root, int hkey, String key, String value, int wow64)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
        new Integer(hkey), toCstr(key), new Integer(KEY_READ | wow64)
    });
    if (handles[1] != REG_SUCCESS) {
      return null; 
    }
    byte[] valb = (byte[]) regQueryValueEx.invoke(root, new Object[] {
        new Integer(handles[0]), toCstr(value)
    });
    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    return (valb != null ? new String(valb).trim() : null);
  }

  //========================================================================
  private static Map<String,String> readStringValues(Preferences root, int hkey, String key, int wow64)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    HashMap<String, String> results = new HashMap<String,String>();
    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
        new Integer(hkey), toCstr(key), new Integer(KEY_READ | wow64)
    });
    if (handles[1] != REG_SUCCESS) {
      return null;
    }
    int[] info = (int[]) regQueryInfoKey.invoke(root, new Object[] {
        new Integer(handles[0])
    });

    int count  = info[2]; // count  
    int maxlen = info[3]; // value length max
    for(int index=0; index<count; index++)  {
      byte[] name = (byte[]) regEnumValue.invoke(root, new Object[] {
          new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1)
      });
      String value = readString(hkey, key, new String(name), wow64);
      results.put(new String(name).trim(), value);
    }
    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    return results;
  }

  //========================================================================
  private static List<String> readStringSubKeys(Preferences root, int hkey, String key, int wow64)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    List<String> results = new ArrayList<String>();
    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
        new Integer(hkey), toCstr(key), new Integer(KEY_READ | wow64) 
        });
    if (handles[1] != REG_SUCCESS) {
      return null;
    }
    int[] info = (int[]) regQueryInfoKey.invoke(root, new Object[] {
        new Integer(handles[0])
    });

    int count  = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj, confirmed by Petrucio
    int maxlen = info[3]; // value length max
    for(int index=0; index<count; index++)  {
      byte[] name = (byte[]) regEnumKeyEx.invoke(root, new Object[] {
          new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1)
          });
      results.add(new String(name).trim());
    }
    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    return results;
  }

  //========================================================================
  private static int [] createKey(Preferences root, int hkey, String key)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    return (int[]) regCreateKeyEx.invoke(root, new Object[] {
      new Integer(hkey), toCstr(key)
    });
  }

  //========================================================================
  private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value, int wow64)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
  {
    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
        new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS | wow64)
    });
    regSetValueEx.invoke(root, new Object[] { 
          new Integer(handles[0]), toCstr(valueName), toCstr(value) 
          }); 
    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
  }

  //========================================================================
  // utility
  private static byte[] toCstr(String str) {
    byte[] result = new byte[str.length() + 1];

    for (int i = 0; i < str.length(); i++) {
      result[i] = (byte) str.charAt(i);
    }
    result[str.length()] = 0;
    return result;
  }
}
