package registry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

public class WinRegistry {
	
	private static final int KEY_ALL_ACCESS = 0xf003f;
	private static final int KEY_READ = 0x20019;
	
	public static final int REG_SUCCESS = 0;
	public static final int REG_NOTFOUND = 2;
	public static final int REG_ACCESSDENIED = 5;
	
	public static final int HKEY_CURRENT_USER = 0x80000001;
	public static final int HKEY_LOCAL_MACHINE = 0x80000002;
	private static final int HKEY_CLASSES_ROOT = 0x80000000;
	private static Preferences systemRoot = Preferences.systemRoot();
	private static Preferences userRoot = Preferences.userRoot();
	private static Class<? extends Preferences> userClass = systemRoot.getClass();
	
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
	      regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey", new Class[] { int.class, byte[].class, int.class });
	      regOpenKey.setAccessible(true);
	      regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { int.class });
	      regCloseKey.setAccessible(true);
	      regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx",  new Class[] { int.class, byte[].class });
	      regQueryValueEx.setAccessible(true);
	      regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue", new Class[] { int.class, int.class, int.class });
	      regEnumValue.setAccessible(true);
	      regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1", new Class[] { int.class });
	      regQueryInfoKey.setAccessible(true);
	      regEnumKeyEx = userClass.getDeclaredMethod("WindowsRegEnumKeyEx", new Class[] { int.class, int.class, int.class });  
	      regEnumKeyEx.setAccessible(true);
	      regCreateKeyEx = userClass.getDeclaredMethod("WindowsRegCreateKeyEx", new Class[] { int.class, byte[].class });  
	      regCreateKeyEx.setAccessible(true);  
	      regSetValueEx = userClass.getDeclaredMethod("WindowsRegSetValueEx", new Class[] { int.class, byte[].class, byte[].class });  
	      regSetValueEx.setAccessible(true); 
	      regDeleteValue = userClass.getDeclaredMethod("WindowsRegDeleteValue", new Class[] { int.class, byte[].class });  
	      regDeleteValue.setAccessible(true); 
	      regDeleteKey = userClass.getDeclaredMethod("WindowsRegDeleteKey", new Class[] { int.class, byte[].class });  
	      regDeleteKey.setAccessible(true); 
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	
	private WinRegistry() {		
	}
	
	public static boolean exists(Preferences root, int hkey, String key, String value) 
			throws IllegalArgumentException,IllegalAccessException, InvocationTargetException {
		
		int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {	new Integer(hkey), toCstr(key), new Integer(KEY_READ) });		
		return handles[1] == REG_SUCCESS; 		
	}
	
	public static String readString(Preferences root, int hkey, String key, String value) 
			throws IllegalArgumentException,IllegalAccessException, InvocationTargetException {
		
		int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {	new Integer(hkey), toCstr(key), new Integer(KEY_READ) });
		System.out.println("**** handle="+handles[1]);
		if (handles[1] != REG_SUCCESS) {
			return null;
		}
		byte[] valb = (byte[]) regQueryValueEx.invoke(root, new Object[] {
				new Integer(handles[0]), toCstr(value) });
		regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
		return (valb != null ? new String(valb).trim() : null);
	}
	
	public static void createKey(int hkey, String key)	
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int[] ret;
		ret = createKey(systemRoot, hkey, key);
		regCloseKey.invoke(systemRoot, new Object[] { new Integer(ret[0]) });
		if (ret[1] != REG_SUCCESS) {
			System.out.println("Could not create key: " + key);
			throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
		}
	}
	
	private static int[] createKey(Preferences root, int hkey, String key)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		return (int[]) regCreateKeyEx.invoke(root, new Object[] { new Integer(hkey), toCstr(key) });
	}
	
	private static int deleteKey(Preferences root, int hkey, String key)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		int rc = ((Integer) regDeleteKey.invoke(root, new Object[] { new Integer(hkey), toCstr(key) })).intValue();
		return rc; // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
	}
	
	private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
    
		int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS) });
		regSetValueEx.invoke(root, new Object[] {new Integer(handles[0]), toCstr(valueName), toCstr(value) }); 
        regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
  }
	
	private static byte[] toCstr(String str) {
		byte[] result = new byte[str.length() + 1];

		for (int i = 0; i < str.length(); i++) {
			result[i] = (byte) str.charAt(i);
		}
		result[str.length()] = 0;
		return result;
	}
	
	public static void main(String[] args) {
		try {			
			
			String val2 = WinRegistry.readString(userRoot, HKEY_CURRENT_USER, "Software\\Classes\\nextreports", "(Default)");
			System.out.println("val2="+val2);
			
//			String val = WinRegistry.readString(systemRoot, HKEY_CLASSES_ROOT, "nextreports", "CustomUrlApplication");
//			System.out.println("val="+val);
//			String val2 = WinRegistry.readString(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\nextreports", "CustomUrlApplication");
//			System.out.println("val2="+val2);
//			boolean val3 = WinRegistry.exists(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node", "(Default)");
//			System.out.println("Wow6432Node exists = "+val3);
			
//			int status = deleteKey(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\nextreports");
//			System.out.println("Wow6432Node delete status="+status);
			
			//createNextReportsUrl();
					
		} catch (Exception e) {		
			e.printStackTrace();
		} 
	}
	
	private static void createNextReportsUrl() throws Exception {
		System.out.println("Create nextreport registry entries");
		createKey(HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports", "(Default)", "URL: Protocol handled by NextReports");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports", "CustomUrlApplication", "D:\\Programs\\NextReports 4.2\\nextreports.exe");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports", "CustomUrlArguments", "");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports", "URL Protocol", "");
		
		createKey(HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\DefaultIcon");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\DefaultIcon", "(Default)", "D:\\Programs\\NextReports 4.2\\nextreports.exe");
		
		createKey(HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\shell");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\shell", "(Default)", "");
		createKey(HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\shell\\\\open");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\shell\\\\open", "(Default)", "");
		createKey(HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\shell\\\\open\\\\command");
		writeStringValue(systemRoot, HKEY_CLASSES_ROOT, "Wow6432Node\\\\nextreports\\\\shell\\\\open\\\\command", "(Default)", "D:\\Programs\\NextReports 4.2\\nextreports.exe");
	}

}
