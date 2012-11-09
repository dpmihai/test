package mouse;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 22, 2005 Time: 9:46:02 AM
 */
public class MouseScrollHandler extends MouseInputAdapter implements ActionListener {
	public static void enableMouseScrolling(JScrollPane pane) {
		MouseScrollHandler handler = new MouseScrollHandler(pane);
		Component view = pane.getViewport().getView();
		view.addMouseListener(handler);
		view.addMouseMotionListener(handler);
	}

	private Timer movingTimer;
	private Point startPoint;
	private Point currentPoint;
	private MouseScrollViewport viewport;
	private Point lastViewPosition;

	protected MouseScrollHandler(JScrollPane scrollPane) {
		this.viewport = new MouseScrollViewport(new MouseScrollIcon());
		viewport.setView(scrollPane.getViewport().getView());
		scrollPane.setViewport(viewport);
		lastViewPosition = viewport.getViewPosition();

		movingTimer = new Timer(100, this);
		movingTimer.setRepeats(true);
	}

	public Point getPoint(MouseEvent e) {
		Point point = e.getPoint();
		Point viewPos = viewport.getViewPosition();
		point.x -= viewPos.x;
		point.y -= viewPos.y;
		return point;
	}

	public void mousePressed(MouseEvent e) {
		if(!movingTimer.isRunning() && SwingUtilities.isMiddleMouseButton(e)) {
			startPoint = currentPoint = getPoint(e);
			movingTimer.start();
		} else {
			// end scrolling if we were in scrolling mode
			startPoint = null;
			movingTimer.stop();
		}
		viewport.setMarkPoint(startPoint);
	}

	public void actionPerformed(ActionEvent e) {
		int scrollAmountY = ((int)currentPoint.getY() - (int)startPoint.getY())/4;
		int scrollAmountX = ((int)currentPoint.getX() - (int)startPoint.getX())/4;
		Point viewPosition = viewport.getViewPosition();

		// we just don't want to scroll by such a small amount
		if(scrollAmountY > 3 || scrollAmountY < -3) {
			viewPosition.y += scrollAmountY;
		}

		if(scrollAmountX > 3 || scrollAmountX < -3) {
			viewPosition.x += scrollAmountX;
		}

		// bound-check
		Dimension viewSize = viewport.getViewSize();
		Rectangle viewRect = viewport.getViewRect();
		viewPosition.x = Math.min(Math.max(0, viewPosition.x), viewSize.width - viewRect.width);
		viewPosition.y = Math.min(Math.max(0, viewPosition.y), viewSize.height - viewRect.height);

		if(lastViewPosition.equals(viewPosition) == false) {
			viewport.setViewPosition(viewPosition);
			lastViewPosition = viewPosition;
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(startPoint != null) {
			currentPoint = getPoint(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		if(startPoint != null) {
			currentPoint = getPoint(e);
		}
	}
}
