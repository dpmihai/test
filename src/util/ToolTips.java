package util;
//
// Created by IntelliJ IDEA.
// User: mihai.panaitescu
// Date: 26-Jun-2009
// Time: 10:32:41

//


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class ToolTips extends JPanel
{
    List<Rectangle> rectangles;
    List<String> tips;
    Rectangle liveDrag;
    boolean dragInProgress;
    JWindow toolTip;
    JLabel label;
    final double MINIMUM_DIAGONAL = 30.0;

    public ToolTips(Frame f)
    {
        rectangles = new ArrayList<Rectangle>();
        tips = new ArrayList<String>();
        rectangles.add(new Rectangle(100,100,65,40));
        tips.add("rectangle 1");
        liveDrag = new Rectangle();
        dragInProgress = false;
        // initialize toolTip
        toolTip = new JWindow(f);
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(UIManager.getColor("ToolTip.background"));
        label.setBorder(UIManager.getBorder("ToolTip.border"));
        toolTip.add(label);
        setOpaque(true);
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        for(Rectangle r : rectangles)
            g2.draw(r);
        if(dragInProgress)
        {
            g2.setPaint(Color.red);
            g2.draw(liveDrag);
        }
    }

    public void showToolTip(int index, Point p)
    {
        label.setText(tips.get(index));
        toolTip.pack();
        toolTip.setLocation(p);
        toolTip.setVisible(true);
    }

    public void hideToolTip()
    {
        toolTip.dispose();
    }

    public void setLiveDrag(Point p1, Point p2)
    {
        if(!dragInProgress)
            dragInProgress = true;
        liveDrag.setFrameFromDiagonal(p1, p2);
        repaint();
    }

    public void addRectangle(double distance)
    {
        dragInProgress = false;
        if(distance >= MINIMUM_DIAGONAL)
        {
            rectangles.add((Rectangle)liveDrag.clone());
            tips.add("rectangle " + (tips.size()+1));
        }
        repaint();
    }

    public void clear()
    {
        rectangles.clear();
        tips.clear();
        dragInProgress = false;
        repaint();
    }

    public boolean isToolTipShowing()
    {
        return toolTip.isShowing();
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(500,500);
    }

    public static void main(String[] args)
    {
        JFrame f = new JFrame("click and drag");
        ToolTips toolTips = new ToolTips(f);
        Tipster tipster = new Tipster(toolTips);
        toolTips.addMouseListener(tipster);
        toolTips.addMouseMotionListener(tipster);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new JScrollPane(toolTips));
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}

class Tipster extends MouseInputAdapter
{
    ToolTips toolTips;
    Point start;

    public Tipster(ToolTips tt)
    {
        toolTips = tt;
    }

    public void mousePressed(MouseEvent e)
    {
        start = e.getPoint();
        if(e.getClickCount() == 2)
            toolTips.clear();
    }

    public void mouseReleased(MouseEvent e)
    {
        Point p = e.getPoint();
        double diagDistance = start.distance(p);
        toolTips.addRectangle(diagDistance);
    }

    public void mouseDragged(MouseEvent e)
    {
        Point end = e.getPoint();
        toolTips.setLiveDrag(start, end);
    }

    public void mouseMoved(MouseEvent e)
    {
        Point p = e.getPoint();
        boolean traversing = false;
        List<Rectangle> rects = toolTips.rectangles;
        for(int j = 0; j < rects.size(); j++)
        {
            Rectangle r = rects.get(j);
            if(r.contains(p))
            {
                if(!toolTips.isToolTipShowing())
                {
                    SwingUtilities.convertPointToScreen(p, toolTips);
                    toolTips.showToolTip(j, p);
                }
                traversing = true;
                break;
            }
        }
        if(!traversing && toolTips.isToolTipShowing())
            toolTips.hideToolTip();
    }
}

