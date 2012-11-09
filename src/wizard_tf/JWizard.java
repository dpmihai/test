package wizard_tf;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 24, 2008
 * Time: 11:43:35 AM
 */
public class JWizard extends JPanel {

    private int tab;						// Current tab
	private int tabSize;					// Size of tab

    private WButtonPanel buttonPanel;
    private WMainPanel mainPanel;

    public JWizard(List<WPanel> panels) {

        if ((panels == null) || (panels.size() <= 1)) {
            throw new IllegalArgumentException("Wizard must contain at least two panels.");
        }

        tab = 0;
        tabSize = panels.size();

        mainPanel = new WMainPanel((WPanel[])panels.toArray(new WPanel[tabSize]));


        buttonPanel = new WButtonPanel() {

            protected void backward() {
                  mainPanel.updateTab(false);
            }

            protected void forward() {
                 mainPanel.updateTab(true);
            }

            protected void cancel() {
                Window window = SwingUtilities.getWindowAncestor(JWizard.this);
                window.dispose();
            }
        };

        mainPanel.setButtonPanel(buttonPanel);

        this.setLayout(new BorderLayout(10,10));
		//this.add(topBar,BorderLayout.NORTH);
		this.add(mainPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
    }


}
