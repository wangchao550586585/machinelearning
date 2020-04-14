package K.knn;

/**
 *  K个最近邻的类别得分
 * Created by Administrator on 2017/6/21.
 */
public class KnnValueSort {
    private String typeId;//分类ID
    private double score;//该分类得分

    public KnnValueSort(String typeId, double score) {
        this.typeId = typeId;
        this.score = score;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }
}
