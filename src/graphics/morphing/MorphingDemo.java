package graphics.morphing;

/*
 * Copyright (c) 2007, Romain Guy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the TimingFramework project nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.MouseTrigger;
import org.jdesktop.animation.timing.triggers.MouseTriggerEvent;
/**
 *
 * @author Romain Guy <romain.guy@mac.com>
 */
public class MorphingDemo extends JFrame {
    private ImageViewer imageViewer;

    public MorphingDemo() {
        super("Morphing Demo");
        
        add(buildImageViewer());
        add(buildControls(), BorderLayout.SOUTH);
        
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
        
    private JComponent buildImageViewer() {
        return imageViewer = new ImageViewer();
    }
    
    private JComponent buildControls() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        
        JButton button;
        panel.add(button = new DirectionButton("Backward",
                DirectionButton.Direction.LEFT));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imageViewer.previous();
            }
        });
        
        panel.add(button = new DirectionButton("Forward",
                DirectionButton.Direction.RIGHT));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imageViewer.next();
            }
        });
        
        return panel;
    }
    
    
    public static class ImageViewer extends JComponent {
        private BufferedImage firstImage;
        private BufferedImage secondImage;
        
        private float alpha = 0.0f;
        
        private ImageViewer() {
            try {
                firstImage = GraphicsUtilities.loadCompatibleImage(
                    getClass().getResource("images/suzhou.jpg"));
                secondImage = GraphicsUtilities.loadCompatibleImage(
                    getClass().getResource("images/shanghai.jpg"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(firstImage.getWidth(), firstImage.getHeight());
        }
        
        public void next() {
            Animator animator = new Animator(500);
            animator.addTarget(new PropertySetter(this, "alpha", 1.0f));
            animator.setAcceleration(0.2f);
            animator.setDeceleration(0.4f);
            animator.start();
        }
        
        public void previous() {
            Animator animator = new Animator(500);
            animator.addTarget(new PropertySetter(this, "alpha", 0.0f));
            animator.setAcceleration(0.2f);
            animator.setDeceleration(0.4f);
            animator.start();
        }
        
        public void setAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }
        
        public float getAlpha() {
            return this.alpha;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            
            g2.setComposite(AlphaComposite.SrcOver.derive(1.0f - alpha));
            g2.drawImage(firstImage, 0, 0, null);
            g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g2.drawImage(secondImage, 0, 0, null);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
                new MorphingDemo().setVisible(true);
            }
        });
    }
}
