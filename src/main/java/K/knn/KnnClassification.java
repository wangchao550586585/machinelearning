/**
 *@Description: KNN分类
 */
package K.knn;

import K.KMENS.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings({"rawtypes"})
public abstract class KnnClassification<T> {
    private List<KnnValueBean> dataArray;
    private int K = 3;

    public int getK() {
        return K;
    }
    public void setK(int K) {
        if (K < 1) {
            throw new IllegalArgumentException("K must greater than 0");
        }
        this.K = K;
    }

    /**
     * @param value
     * @param typeId
     * @Author:lulei
     * @Description: 向模型中添加记录
     */
    public void addRecord(T value, String typeId) {
        if (dataArray == null) {
            dataArray = new ArrayList<KnnValueBean>();
        }
        dataArray.add(new KnnValueBean<T>(value, typeId));
    }

    /**
     * @param value
     * @return
     * @Author:lulei
     * @Description: KNN分类判断value的类别
     */
    public String getTypeId(T value) {
        KnnValueSort[] array = getKType(value);
        System.out.println(JsonUtil.parseJson(array));
        HashMap<String, Integer> map = new HashMap<String, Integer>(K);
        for (KnnValueSort bean : array) {
            if (bean != null) {
                if (map.containsKey(bean.getTypeId())) {
                    map.put(bean.getTypeId(), map.get(bean.getTypeId()) + 1);
                } else {
                    map.put(bean.getTypeId(), 1);
                }
            }
        }
        String maxTypeId = null;
        int maxCount = 0;
        Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Integer> entry = iter.next();
            if (maxCount < entry.getValue()) {
                maxCount = entry.getValue();
                maxTypeId = entry.getKey();
            }
        }
        return maxTypeId;
    }

    /**
     * @param value
     * @return
     * @Author:lulei
     * @Description: 获取距离最近的K个分类
     */
    private KnnValueSort[] getKType(T value) {
        int k = 0;
        KnnValueSort[] topK = new KnnValueSort[K];
        for (KnnValueBean<T> bean : dataArray) {
            double score = similarScore(bean.getValue(), value);
            if (k == 0) {
                //数组中的记录个数为0是直接添加  
                topK[k] = new KnnValueSort(bean.getTypeId(), score);
                k++;
            } else {
                if (!(k == K && score < topK[k -1].getScore())){
                    int i = 0;
                    //找到要插入的点  
                    for (; i < k && score < topK[i].getScore(); i++);
                    int j = k - 1;
                    if (k < K) {
                        j = k;
                        k++;
                    }
                    for (; j > i; j--) {
                        topK[j] = topK[j - 1];
                    }
                    topK[i] = new KnnValueSort(bean.getTypeId(), score);
                }
            }
        }
        return topK;
    }

    /**
     * @param o1
     * @param o2
     * @return
     * @Author:lulei
     * @Description: o1 o2之间的相似度 
     */
    public abstract double similarScore(T o1, T o2);
}  