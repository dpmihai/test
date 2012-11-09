package util;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 20, 2006
 * Time: 1:22:45 PM
 */
public class UIMangerUtil {


    public static void printDefaultProperties() {

        List<String> keys = new ArrayList<String>();
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration en = defaults.keys();
        while (en.hasMoreElements()) {
            Object element = en.nextElement();            
            if (element instanceof String) {
                keys.add((String)element);
            } else if (element instanceof StringBuffer) {
                keys.add( ((StringBuffer)element).toString() );
            } else {
            	System.out.println("class="+element.getClass());
            }
        }
        Collections.sort(keys);

        System.out.println("UIManager Default Properties");
        System.out.println("\n KEY / VALUE list");
        for (String key : keys) {
            System.out.printf(" key: %-60s  val: %-20s\n", key, defaults.get(key));
        }
        System.out.println("\n");
    }

    public static void printDefaultProperties(String startWith) {

        List<String> keys = new ArrayList<String>();
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration en = defaults.keys();
        while (en.hasMoreElements()) {
            Object element = en.nextElement();
            if (element instanceof String) {
                String key = (String) element;
                if (key.startsWith(startWith)) {
                    keys.add(key);
                }
            } else if (element instanceof StringBuffer) {
                keys.add( ((StringBuffer)element).toString() );
            }
        }
        Collections.sort(keys);

        System.out.println("UIManager Default Properties");
        System.out.println("\n KEY / VALUE list");
        for (String key : keys) {
            System.out.printf(" key: %-60s  val: %-20s\n", key, defaults.get(key));
        }
        System.out.println("\n");
    }


    public static void printWindowProperties() {
        String propnames[] = (String[]) Toolkit.getDefaultToolkit().getDesktopProperty("win.propNames");
        System.out.println("Supported windows property names:");
        for (int i = 0; i < propnames.length; i++) {
            System.out.println(propnames[i]);
        }

    }

     public static void main(String[] args) {
        UIMangerUtil.printDefaultProperties();
        //UIMangerUtil.printWindowProperties();

    }


}

