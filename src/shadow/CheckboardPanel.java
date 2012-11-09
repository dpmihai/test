package shadow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CheckboardPanel extends JPanel {
    protected int tileSize = 7;
    protected Color darkColor = new Color(204, 204, 204);
    private BufferedImage buffer;

    public CheckboardPanel() {
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension superSize = super.getPreferredSize();
        superSize.setSize(superSize.width + 50,
                          superSize.height + 50);
        return superSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (buffer == null ||
            getWidth() != buffer.getWidth() ||
            getHeight() != buffer.getHeight()) {
            createCheckboard();
        }

        Rectangle bounds = g.getClipBounds();
        g.drawImage(buffer.getSubimage(bounds.x, bounds.y,
                                       bounds.width, bounds.height),
                                       bounds.x, bounds.y,
                                       null);
    }

    private void createCheckboard() {
        int width = getWidth();
        int height = getHeight();
        buffer = new BufferedImage(width,
                                   height,
                                   BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffer.createGraphics();
        
        int boundsStart = 0;
        int boundsEnd = boundsStart + width;
        int startX = boundsStart - (boundsStart % tileSize);
        int endX = boundsEnd + (boundsEnd % tileSize);

        boundsStart = 0;
        boundsEnd = boundsStart + height;
        int startY = boundsStart - (boundsStart % tileSize);
        int endY = boundsEnd + (boundsEnd % tileSize);

        for (int x = startX; x < endX; x += tileSize) {
            int xdark = x % (tileSize << 1) == 0 ? tileSize : 0;
            for (int y = startY; y < endY; y += tileSize) {
                boolean dark = ((y + xdark) % (tileSize << 1)) == 0;
                g.setColor(dark ? darkColor : Color.WHITE);
                g.fillRect(x, y, tileSize, tileSize);
            }
        }
        
        g.dispose();
    }
}
