package K.knn;

import K.KMENS.JsonUtil;

/**
 * Created by Administrator on 2017/6/21.
 */
public class Test extends KnnClassification<Integer> {

    @Override
    public double similarScore(Integer o1, Integer o2) {
        return -1 * Math.abs(o1 - o2);
    }

    /**
     * @param args
     * @Author:lulei
     * @Description:
     */
    public static void main(String[] args) {
        Test test = new Test();
        for (int i = 1; i < 10; i++) {
            test.addRecord(i, i > 5 ? "0" : "1");
        }
        System.out.println(JsonUtil.parseJson(test.getTypeId(0)));

    }
}
