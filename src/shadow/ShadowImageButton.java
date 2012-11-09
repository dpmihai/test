package shadow;

import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 7, 2006
 * Time: 11:18:06 AM
 */
public class ShadowImageButton extends JButton implements MouseListener  {

    private String imageName;

    public ShadowImageButton(String imageName) {
        super();
        this.imageName = imageName;
        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.clearRect(0, 0, getWidth(), getHeight());
        ImageUtil.drawImage(imageName, true, g2, 10, 10);
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("clicked");
        if (isEnabled()) {
            this.setFocusPainted(true);
        }

    }

    public void mouseEntered(MouseEvent e) {
       if (isEnabled()) {
            this.setBorderPainted(true);
//            this.setFocusPainted(true);
        }
    }

    public void mouseExited(MouseEvent e) {
        this.setBorderPainted(false);
        this.setFocusPainted(false);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
