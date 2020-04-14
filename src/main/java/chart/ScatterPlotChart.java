package chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import classification.KNN.Data;

/**
 * @author shenchao 散点图
 */
public class ScatterPlotChart {

    public ScatterPlotChart() {
        Map<Integer,List<Data>> map = initDataSet();
        showChart(map);
    }

    /**
     *  这里的Data类是数据的封装，可以安装实际需求实现，map集合的键为类别
     */
    private Map<Integer, List<Data>> initDataSet() {
        return null;
    }

    public void showChart(Map<Integer,List<Data>> map) {

        DefaultXYDataset xydataset = new DefaultXYDataset();

        //根据类别建立数据集
        for (Entry<Integer, List<Data>> entry : map.entrySet()) {
            List<Data> l = entry.getValue();

            int size = l.size();
            //散点图要求数据集为二维数组
            double[][] datas = new double[2][size];
            for (int i = 0; i < size; i++) {
                Data data = l.get(i);
                datas[0][i] = data.getDistance();
                datas[1][i] = data.getIcecream();
            }
            xydataset.addSeries(entry.getKey(), datas);
        }

        JFreeChart chart = ChartFactory.createScatterPlot("散点图", "每年获取的飞行常客里程数", "每周所消费的冰淇淋公升数", xydataset, PlotOrientation.VERTICAL, true, false, false);
        ChartFrame frame = new ChartFrame("散点图", chart, true);
        chart.setBackgroundPaint(Color.white);
        chart.setBorderPaint(Color.GREEN);
        chart.setBorderStroke(new BasicStroke(1.5f));
        XYPlot xyplot = (XYPlot) chart.getPlot();

        xyplot.setBackgroundPaint(new Color(255, 253, 246));
        ValueAxis vaaxis = xyplot.getDomainAxis();
        vaaxis.setAxisLineStroke(new BasicStroke(1.5f));

        ValueAxis va = xyplot.getDomainAxis(0);
        va.setAxisLineStroke(new BasicStroke(1.5f));

        va.setAxisLineStroke(new BasicStroke(1.5f)); // 坐标轴粗细
        va.setAxisLinePaint(new Color(215, 215, 215)); // 坐标轴颜色
        xyplot.setOutlineStroke(new BasicStroke(1.5f)); // 边框粗细
        va.setLabelPaint(new Color(10, 10, 10)); // 坐标轴标题颜色
        va.setTickLabelPaint(new Color(102, 102, 102)); // 坐标轴标尺值颜色
        ValueAxis axis = xyplot.getRangeAxis();
        axis.setAxisLineStroke(new BasicStroke(1.5f));

        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
                .getRenderer();
        xylineandshaperenderer.setSeriesOutlinePaint(0, Color.WHITE);
        xylineandshaperenderer.setUseOutlinePaint(true);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setTickMarkInsideLength(2.0F);
        numberaxis.setTickMarkOutsideLength(0.0F);
        numberaxis.setAxisLineStroke(new BasicStroke(1.5f));

        frame.pack();
        frame.setVisible(true);
    }

}
