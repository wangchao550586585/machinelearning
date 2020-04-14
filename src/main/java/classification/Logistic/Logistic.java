package classification.Logistic;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * 用Logistic回归进行分类
 */
public class Logistic {

    public List<Horse> initDataSet(String fileName) {
        List<Horse> dataSet = new ArrayList<Horse>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    "/home/shenchao/Desktop/MLSourceCode/machinelearninginaction/Ch05/"+fileName)));
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                Horse horse = new Horse();
                String[] s = line.split("\t");
                List<Double> list = new ArrayList<Double>();
                for (int i = 0; i < s.length-1; i++) {
                    list.add(Double.parseDouble(s[i]));
                }
                horse.setAttributes(list);
                horse.setLabel(s[s.length-1]);

                dataSet.add(horse);
            }
            return dataSet;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 改进的随机梯度上升算法
     *
     * @param trainDataSet 训练集
     * @param numIter 迭代次数
     * @return 权重向量
     */
    public List<Double> stocGradAscent(List<Horse> trainDataSet, int numIter) {
        //初始化回归系数
        List<Double> weights = new ArrayList<Double>();
        for (int i = 0; i < trainDataSet.get(0).getAttributes().size(); i++) {
            weights.add(1.0);
        }

        for (int i = 0; i < numIter; i++) {
            for (int j = 0; j < trainDataSet.size(); j++) {
                double alpha = 4.0/(1.0+i+j) + 0.01;
                int randIndex = new Random().nextInt(trainDataSet.size());
                double h = sigmoid(vecMultipVec(trainDataSet.get(randIndex).getAttributes(), weights));
                double error = Double.parseDouble(trainDataSet.get(randIndex).getLabel()) - h;
                weights = vecAddVec(weights,alpha, error, trainDataSet.get(randIndex).getAttributes());
                trainDataSet.remove(randIndex);
            }
        }

        return weights;
    }

    private List<Double> vecAddVec(List<Double> weights, double alpha,
                                   double error, List<Double> attributes) {
        List<Double> list = new ArrayList<Double>();
        for (int i = 0; i < weights.size(); i++) {
            list.add(weights.get(i) + alpha * error * attributes.get(i));
        }
        return list;
    }


    /**
     * 计算向量的内积
     * @param attributes
     * @param weights
     * @return
     */
    private double vecMultipVec(List<Double> attributes, List<Double> weights) {
        double sum = 0.0;
        for (int i = 0; i < attributes.size(); i++) {
            sum += attributes.get(i) * weights.get(i);
        }
        return sum;
    }


    /**
     * @param x
     * @return
     */
    private double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }


    public double test() {
        List<Horse> trainDataSet = initDataSet("horseColicTraining.txt");
        List<Horse> testDataSet = initDataSet("horseColicTest.txt");

        List<Double> trainWeights = stocGradAscent(trainDataSet, 500);
        int errorCount = 0;

        for (Horse horse : testDataSet) {
            if ((int)classifyVector(horse.getAttributes() , trainWeights) != (int)(Double.parseDouble(horse.getLabel()))) {
                ++errorCount;
            }
        }
        System.out.println("the error rate of this test is: " + (double) errorCount / testDataSet.size());
        return (double) errorCount / testDataSet.size();
    }

    private double classifyVector(List<Double> attributes,
                                  List<Double> trainWeights) {
        double prob = sigmoid(vecMultipVec(attributes, trainWeights));
        if (prob > 0.5) {
            return 1.0;
        }
        return 0.0;
    }


    public static void main(String[] args) {
        Logistic logistic = new Logistic();
        double sum = 0.0;
        for (int i = 0; i < 10; i++) {
            sum += logistic.test();
        }
        System.out.println("after 10 iterations the average error rate is: " + sum / 10 );
    }

}
