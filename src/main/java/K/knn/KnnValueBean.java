package K.knn;

/**
 * KNN分类模型中一条记录的存储格式
 * Created by Administrator on 2017/6/21.
 */
public class KnnValueBean<T> {
    private T value;//记录值
    private String typeId;//分类ID

    public KnnValueBean(T value, String typeId) {
        this.value = value;
        this.typeId = typeId;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
