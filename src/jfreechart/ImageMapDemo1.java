package jfreechart;

/**
 * User: mihai.panaitescu
 * Date: 01-Oct-2010
 * Time: 14:03:22
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 * A demo showing how to create an HTML image map for a bar chart.
 */
public class ImageMapDemo1 {

    /**
     * Default constructor.
     */
    public ImageMapDemo1() {
        super();
    }

    /**
     * Starting point for the demo.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        // create a chart
        final double[][] data = new double[][] {
            {56.0, -12.0, 34.0, 76.0, 56.0, 100.0, 67.0, 45.0},
            {37.0, 45.0, 67.0, 25.0, 34.0, 34.0, 100.0, 53.0},
            {43.0, 54.0, 34.0, 34.0, 87.0, 64.0, 73.0, 12.0}
        };
        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
            "Series ", "Type ", data
        );

        JFreeChart chart = null;
        final boolean drilldown = true;

        if (drilldown) {
            final CategoryAxis categoryAxis = new CategoryAxis("Category");
            final ValueAxis valueAxis = new NumberAxis("Value");
            final BarRenderer renderer = new BarRenderer();
            renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
            renderer.setItemURLGenerator(new StandardCategoryURLGenerator("bar_chart_detail.jsp"));
            final CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
            plot.setOrientation(PlotOrientation.VERTICAL);
            chart = new JFreeChart("Bar Chart", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        }
        else {
            chart = ChartFactory.createBarChart(
                "Vertical Bar Chart",  // chart title
                "Category",            // domain axis label
                "Value",               // range axis label
                dataset,               // data
                PlotOrientation.VERTICAL,
                true,                  // include legend
                true,
                false
            );
        }
        chart.setBackgroundPaint(java.awt.Color.white);

        // ****************************************************************************
        // * JFREECHART DEVELOPER GUIDE                                               *
        // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
        // * to purchase from Object Refinery Limited:                                *
        // *                                                                          *
        // * http://www.object-refinery.com/jfreechart/guide.html                     *
        // *                                                                          *
        // * Sales are used to provide funding for the JFreeChart project - please    *
        // * support us so that we can continue developing free software.             *
        // ****************************************************************************

        // save it to an image
        try {
            final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            final File file1 = new File("barchart100.png");
            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);

            // write an HTML page incorporating the image with an image map
            final File file2 = new File("barchart100.html");
            final OutputStream out = new BufferedOutputStream(new FileOutputStream(file2));
            final PrintWriter writer = new PrintWriter(out);
            writer.println("<HTML>");
            writer.println("<HEAD><TITLE>JFreeChart Image Map Demo</TITLE></HEAD>");
            writer.println("<BODY>");
            ChartUtilities.writeImageMap(writer, "chart", info, true);
            writer.println("<IMG SRC=\"barchart100.png\" "
                           + "WIDTH=\"600\" HEIGHT=\"400\" BORDER=\"0\" USEMAP=\"#chart\">");
            writer.println("</BODY>");
            writer.println("</HTML>");
            writer.close();

        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

    }

}