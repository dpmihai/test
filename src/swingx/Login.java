package swingx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.prefs.Preferences;
import java.util.List;

import org.jdesktop.swingx.auth.DefaultUserNameStore;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.LoginAdapter;
import org.jdesktop.swingx.auth.LoginEvent;
import org.jdesktop.swingx.JXLoginPane;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 23, 2008
 * Time: 10:33:37 AM
 */
public class Login {

    private Login(){        
    }

    public static void show() {

        configure();

        final LoginService loginService = new SimpleLoginService();
        ServerHandler serverHandler = new ServerHandler();

        DefaultUserNameStore userNames = new DefaultUserNameStore();
        Preferences appPrefs = Preferences.userNodeForPackage(TestLogin.class);
        userNames.setPreferences(appPrefs.node("login"));

        List<Server> servers = serverHandler.getServers();

        //serverHandler.adjustServers(servers, "Test Server");
        ((SimpleLoginService) loginService).setServers(servers);

        List<String> list = serverHandler.getServerNames(servers);
        final JXLoginPane panel = new JXLoginPane(loginService, null, userNames, list);
        //panel.setUserName("mihai");

        panel.setBannerText("Reportz Login");

        // overwrite login failed message
        loginService.addLoginListener(new LoginAdapter() {
            public void loginFailed(LoginEvent event) {
                String defaultMessage = "<html><b>Couldn't log in</b><br><br>Check your user name and password.<br></html>";
                String message = event.getCause() != null ? event.getCause().getMessage() : defaultMessage;
                panel.setErrorMessage(message);
            }
        });

        final JXLoginPane.JXLoginFrame frame = JXLoginPane.showLoginFrame(panel);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (frame.getStatus() == JXLoginPane.Status.SUCCEEDED) {
                    System.out.println("Login Succeeded!");
                } else {
                    System.out.println("Login Failed:" + frame.getStatus());
                }
                System.exit(0);
            }
        });
        
        frame.setVisible(true);

    }

    private static void configure() {        

        // overwrite banner
        Color color = new Color(0, 102, 153);
        UIManager.put("JXLoginPane.bannerDarkBackground", color);
        UIManager.put("JXLoginPane.bannerLightBackground", color.brighter());
        UIManager.put("JXLoginPane.bannerFont", new Font("Arial", Font.ITALIC, 25));
        UIManager.put("JXLoginPane.bannerForeground", Color.WHITE);

        // overwrite login & cancel strings from buttons
        UIManager.put("JXLoginPane.loginString", "Ok");
        UIManager.put("JXLoginPane.cancelString", "Exit");

        // overwrite please wait at login
        UIManager.put("JXLoginPane.pleaseWaitFont", new Font("Arial", Font.ITALIC, 20));
        UIManager.put("JXLoginPane.pleaseWait", "Loging ...");
        UIManager.put("JXLoginPane.cancelLogin", "Cancel");

        // overwrite error icon , message, background & border
        //UIManager.put("JXLoginPane.errorIcon", ImageUtil.getImageIcon("img.png"));
        //UIManager.put("JXLoginPane.errorMessage", "Failed"); // if error message must be inserted see login listener
//            Color errorColor = new Color(252, 236, 236);
//            UIManager.put("JXLoginPane.errorBackground", errorColor);
//            UIManager.put("JXLoginPane.errorBorder", new BorderUIResource(BorderFactory.createCompoundBorder(
//                    BorderFactory.createEmptyBorder(0, 36, 0, 11),
//                    BorderFactory.createCompoundBorder(
//                            BorderFactory.createLineBorder(Color.GRAY.darker()),
//                            BorderFactory.createMatteBorder(5, 7, 5, 5, errorColor)))));

        // overwrite name , password & server labels
        UIManager.put("JXLoginPane.nameString", "User");
        UIManager.put("JXLoginPane.passwordString", "Credentials");
        UIManager.put("JXLoginPane.serverString", "Server");

        //UIManager.put("JXLoginPane.capsOnWarning", "Caps Lock pressed!");

    }
}
