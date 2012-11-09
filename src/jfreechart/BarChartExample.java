package jfreechart;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChartExample {
    public static void main(String[] args) {
        // Create a simple Bar chart
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    	dataset.setValue(6, "Profit1", "Jane");
    	dataset.setValue(3, "Profit2", "Jane");
    	dataset.setValue(7, "Profit1", "Tom");
    	dataset.setValue(10, "Profit2", "Tom");
    	dataset.setValue(8, "Profit1", "Jill");
    	dataset.setValue(8, "Profit2", "Jill");
    	dataset.setValue(5, "Profit1", "John");
    	dataset.setValue(6, "Profit2", "John");
    	dataset.setValue(12, "Profit1", "Fred");
    	dataset.setValue(5, "Profit2", "Fred");
    	// Profit1, Profit2 represent the row keys
    	// Jane, Tom, Jill, etc. represent the column keys
    	JFreeChart chart = ChartFactory.createBarChart3D("Comparison between Salesman",
    	"Salesman", "Value ($)", dataset, PlotOrientation.VERTICAL, true, true, false );
    	CategoryPlot plot = chart.getCategoryPlot();
    	plot.setRenderer(new CylinderRenderer());
    	
        try {        	        	        
            ChartUtilities.saveChartAsJPEG(new File("C:chart.jpg"), chart, 500,
                300);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }
}
