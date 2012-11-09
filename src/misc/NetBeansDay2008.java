package misc;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 15, 2008
 * Time: 10:13:16 AM
 */

import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class NetBeansDay2008 {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        final JEditorPane editorPane = new JEditorPane();
        editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        editorPane.setFont(new Font("Arial", Font.PLAIN, 20));
        editorPane.setPreferredSize(new Dimension(520, 180));
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText(
                "<html>"
                        + "<body>"
                        + "<table border='0px' cxellpadding='10px' height='100%'>"
                        + "<tr>"
                        + "<td valign='center'>"
                        + "<img src='"
                        //+ "http://www.netbeans.org/images/javaone/2008/nbday08_logo.gif"
                        // Alternatively an image stored next to the NetBeansDay2008
                        // class can be used like this.
                        + NetBeansDay2008.class.getResource("nbday08_logo.gif").toExternalForm()
                        + "'>"
                        + "</td>"
                        + "<td>"
                        + "<a href=\"http://www.netbeans.org/community/articles/javaone/2008/nb-day.html\"><b>NetBeans Day</b></a>"
                        + "<br>San Francisco May 5th, 2008"
                        + "NetBeans - The only IDE you need!"
                        + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "</body>"
                        + "</html>"
        );

        // TIP: Make the JOptionPane resizable using the HierarchyListener
        editorPane.addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent e) {
                Window window = SwingUtilities.getWindowAncestor(editorPane);
                if (window instanceof Dialog) {
                    Dialog dialog = (Dialog) window;
                    if (!dialog.isResizable()) {
                        dialog.setResizable(true);
                    }
                }
            }
        });

        // TIP: Add Hyperlink listener to process hyperlinks
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            // TIP: Show hand cursor
                            SwingUtilities.getWindowAncestor(editorPane).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            // TIP: Show URL as the tooltip
                            editorPane.setToolTipText(e.getURL().toExternalForm());
                        }
                    });
                } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            // Show default cursor
                            SwingUtilities.getWindowAncestor(editorPane).setCursor(Cursor.getDefaultCursor());

                            // Reset tooltip
                            editorPane.setToolTipText(null);
                        }
                    });
                } else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    // TIP: Starting with JDK6 you can show the URL in desktop browser
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (Exception ex) {
                            Logger.getLogger(NetBeansDay2008.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                    //System.out.println("Go to URL: " + e.getURL());
                }
            }
        });
        JOptionPane.showMessageDialog(null,
                new JScrollPane(editorPane),
                "NetBeans Day 2008",
                JOptionPane.PLAIN_MESSAGE);
    }
}