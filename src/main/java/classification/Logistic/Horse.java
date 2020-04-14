package classification.Logistic;

import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */
public class Horse {

    private List<Double> attributes;
    private String label;

    public List<Double> getAttributes() {
        return attributes;
    }
    public void setAttributes(List<Double> attributes) {
        this.attributes = attributes;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}
