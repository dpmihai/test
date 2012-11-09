package mouse;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 22, 2005 Time: 9:47:30 AM
 */
public class MouseScrollIcon implements Icon {
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D gfx = (Graphics2D)g;
			gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			gfx.setColor(Color.darkGray);
			gfx.drawOval(x, y, getIconWidth(), getIconHeight());

			gfx.setColor(Color.black);
			gfx.fillOval(x+(getIconWidth()/2)-3, y+(getIconHeight()/2)-3, 6, 6);

			gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		}

		public int getIconWidth() {
			return 24;
		}

		public int getIconHeight() {
			return 24;
		}
	}
