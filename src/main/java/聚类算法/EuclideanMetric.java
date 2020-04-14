package 聚类算法;

import java.util.List;

/**
 *  基于欧几里得度量的相似度计算
 *  用欧几里得度量去计算相似度非常的简单，当然，这同时带来的后果是，在某种情况下会带来很大的误差。举个例子说：比如上篇博客提到的电影案例，每个人对自己看过的电影打分，这当然存在很大的主观性，因而可能会存在这样一种情况，一些人倾向于给高分，而另一些人倾向于给低分。这当然会造成两者的距离很大偏差。而使用皮尔逊相关度算法，则不会出现这种问题，因为它们变化的大小不变，所以斜率不变。所以选择哪种算法，依情况而定。
 * Created by Administrator on 2017/6/21.
 */
public class EuclideanMetric {
    /**
     * 两个向量可以为任意维度，但必须保持维度相同，表示n维度中的两点
     *
     * @param vector1
     * @param vector2
     * @return 两点间距离
     */
    public static double sim_distance(Double[] vector1, Double[] vector2) {
        double distance = 0;
        if (vector1.length == vector2.length) {
            for (int i = 0; i < vector1.length; i++) {
                double temp = Math.pow((vector1[i] - vector2[i]), 2);
                distance += temp;
            }
            distance = Math.sqrt(distance);
        }
        return distance;
    }

    public static Double sim_distance(List<Double>  vector, List<Double> vector1) {
        Double[] doubles1 = vector.toArray(new Double[vector.size()]);
        Double[] doubles2 = vector1.toArray(new Double[vector.size()]);
        return sim_distance(doubles1,doubles2);
    }
}
