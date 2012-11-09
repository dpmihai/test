package jfreechart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYChartExample {
    public static void main(String[] args) {
        //         Create a simple XY chart
        XYSeries series = new XYSeries("XYGraph");
        series.add(1, 4.0785);
        series.add(1, 4.0812);
        series.add(2, 4.1088);
        series.add(3, 4.0922);
        series.add(4, 4.1234);
        
//        XYSeries series1 = new XYSeries("XYGraph2");
//        series1.add(1, 4);
//        series1.add(2, 3);
//        series1.add(3, 5);
//        series1.add(4, 1);
//        series1.add(6, 3);
//        series1.add(5, 5);
        //         Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();        
        dataset.addSeries(series);
//        dataset.addSeries(series1);
        //         Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart("XY Chart", // Title
                "x-axis", // x-axis Label
                "y-axis", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
            );        
        
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.RED);
        plot.getRenderer().setSeriesPaint(1, Color.BLUE);
        //plot.setDomainGridlinesVisible(false); // axa x - vertical lines
        //plot.setRangeGridlinesVisible(false); // axa y -> horizontal lines
        plot.setBackgroundPaint(Color.WHITE);                
        
        plot.getDomainAxis().setTickLabelPaint(Color.ORANGE);
        plot.getRangeAxis().setTickLabelPaint(Color.GREEN);
        
        // hide labels
        //plot.getDomainAxis().setLabel("");
        //plot.getRangeAxis().setLabel("");
        
        plot.getRangeAxis().setRange(new Range(3.75, 4.4));
        
        try {
            ChartUtilities.saveChartAsJPEG(new File("C:chart.jpg"), chart, 500, 300);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }
}
