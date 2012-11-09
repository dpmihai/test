package dock;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.graphics.ReflectionRenderer;
import dock.shapes.RoundShape;

public class DockBar extends JPanel {

	public final static int ICON_SIZE = 45;
	public final static int AFFECTED = 4;
	public final static int MOUSE_OUT = 1;

	private final Color black = new Color(0f, 0f, 0.45f, 0.7f);
	private final Color gray = new Color(1f, 1f, 1f, 0.5f);
	private final Color colors[] = new Color[] { black, gray, black };

	private BufferedImage cache;
	private LinkedList<IconOnBar> iconsOnBar;
	private Glass glass;

    public DockBar() {
		iconsOnBar = new LinkedList<IconOnBar>();
		//setOpaque(false);
		addMouseListener(new LauncherMouseListener());
	}

	public void setFrameParent() {
		JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class,	this);
		glass = new Glass(this);
		frame.setGlassPane(glass);
	}

	@Override
	public Dimension getPreferredSize() {
		int w = 15 + iconsOnBar.size() * (ICON_SIZE + 15);
		return new Dimension(w, 50);
	}

	public BufferedImage paintBackground(int width, DockBar dockBar, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if (cache == null || cache.getWidth() != width) {
			BufferedImage gradientImage = new BufferedImage(width, 2, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = gradientImage.createGraphics();
			Point2D start = new Point2D.Double(15, 0);
			Point2D end = new Point2D.Double(width - 15, 0);
			float fractions[] = new float[] { 0f, 0.5f, 1 };
			LinearGradientPaint paint = new LinearGradientPaint(start, end,	fractions, colors);
			g2d.setPaint(paint);
			g2d.fillRect(0, 0, width, 2);
			g2d.dispose();
			Polygon polygon = new Polygon(
					new int[] { 15, width - 15, width, 0 }, new int[] {
							(dockBar.getHeight() / 3),
							(dockBar.getHeight() / 3), dockBar.getHeight(),
							dockBar.getHeight() }, 4);
			Shape rpolygon = new RoundShape(polygon, 5);
			GraphicsConfiguration gc = g2.getDeviceConfiguration();
			cache = gc.createCompatibleImage(width, dockBar.getHeight(), Transparency.TRANSLUCENT);
			Graphics2D g22 = cache.createGraphics();

			g22.setComposite(AlphaComposite.Clear);
			g22.fillRect(0, 0, width, dockBar.getHeight());
			g22.setComposite(AlphaComposite.Src);
			g22.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g22.setColor(Color.WHITE);
			g22.fill(rpolygon);

			g22.setComposite(AlphaComposite.SrcAtop);
			g22.drawImage(gradientImage, 0, 0, width, dockBar.getHeight(),	null);
			gradientImage.flush();
			g22.dispose();
		}
		return cache;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (!glass.isVisible()) {
			Graphics2D g2 = (Graphics2D) g;
			BufferedImage background = paintBackground(getSize().width, this, g2);
			g2.drawImage(background, 0, 0, null);
			int x = 15;
			int y = 0;

			for (IconOnBar iconOnBar : iconsOnBar) {
				ReflectionRenderer renderer = new ReflectionRenderer();
				int originalH = iconOnBar.getIcon().getHeight();
				BufferedImage mirror = renderer.appendReflection(iconOnBar.getIcon());
				int processedH = mirror.getHeight();
				int newH = (processedH * ICON_SIZE) / originalH;
                g.drawImage(mirror, x, y + getHeight() - ICON_SIZE - 6,	ICON_SIZE, newH, null);
				mirror.flush();
				x += ICON_SIZE + 15;
			}
		}
	}

	public void addApplication(String label, BufferedImage icon, ActionListener actionListener) {
		iconsOnBar.add(new IconOnBar(label, icon, actionListener));
	}

	private class IconOnBar {

        private String label;
		private BufferedImage icon;
		private BufferedImage mirror;
		private int mouseIs = MOUSE_OUT;
		private int mouseWas = MOUSE_OUT;
		private ActionListener actionListener;

		public IconOnBar(String label, BufferedImage icon, ActionListener actionListener) {
			this.label = label;
			this.icon = icon;
			ReflectionRenderer renderer = new ReflectionRenderer();
			this.mirror = renderer.createReflection(icon);			
			this.actionListener = actionListener;
			setOpaque(false);
		}

		public BufferedImage getIcon() {
			return icon;
		}
		
		public BufferedImage getMirror() {
			return mirror;
		}
		
		private int lastH;
		private BufferedImage resizedIcon;
		
		public BufferedImage getResizedIcon() {			
			int h = calculateCurrentSize();
            if (lastH!=h) {
				resizedIcon = GraphicsUtilities.createCompatibleTranslucentImage(h,(int)(h*1.3));
				Graphics g = resizedIcon.createGraphics();
				g.drawImage(getIcon(), 0, 0, h, h, null);
				g.drawImage(mirror, 0, h, h, (int)(h*0.3), null);
				g.dispose();
			}
			lastH = h;
			return resizedIcon;
		}

		public String getLabel() {
			return label;
		}

		public ActionListener getActionListener() {
			return actionListener;
		}
		
		public void setMouseLocation(int location) {
			mouseWas = mouseIs;
			mouseIs = location;
		}

		public int getMouseLocation() {
			return mouseIs;
		}

		public int calculateCurrentSize() {
//            return (int) (ICON_SIZE*((mouseWas ) + ((mouseIs ) - (mouseWas))
//					* glass.getProgress()));
            int size = (int) (ICON_SIZE*( ((mouseWas ) + ((mouseIs ) - (mouseWas))
					* glass.getProgress())/2) );
            if (size < ICON_SIZE) {
                size = ICON_SIZE;
            }
            return size;
        }
	}

	private class LauncherMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseEntered(MouseEvent e) {
			if (glass != null)
				glass.setVisible(true);
		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public class Glass extends JPanel {

		private DockBar dockBar;
		private float progress;
		private float clickProgress;

        private GlassMouseMotionListener gmmListener;
        private GlassMouseListener gmListener;

        private Animator animator;

        public Glass(DockBar dockBar) {
			this.dockBar = dockBar;
			setOpaque(false);
//			addMouseMotionListener(new GlassMouseMotionListener(this));
//			addMouseListener(new GlassMouseListener(this));
            registerListeners();
        }

        public synchronized void registerListeners() {
            unregisterListeners(); // take care to have only those two listeners
            gmmListener = new GlassMouseMotionListener(this);
            addMouseMotionListener(gmmListener);
            gmListener = new GlassMouseListener(this);
            addMouseListener(gmListener);
        }

        public void unregisterListeners() {
            removeMouseMotionListener(gmmListener);
            removeMouseListener(gmListener);
        }

        public DockBar getDockBar() {
			return dockBar;
		}

        public void setAnimator(Animator animator) {
            this.animator = animator;
        }

        public void setProgress(float progress) {
			this.progress = progress;
			Point p = dockBar.getLocationOnScreen();
			SwingUtilities.convertPointFromScreen(p, getRootPane().getLayeredPane());
			int y = (int) (p.y - 20 * clickProgress);
			repaint(0,y-200,getSize().width,getSize().height-y+200);
			//repaint();
		}

		public void setClickProgress(float clickProgress) {
			this.clickProgress = clickProgress;				
			Point p = dockBar.getLocationOnScreen();
			SwingUtilities.convertPointFromScreen(p, getRootPane().getLayeredPane());
			int y = (int) (p.y - 20 * clickProgress);

			int currentWidth = 0;

			for (IconOnBar iconOnBar : iconsOnBar) {
				currentWidth += iconOnBar.calculateCurrentSize() + 15;
			}
			int x = (getWidth() - currentWidth) / 2;
			for (IconOnBar iconOnBar : iconsOnBar) {				
				int h = iconOnBar.calculateCurrentSize();								
				if (iconOnBar.getMouseLocation() == AFFECTED) {
					break;
				}
				x += h + 15;
			}
			int x1 = (int) (x - 5 * clickProgress);
			repaint(x1,y-400,220,getSize().height-y+400);
		}

		@Override
		public void paintComponent(Graphics g) {            
            Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			int currentWidth = 0;
			for (IconOnBar iconOnBar : iconsOnBar) {
				currentWidth += iconOnBar.calculateCurrentSize() + 15;
			}
			int x = (getWidth() - currentWidth) / 2;

			BufferedImage background = paintBackground(currentWidth, dockBar, g);
			Point p = dockBar.getLocationOnScreen();
			SwingUtilities.convertPointFromScreen(p, getRootPane().getLayeredPane());

			g.drawImage(background, x, p.y, currentWidth, dockBar.getHeight(),	null);

			ReflectionRenderer renderer = new ReflectionRenderer();
			for (IconOnBar iconOnBar : iconsOnBar) {
				int y = p.y;
				int x1 = x;
				int h = iconOnBar.calculateCurrentSize();
				int h1 = h;
				BufferedImage mirror;
				if (iconOnBar.getMouseLocation() == AFFECTED) {
					y = (int) (y - 20 * clickProgress);
					x1 = (int) (x - 5 * clickProgress);
					h1 += (int) ((h1 * clickProgress) / 20);
					int fontHeight = 14; //(h1 * 8 / 25);
					g.setFont(new Font("Arial", Font.BOLD, fontHeight));
					int labelWidth = g.getFontMetrics().stringWidth(iconOnBar.getLabel());
					int labelX = x1 + (h1 -labelWidth)/2 + 5;
					labelX = Math.max(15,labelX);
					labelX = Math.min(labelX, getWidth()-15-labelWidth);
					g.setClip(0,0,getSize().width,getSize().height);					
					int labelY = p.y + fontHeight + 20;
					g.setColor(Color.BLACK);
					g.drawString(iconOnBar.getLabel(), labelX+1, labelY+1);					
					g.drawString(iconOnBar.getLabel(), labelX-1, labelY-1);					
					g.drawString(iconOnBar.getLabel(), labelX-1, labelY+1);										
					g.drawString(iconOnBar.getLabel(), labelX+1, labelY);					
					g.drawString(iconOnBar.getLabel(), labelX, labelY-1);
					g.drawString(iconOnBar.getLabel(), labelX, labelY+1);
					g.setColor(Color.white);
					g.drawString(iconOnBar.getLabel(), labelX, labelY);					
					
					BufferedImage reflection = iconOnBar.getMirror();
					BufferedImage buffer = GraphicsUtilities
							.createCompatibleTranslucentImage(reflection
									.getWidth(), iconOnBar.getIcon()
									.getHeight()
									+ reflection.getHeight());
					Graphics2D g2 = buffer.createGraphics();

					int effectiveRadius = renderer.getEffectiveBlurRadius();

					g2.drawImage(reflection, 0, (int) (iconOnBar.getIcon().getHeight()
							- effectiveRadius + 30 * clickProgress), null);
                    g2.drawImage(iconOnBar.getIcon(), effectiveRadius, 0, null);
					g2.dispose();
					reflection.flush();
					mirror = buffer;
					int originalH = iconOnBar.getIcon().getHeight();

					int processedH = mirror.getHeight();
					int newH = (processedH * h1) / originalH;
					g.setClip(x1, y - h1, h1, dockBar.getHeight() + h1 + 7);
					g.drawImage(mirror, x1, y + dockBar.getHeight() - h1 - 20, h1, newH, null);
				} else {
					mirror = iconOnBar.getResizedIcon(); //renderer.appendReflection(iconOnBar.getResizedIcon());
					g.setClip(x1, y - h1, h1, dockBar.getHeight() + h1 + 7);
					g.drawImage(mirror, x1, y + dockBar.getHeight() - h1 - 20, null);
				}
				mirror.flush();
				x += h + 15;
			}

            if ((animator != null) && !animator.isRunning()) {
                registerListeners();
            }
        }

		public float getProgress() {
			return progress;
		}
	}

	private class GlassMouseListener implements MouseListener {

		private Glass glass;

		Animator animator;

		public GlassMouseListener(Glass glass) {
			this.glass = glass;

		}

		public void mouseClicked(MouseEvent e) {
			for (IconOnBar iconOnBar : iconsOnBar) {
				if (iconOnBar.getMouseLocation() == AFFECTED) {
					if (animator != null && animator.isRunning()) {
                        animator.stop();
                    }

                    // only one click is possible at a time
                    // when animation ends the listeners are registered
                    glass.unregisterListeners();

                    animator = PropertySetter.createAnimator(400, glass, "clickProgress", 0f, 3f);
					animator.addTarget(new TimingTarget() {

						public void begin() {
							// TODO Auto-generated method stub
						}

						public void end() {							
							for (int i = 0; i < iconsOnBar.size(); ++i) {
								IconOnBar iconOnBar = iconsOnBar.get(i);
								iconOnBar.setMouseLocation(MOUSE_OUT);
							}
							glass.setVisible(false);							
						}

						public void repeat() {
							// TODO Auto-generated method stub
						}

						public void timingEvent(float arg0) {
							// TODO Auto-generated method stub
						}
						
					});
					animator.setRepeatBehavior(RepeatBehavior.REVERSE);
                    //animator.setRepeatCount(6);
                    animator.setRepeatCount(2);
					animator.setDeceleration(0.98f);
                    glass.setAnimator(animator);
                    animator.start();
					ActionListener al = iconOnBar.getActionListener();
					if (al != null) {
						ActionEvent event = new ActionEvent(iconOnBar,0,iconOnBar.getLabel());
						al.actionPerformed(event);
                    }
                    return;
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}

	}

	private class GlassMouseMotionListener implements MouseMotionListener {
		private Glass glass;

		Animator animator;

		public GlassMouseMotionListener(Glass glass) {
			this.glass = glass;

		}

		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		private int lastMouseOver = -1;

		public void mouseMoved(MouseEvent e) {
			int x = 0;
			for (IconOnBar iconOnBar : iconsOnBar) {
				int h = iconOnBar.calculateCurrentSize();
				x += h + 15;
			}

			x = (glass.getWidth() - x) / 2;

			Point p = glass.getDockBar().getLocationOnScreen();
			SwingUtilities.convertPointFromScreen(p, getRootPane().getLayeredPane());

			int y = p.y;

			int i = 0;
			if (y - (ICON_SIZE * AFFECTED) > e.getPoint().y) {
				glass.setVisible(false);
				mouseEntered(9999);
				mouseEntered(9999);
			} else {
				for (IconOnBar iconOnBar : iconsOnBar) {
					int h = iconOnBar.calculateCurrentSize();
					int mousePos = e.getPoint().x;
					if ((x < mousePos) && (x + h > mousePos)) {
						if (lastMouseOver != i) {
							mouseEntered(i);
							lastMouseOver = i;
							return;
						}
					}
					x += h + 15;
					i++;
				}
			}
		}

		public void mouseEntered(int appId) {
			for (int i = 0; i < iconsOnBar.size(); ++i) {
				IconOnBar iconOnBar = iconsOnBar.get(i);
				int diff = Math.abs(appId - i);
				if (diff < AFFECTED - 1)
					iconOnBar.setMouseLocation(AFFECTED - diff);
				else
					iconOnBar.setMouseLocation(MOUSE_OUT);
			}
			if (animator != null && animator.isRunning())
				animator.stop();
			animator = PropertySetter.createAnimator(400, glass, "progress", 0f, 1f);
			animator.setAcceleration(0.3f);
			animator.setDeceleration(0.2f);
			animator.start();
		}
	}

}
