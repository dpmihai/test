package trafficlight;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;

public class TrafficTest {
	
	public static void main(String[] args) {
		
		TrafficLight tl = new TrafficLight();
		tl.setPreferredSize(new Dimension(100, 300));
		
		tl.setRedOn(true);
		
		JFrame frame = new JFrame("Traffic Light");
        Container contents = frame.getContentPane();
        contents.setLayout(new FlowLayout());
        contents.add(tl);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

}
