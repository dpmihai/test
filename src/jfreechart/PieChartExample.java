package jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.io.File;
import java.util.List;

public class PieChartExample {
    public static void main(String[] args) {
        // Create a simple pie chart
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Boom Server", new Integer(30));
        pieDataset.setValue("Next Reports", new Integer(33));
        pieDataset.setValue("ReMinder", new Integer(37));        
        JFreeChart chart = ChartFactory.createPieChart(
               "Project Hours",
                pieDataset, 
                true, 
                true, 
                false);
        chart.setBackgroundPaint(Color.WHITE);
        
        PiePlot plot = (PiePlot)chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setStartAngle(330);
        
        // Specify the colors here
        Color[] colors = {Color.blue, Color.yellow, Color.green, Color.red, Color.ORANGE};
        PieRenderer renderer = new PieRenderer(colors);
        renderer.setColor(plot, pieDataset);

        
        try {
            ChartUtilities.saveChartAsJPEG(new File("C:chart.jpg"), chart, 500,  300);
        } catch (Exception e) {
            System.out.println("Problem occurred creating chart.");
        }
    }
    
    public static class PieRenderer
    {
        private Color[] color;
       
        public PieRenderer(Color[] color)
        {
            this.color = color;
        }       
       
        public void setColor(PiePlot plot, DefaultPieDataset dataset)
        {
            List <Comparable> keys = dataset.getKeys();
            int aInt;
           
            for (int i = 0; i < keys.size(); i++)
            {
                aInt = i % this.color.length;
                plot.setSectionPaint(keys.get(i), this.color[aInt]);
            }
        }
    }

}
