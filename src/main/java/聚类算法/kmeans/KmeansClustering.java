package 聚类算法.kmeans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 *  K-means聚类算法
 1）从样本D中随机选取K个元素，作为K个簇的中心
 2）分别计算剩下的元素到K个簇的距离，将这些元素归化到距离最短的簇
 3）根据聚类结果，重新计算K个簇各自的中心，计算方法是取簇中所有元素各自维度的算术平均
 4）将D中的元素按照新的中心重新聚类
 5）重复第四步，直到中心不发生变化
 6）将结果输出

 算法不足之处：最终的结果与初始化K个中心值的选择有很大的关系，容易受噪音干扰。
 * Created by Administrator on 2017/6/21.
 */
public class KmeansClustering {
    private List<Point> dataset = null;

    public KmeansClustering() throws IOException {
        initDataSet();
    }

    /**
     * 初始化数据集
     *
     * @throws IOException
     */
    private void initDataSet() throws IOException {
        dataset = new ArrayList<Point>();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(KmeansClustering.class.getClassLoader()
                        .getResourceAsStream("data.txt")));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] s = line.split("\t");
            Point point = new Point();
            point.setX(Double.parseDouble(s[1]));
            point.setY(Double.parseDouble(s[2]));
            point.setName(s[0]);

            dataset.add(point);
        }
    }

    /**
     * @param k
     *            聚类的数目
     */
    public Map<Point,List<Point>> kcluster(int k) {
        // 随机从样本集合中选取k个样本点作为聚簇中心
        // 每个聚簇中心有哪些点
        Map<Point,List<Point>> nowClusterCenterMap = new HashMap<Point, List<Point>>();
        for (int i = 0; i < k; i++) {
            Random random = new Random();
            int num = random.nextInt(dataset.size());
            nowClusterCenterMap.put(dataset.get(num), new ArrayList<Point>());
        }

        //上一次的聚簇中心
        Map<Point,List<Point>> lastClusterCenterMap = null;

        // 找到离中心最近的点,然后加入以该中心为map键的list中
        while (true) {
            for (Point point : dataset) {
                double shortest = Double.MAX_VALUE;
                Point key = null;
                for (Map.Entry<Point, List<Point>> entry : nowClusterCenterMap.entrySet()) {
                    double distance = distance(point, entry.getKey());
                    if (distance < shortest) {
                        shortest = distance;
                        key = entry.getKey();
                    }
                }
                nowClusterCenterMap.get(key).add(point);
            }

            //如果结果与上一次相同，则整个过程结束
            if (isEqualCenter(lastClusterCenterMap,nowClusterCenterMap)) {
                break;
            }
            lastClusterCenterMap = nowClusterCenterMap;
            nowClusterCenterMap = new HashMap<Point, List<Point>>();
            //把中心点移到其所有成员的平均位置处,并构建新的聚簇中心
            for (Map.Entry<Point,List<Point>> entry : lastClusterCenterMap.entrySet()) {
                nowClusterCenterMap.put(getNewCenterPoint(entry.getValue()), new ArrayList<Point>());
            }

        }
        return nowClusterCenterMap;
    }

    /**
     *      判断前后两次是否是相同的聚簇中心，若是则程序结束，否则继续,知道相同
     * @param lastClusterCenterMap
     * @param nowClusterCenterMap
     * @return bool
     */
    private boolean isEqualCenter(Map<Point, List<Point>> lastClusterCenterMap,
                                  Map<Point, List<Point>> nowClusterCenterMap) {
        if (lastClusterCenterMap == null) {
            return false;
        }else {
            for (Map.Entry<Point, List<Point>> entry : lastClusterCenterMap.entrySet()) {
                if (!nowClusterCenterMap.containsKey(entry.getKey())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 计算新的中心
     *
     * @param value
     * @return Point
     */
    private Point getNewCenterPoint(List<Point> value) {
        double sumX = 0.0, sumY = 0.0;
        for (Point point : value) {
            sumX += point.getX();
            sumY += point.getY();
        }
//      System.out.println((int)sumX / value.size() + "===" + (int)sumY / value.size());
        Point point = new Point();
        point.setX(sumX / value.size());
        point.setY(sumY / value.size());
        return  point;
    }

    /**
     * 使用欧几里得算法计算两点之间距离
     *
     * @param point1
     * @param point2
     * @return 两点之间距离
     */
    private double distance(Point point1, Point point2) {
        double distance = Math.pow((point1.getX() - point2.getX()), 2)
                + Math.pow((point1.getY() - point2.getY()), 2);
        distance = Math.sqrt(distance);
        return distance;
    }

    public static void main(String[] args) throws IOException {
        KmeansClustering kmeansClustering = new KmeansClustering();
        Map<Point, List<Point>> result = kmeansClustering.kcluster(3);
        for (Map.Entry<Point, List<Point>> entry : result.entrySet()) {
            System.out.println("===============聚簇中心为：" + entry.getKey() + "================");
            for (Point point : entry.getValue()) {
                System.out.println(point.getName());
            }
        }
    }
}
