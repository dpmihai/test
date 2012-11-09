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
 * Date: Jul 22, 2005 Time: 9:46:53 AM
 */
public class MouseScrollViewport extends JViewport {
		private Rectangle lastViewRect;

		private Point markPoint;
		private Icon markIcon;

		public MouseScrollViewport(Icon icon) {
			this.markIcon = icon;

			lastViewRect = new Rectangle();
			Point p = getViewPosition();
			lastViewRect.x = Math.max(p.x-10, 0);
			lastViewRect.y = Math.max(p.y-10, 0);
			lastViewRect.width = 20;
			lastViewRect.height = 20;
		}

		public Point getMarkPoint() {
			return markPoint;
		}

		public void setMarkPoint(Point markPoint) {
			this.markPoint = markPoint;
			repaint();
		}

		public Icon getMarkIcon() {
			return markIcon;
		}

		public void setMarkIcon(Icon markIcon) {
			this.markIcon = markIcon;
		}

		public void setViewPosition(Point p) {
			super.setViewPosition(p);

			if(lastViewRect != null && !lastViewRect.contains(p)) {
				lastViewRect.x = Math.max(p.x-10, 0);
				lastViewRect.y = Math.max(p.y-10, 0);

				repaint();
			}
		}

		protected void paintChildren(Graphics g) {
			super.paintChildren(g);

			if(markPoint != null) {
				int x = (int)markPoint.x-(markIcon.getIconWidth()/2);
				int y = (int)markPoint.y-(markIcon.getIconHeight()/2);
				markIcon.paintIcon(this, g, x, y);
			}
		}
	}