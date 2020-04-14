package chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * 折线图
 * Created by Administrator on 2017/6/21.
 */
public class LineChart {

    public LineChart() {
        Map<Integer, Integer> map = initDataSet();
        showChart(map);
    }

    /**
     *  初始化数据集
     *      柱状图的数据集接受三个参数，第一个参数为柱子的高度，第二个为几个类别进行比较，第三个为共有几组数据
     *
     * @return
     */
    private Map<Integer, Integer> initDataSet() {
        return null;
    }

    private void showChart(Map<Integer, Integer> map) {
        // 创建饼图数据对象
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        Set<Map.Entry<Integer, Integer>> set = map.entrySet();
        for (Map.Entry<Integer, Integer> entry : set) {
            dataSet.setValue(entry.getValue(), "数量", entry.getKey());
        }

        // 如果把createLineChart改为createLineChart3D就变为了3D效果的折线图
        JFreeChart chart = ChartFactory.createLineChart("每年说说的发布量", "年份", "数目",
                dataSet, PlotOrientation.VERTICAL, // 绘制方向
                false, // 显示图例
                true, // 采用标准生成器
                false // 是否生成超链接
        );
        ChartFrame frame = new ChartFrame("图表标题", chart, true);
        chart.setBackgroundPaint(Color.WHITE);// 设置背景色
        // 获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE); // 设置绘图区背景色
        plot.setRangeGridlinePaint(Color.GRAY); // 设置水平方向背景线颜色
        plot.setRangeGridlinesVisible(true);// 设置是否显示水平方向背景线,默认值为true
        plot.setDomainGridlinePaint(Color.GRAY); // 设置垂直方向背景线颜色
        plot.setDomainGridlinesVisible(true); // 设置是否显示垂直方向背景线,默认值为false

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0.01);// 左边距 边框距离
        domainAxis.setUpperMargin(0.06);// 右边距 边框距离,防止最后边的一个数据靠近了坐标轴。
        domainAxis.setMaximumCategoryLabelLines(2);
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 16));

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// Y轴显示整数
        rangeAxis.setAutoRangeMinimumSize(1); // 最小跨度
        rangeAxis.setUpperMargin(0.18);// 上边距,防止最大的一个数据靠近了坐标轴。
        rangeAxis.setLowerBound(0); // 最小值显示0
        rangeAxis.setAutoRange(false); // 不自动分配Y轴数据
        rangeAxis.setTickMarkStroke(new BasicStroke(1.6f)); // 设置坐标标记大小
        rangeAxis.setTickMarkPaint(Color.BLACK); // 设置坐标标记颜色
        rangeAxis.setLabelFont(new Font("宋体", Font.PLAIN, 16));

        // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) plot
                .getRenderer();
        lineandshaperenderer.setBaseShapesVisible(true); // series 点（即数据点）可见

        lineandshaperenderer.setBaseLinesVisible(true);
        // 显示折点数据
        lineandshaperenderer
                .setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        lineandshaperenderer.setBaseItemLabelsVisible(true);

        frame.pack();
        frame.setVisible(true);
    }
}
