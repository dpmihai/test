package restart;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 8, 2006
 * Time: 11:01:23 AM
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import java.awt.event.*;

public class App{
    public static void main(String args[]){
        Action restartAction = new AbstractAction("Restart"){
            public void actionPerformed(ActionEvent ae){
                try{
                    // run.bat tests this exit code !
                    System.exit(100);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        };

        JFrame frame = new JFrame("App");
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.add(restartAction);
        menubar.add(menu);
        frame.setJMenuBar(menubar);
        frame.setSize(100, 100);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}