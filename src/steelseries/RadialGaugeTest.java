package steelseries;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.LedColor;
import eu.hansolo.steelseries.tools.Model;

// http://harmoniccode.blogspot.ro/2010/08/java-swing-component-library.html
public class RadialGaugeTest {

	public static void main(String[] args) {

		Model model = new Model();
		
		model.setFrameDesign(FrameDesign.TILTED_GRAY);
//		model.setFrameDesign(FrameDesign.CUSTOM);
//		model.setCustomFrameDesign(Color.WHITE);
		
		model.setMinValue(0);
		model.setMaxValue(100);		
		
		model.setLedVisible(true);		
		
		model.setThresholdVisible(true);
		model.setThreshold(80);
		model.setThresholdColor(ColorDef.ORANGE);
		
		model.setTrackVisible(true);
		model.setTrackStart(80);
		model.setTrackStop(100);		
		model.setTrackStartColor(Color.ORANGE);
		model.setTrackStopColor(Color.RED.darker().darker());
		model.setTrackSectionColor(Color.RED);
				
		model.setLcdVisible(false);
		//model.setLcdValue(48);
		
		model.setGaugeType(GaugeType.TYPE4);
		model.setHighlightArea(true);			
		model.setBackgroundColor(BackgroundColor.WHITE);
		model.setPointerColor(ColorDef.RAITH);
		
		final Radial radial = new Radial(model);
		radial.setTitle("Speed");
		radial.setUnitString("km/h");					
		radial.setPreferredSize(new Dimension(200,200));		

		JFrame f = new JFrame("Gauge");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(radial);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
		EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {    
            	radial.setStdTimeToValue(1500);
                radial.setValueAnimated(85);		                           
            }
        });

	}
}
