package jfreechart;

/**
 * User: mihai.panaitescu
 * Date: 01-Oct-2010
 * Time: 14:17:41
 */
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Test {

    public static void main(String[] args) throws IOException{

        ValueAxis xAxis = new NumberAxis("Input Increase");
        ValueAxis yAxis = new NumberAxis("Production");
        XYSeries xySeries = new XYSeries(new Integer(1));
        xySeries.add(0, 200);
        xySeries.add(1, 300);
        xySeries.add(2, 500);
        xySeries.add(3, 700);
        xySeries.add(4, 700);
        xySeries.add(5, 900);

        XYSeriesCollection xyDataset = new XYSeriesCollection(xySeries);

        // create XYPlot
        XYPlot xyPlot = new XYPlot(xyDataset, xAxis, yAxis,
                new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES));
        JFreeChart chart = new JFreeChart(xyPlot);

        // get ImageMap
        ChartRenderingInfo info = new ChartRenderingInfo();


        // populate the info
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG(out,chart,400,300,info);
            Shape area1 = new Rectangle(10, 20, 40, 30);
            StandardEntityCollection ec = new StandardEntityCollection();
            ChartEntity chartEntity = new ChartEntity(area1);
            chartEntity.setToolTipText("http://www.helpdesk.com.au");
            ec.add(chartEntity);
            info.setEntityCollection(ec);
            String imageMap = ChartUtilities.getImageMap("mapTest",info);
            out.close();

            System.out.println(imageMap);

        } catch (IOException e) {
            //logger.error("Failed creating map tag. Error was " + e);
        }

    }

}