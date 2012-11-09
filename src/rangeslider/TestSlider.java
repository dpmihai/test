package rangeslider;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;

public class TestSlider {
	
	public static void main(String[] args) {
		
		RangeSlider slider = new RangeSlider(0, 100);
		slider.setPreferredSize(new Dimension(400, 100));
		
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(10);				
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
				
		slider.setValue(10);	
		slider.setUpperValue(73);
		
		slider.setDarkTrack(true);
		slider.setRangeColor(Color.RED);
		
		JFrame frame = new JFrame("Bidirectional slider");
        Container contents = frame.getContentPane();
        contents.setLayout(new FlowLayout());
        contents.add(slider);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

}
