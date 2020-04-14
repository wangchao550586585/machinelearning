package chart;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * 柱状图
 * Created by Administrator on 2017/6/21.
 */
public class BarChart {
    public BarChart() {
        Map<String,Integer> map = initDataSet();
        showChart(map);
    }

    /**
     *  初始化数据集
     *      柱状图的数据集接受三个参数，第一个参数为柱子的高度，第二个为几个类别进行比较，第三个为共有几组数据
     *
     * @return
     */
    private Map<String, Integer> initDataSet() {
        return null;
    }

    /**
     * @param map
     */
    private void showChart(Map<String, Integer> map) {
        // 创建饼图数据对象
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Set<Entry<String, Integer>> set = map.entrySet();
        for (Entry<String, Integer> entry : set) {
            dataset.setValue(entry.getValue(), "评论数量",entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart3D("评论次数TOP10", "好友昵称",
                "评论数量", dataset, PlotOrientation.VERTICAL, false, true, true);
        ChartFrame frame = new ChartFrame("评论次数TOP10", chart, true);
        // 自定义设定背景色
        // chart.setBackgroundPaint(Color.getHSBColor(23,192,223));
        chart.setBackgroundPaint(Color.WHITE);
        // 获得 plot：3dBar为CategoryPlot
        CategoryPlot categoryPlot = chart.getCategoryPlot();
        // 设定图表数据显示部分背景色
        categoryPlot.setBackgroundPaint(Color.WHITE);
        // 横坐标网格线
        categoryPlot.setDomainGridlinePaint(Color.GRAY);
        // 设置网格线可见
        categoryPlot.setDomainGridlinesVisible(true);
        // 纵坐标网格线
        categoryPlot.setRangeGridlinePaint(Color.GRAY);
        // 重要的类，负责生成各种效果
        // BarRenderer3D renderer=(BarRenderer3D) categoryPlot.getRenderer();
        // 获取纵坐标
        NumberAxis numberaxis = (NumberAxis) categoryPlot.getRangeAxis();

        // 设置纵坐标的标题字体和大小
        numberaxis.setLabelFont(new Font("黑体", Font.CENTER_BASELINE, 16));
        // 设置丛坐标的坐标值的字体颜色
        numberaxis.setLabelPaint(Color.BLACK);
        // 设置丛坐标的坐标轴标尺颜色
        numberaxis.setTickLabelPaint(Color.BLACK);
        // 坐标轴标尺颜色
        numberaxis.setTickMarkPaint(Color.BLUE);
        // 丛坐标的默认间距值
        // numberaxis.setAutoTickUnitSelection(true);
        // 设置丛坐标间距值
        numberaxis.setAutoTickUnitSelection(true);
        // numberaxis.setTickUnit(new NumberTickUnit(150));

        //在柱体的上面显示数据
        BarRenderer custombarrenderer3d = new BarRenderer();
        custombarrenderer3d.setBaseItemLabelPaint(Color.BLACK);//数据字体的颜色
        custombarrenderer3d.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        custombarrenderer3d.setBaseItemLabelsVisible(true);
        categoryPlot.setRenderer(custombarrenderer3d);


        // 获取横坐标
        CategoryAxis domainAxis = categoryPlot.getDomainAxis();
        // 设置横坐标的标题字体和大小
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 16));

        // 设置横坐标的坐标值的字体颜色
        domainAxis.setTickLabelPaint(Color.GRAY);
        // 设置横坐标的坐标值的字体
        domainAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 16));
        // 设置横坐标的显示
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions
                .createUpRotationLabelPositions(0.4));
        // 这句代码解决了底部汉字乱码的问题
//      chart.getLegend().setItemFont(new Font("黑体", 0, 16));
        // 设置图例标题
        Font font = new java.awt.Font("黑体", java.awt.Font.CENTER_BASELINE, 20);
        TextTitle title = new TextTitle("谁是评论你最多的人？");
        title.getBackgroundPaint();
        title.setFont(font);
        // 设置标题的字体颜色
        chart.setTitle(title);
        frame.pack();
        frame.setVisible(true);
    }
}
