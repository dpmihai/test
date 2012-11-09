package jfreechart;

/**
 * User: mihai.panaitescu
 * Date: 01-Oct-2010
 * Time: 13:53:57
 */

import java.awt.Font;
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
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.util.TableOrder;

/**
 * Creates an HTML image map for a multiple pie chart.
 */
public class ImageMapDemo {

    /**
     * Default constructor.
     */
    public ImageMapDemo() {
        super();
    }

    /**
     * Saves the chart image and HTML.
     */
    public void saveImageAndHTML() {

        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

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
            final File file1 = new File("multipiechart100.png");
            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);

            // write an HTML page incorporating the image with an image map
            final File file2 = new File("multipiechart100.html");
            final OutputStream out = new BufferedOutputStream(new FileOutputStream(file2));
            final PrintWriter writer = new PrintWriter(out);
            writer.println("<HTML>");
            writer.println("<HEAD><TITLE>JFreeChart Image Map Demo</TITLE></HEAD>");
            writer.println("<BODY>");
            ChartUtilities.writeImageMap(writer, "chart", info, true);
            writer.println("<IMG SRC=\"multipiechart100.png\" "
                    + "WIDTH=\"600\" HEIGHT=\"400\" BORDER=\"0\" USEMAP=\"#chart\">");
            writer.println("</BODY>");
            writer.println("</HTML>");
            writer.close();

        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    private CategoryDataset createDataset() {
        final double[][] data = new double[][] {
            {3.0, 4.0, 3.0, 5.0},
            {5.0, 7.0, 6.0, 8.0},
            {5.0, 7.0, 3.0, 8.0},
            {1.0, 2.0, 3.0, 4.0},
            {2.0, 3.0, 2.0, 3.0}
        };
        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
            "Region ",
            "Sales/Q",
            data
        );
        return dataset;
    }
    /**
     * Creates a sample chart with the given dataset.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createMultiplePieChart(
            "Multiple Pie Chart",  // chart title
            dataset,               // dataset
            TableOrder.BY_ROW,
            true,                  // include legend
            true,
            true
        );
        final MultiplePiePlot plot = (MultiplePiePlot) chart.getPlot();
        final JFreeChart subchart = plot.getPieChart();
        final PiePlot p = (PiePlot) subchart.getPlot();
        p.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
        p.setLabelFont(new Font("SansSerif", Font.PLAIN, 8));
        p.setInteriorGap(0.30);

        return chart;
    }

    /**
     * Starting point for the demo.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
        final ImageMapDemo demo = new ImageMapDemo();
        demo.saveImageAndHTML();
    }

}
