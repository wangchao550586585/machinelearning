package 聚类算法.Hierarchical;

import 聚类算法.EuclideanMetric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 层次聚类算法
 *   聚类算法属于机器学习中一种无监督学习算法。聚类方法一般可以分为层次聚类与非层次聚类两种。其中层次聚类算法又可以分为合并法与分解法；同样非层次聚类算法也可以分为多种，常用的有K-means算法。这篇博客先来实现层次聚类算法中的合并法，我会在下一篇博文中讲述K-means算法。
 其中，合并法是指：初始阶段，将每个样本点当做其类簇，然后合并这些原子类簇直至达到预期的类簇数或者其他终止条件。
 算法实现：
 输入： K：目标类簇数D：样本点集合
 输出：K个类簇集合
 方法：
 1）将D中各个样本点当做类簇集合
 2）repeat
 3）  找到分属两个不同类簇，且距离最近的样本点对
 4）  将两个类簇合并
 5） util 类簇数=K

 此算法缺点：效率比较低下，每次循环都要计算每两个类簇之间的距离，有些可能要重复计算。还有一不足是，不具有再分配能力，即如果样本点A在某次迭代过程中已经划分给类簇C1，那么在后面的迭代过程中永远属于C1，这将影响聚类结果。
 改进：一般情况下，层次聚类通常和划分式聚类算法组合，这样既可以解决效率问题，又可以解决样本点再分配问题，这样的算法有BIRCH算法等。首先把邻近点划分到微簇，然后对这些微簇进行k-means算法。
 * Created by Administrator on 2017/6/21.
 */
public class HierarchicalClustering {
    private List<Cluster> clusters = null;

    public HierarchicalClustering() throws IOException {
        initData();
    }

    /**
     * 初始化数据集
     *
     * @throws IOException
     */
    private void initData() throws IOException {
        clusters = new ArrayList<Cluster>();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(HierarchicalClustering.class
                        .getClassLoader().getResourceAsStream("data.txt")));
        String line = null;
        int i = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String[] s = line.split("\t");
            Cluster cluster = new Cluster();
            List<Double> list = new ArrayList<Double>();
            for (String string : s) {
                try{
                    list.add(Double.parseDouble(string));
                }catch(Exception e) {
                    cluster.setName(string);
                }
            }
            cluster.setId(i++);
            cluster.setVector(list);

            clusters.add(cluster);
        }

    }

    public Cluster hcluster() {
        // 用distances来缓存任意两聚类之间的距离,其中map集合的键为两个聚类的id
        Map<Integer[], Double> distances = new HashMap<Integer[], Double>();

        int currentId = -1;

        while (clusters.size() > 1) {
            // 最短距离的两聚类id
            int lowestpair1 = 0;
            int lowestpair2 = 1;
            // 最短距离
            double closest = EuclideanMetric.sim_distance(clusters.get(0)
                    .getVector(), clusters.get(1).getVector());

            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    Integer[] key = { clusters.get(i).getId(),
                            clusters.get(j).getId() };
                    if (!distances.containsKey(key)) {
                        distances.put(key, EuclideanMetric.sim_distance(
                                clusters.get(i).getVector(), clusters.get(j)
                                        .getVector()));
                    }

                    double d = distances.get(key);
                    if (d < closest) {
                        closest = d;
                        lowestpair1 = i;
                        lowestpair2 = j;
                    }
                }
            }

            // 计算两个最短距离聚类的平均值
            List<Double> midvec = mergevec(clusters.get(lowestpair1),
                    clusters.get(lowestpair2));

            Cluster cluster = new Cluster(clusters.get(lowestpair1),clusters.get(lowestpair2),midvec,currentId,closest);

            currentId -= 1;

            //注意删除顺序，先删除大的id号，否则会出现越界
            if (lowestpair1 < lowestpair2) {
                clusters.remove(clusters.get(lowestpair2));
                clusters.remove(clusters.get(lowestpair1));
            }else {
                clusters.remove(clusters.get(lowestpair1));
                clusters.remove(clusters.get(lowestpair2));
            }
            clusters.add(cluster);
        }
        return clusters.get(0);
    }

    private List<Double> mergevec(Cluster cluster1, Cluster cluster2) {
        List<Double> midvec = new ArrayList<Double>();
        for (int i = 0; i < cluster1.getVector().size(); i++) {
            midvec.add((cluster1.getVector().get(i) + cluster2.getVector().get(i)) / 2.0);
        }
        return midvec;
    }

    /**
     * 打印输出
     */
    public void printCluster(Cluster cluster,int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("  ");
        }
        //负数标记代表这是一个分支
        if (cluster.getId() < 0) {
            System.out.println("-");
        }else {
            //代表是一个叶子节点
            System.out.println(cluster.getName());
        }

        if (cluster.getLeft()!= null) {
            printCluster(cluster.getLeft(),++n);
        }
        if (cluster.getRight()!=null) {
            printCluster(cluster.getRight(), ++n);
        }
    }

    public static void main(String[] args) throws IOException {
        HierarchicalClustering hierarchicalClustering = new HierarchicalClustering();
        hierarchicalClustering.printCluster(hierarchicalClustering.hcluster(), 0);
    }

}
