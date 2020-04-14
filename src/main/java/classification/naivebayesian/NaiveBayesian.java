package classification.naivebayesian;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 朴素贝叶斯分类算法
 * 之前博客提到的KNN算法以及决策树算法都是要求分类器给出“该数据实例属于哪一类”这类问题的明确答案，正因为如此，才出现了使用决策树分类时，有时无法判定某一测试实例属于哪一类别。使用朴素贝叶斯算法则可以避免这个问题，它给出了这个实例属于某一类别的概率值，然后通过比较概率值，可以找到该实例最有可能属于哪一类别。
 *
 * 朴素贝叶斯分类器通常有两种实现方式：一种基于贝努利模型实现，一种基于多项式模型实现。贝努利实现方式也称“词集模型”，其不考虑词在文档中出现的次数，只考虑出不出现，因此在这个意义上相当于假设词是等权重的。而多项式模型也称“词袋模型”，它考虑词在文档中的出现次数。本文采用的是多项式模型。
 这次的案例使用的是使用朴素贝叶斯过滤垃圾邮件

 可能比较乱，我把所有的模块写在了一起。首先是从两个文件夹中读取邮件，建立数据集。读取同时，会对邮件的内容进行分词，这里的分词规则比较简单，分隔符为非文本字符，可以根据自己规则定义。
 利用贝叶斯分类器对文档进行分类时，要计算多个概率的乘积以获得文档属于某个类别的概率，如果其中一个概率值为0，那么最后乘积也为0，因此为降低这种影响，可以将所有词的出现次数初始化为1，将分母初始化为2。另一个问题是下溢，这是由于太多很小的数相乘造成的，导致最后四舍五入会得到0.解决办法是对乘积取自然对数。
 最后，从训练样本中随机选取10条作为测试集，进行错误率计算，重复十次，取平均数得到最后的错误率为4%。发现结果还是不错的。
 通过特征之间的条件独立性假设，可以降低对数据量的需求。但是独立性假设是指一个词的出现概率并不依赖于文档中的其他词，可见这个假设过于简单，可能会影响最后的准确率。
 */
public class NaiveBayesian {

    private List<Double> p0Vec = null;
    //垃圾邮件中每个词出现的概率
    private List<Double> p1Vec = null;
    //垃圾邮件出现的概率
    private double pSpamRatio;

    /**
     * 初始化数据集
     *
     * @return
     */
    public List<Email> initDataSet() {
        List<Email> dataSet = new ArrayList<Email>();
        BufferedReader bufferedReader1 = null;
        BufferedReader bufferedReader2 = null;
        try {
            for (int i = 1; i < 26; i++) {
                bufferedReader1 = new BufferedReader(new InputStreamReader(
                        new FileInputStream(
                                "/home/shenchao/Desktop/MLSourceCode/machinelearninginaction/Ch04/email/ham/"
                                        + i + ".txt")));
                StringBuilder sb1 = new StringBuilder();
                String string = null;
                while ((string = bufferedReader1.readLine()) != null) {
                    sb1.append(string);
                }
                Email hamEmail = new Email();
                hamEmail.setWordList(textParse(sb1.toString()));
                hamEmail.setFlag(0);

                bufferedReader2 = new BufferedReader(new InputStreamReader(
                        new FileInputStream(
                                "/home/shenchao/Desktop/MLSourceCode/machinelearninginaction/Ch04/email/spam/"
                                        + i + ".txt")));
                StringBuilder sb2 = new StringBuilder();
                while ((string = bufferedReader2.readLine()) != null) {
                    sb2.append(string);
                }
                Email spamEmail = new Email();
                spamEmail.setWordList(textParse(sb2.toString()));
                spamEmail.setFlag(1);

                dataSet.add(hamEmail);
                dataSet.add(spamEmail);
            }
            return dataSet;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                bufferedReader1.close();
                bufferedReader2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 分词，英文的分词相比中文的分词要简单很多，这里使用的分隔符为除单词、数字外的任意字符串
     * 如果使用中文，则可以使用中科院的一套分词系统，分词效果还算不错
     *
     * @param originalString
     * @return
     * @return
     */
    private List<String> textParse(String originalString) {
        String[] s = originalString.split("\\W");
        List<String> wordList = new ArrayList<String>();
        for (String string : s) {
            if (string.contains(" ")) {
                continue;
            }
            if (string.length() > 2) {
                wordList.add(string.toLowerCase());
            }
        }
        return wordList;
    }

    /**
     * 构建单词集，此长度等于向量长度
     *
     * @return
     */
    public Set<String> createVocabList(List<Email> dataSet) {
        Set<String> set = new LinkedHashSet<String>();
        for (Email email : dataSet) {
            for (String string : email.getWordList()) {
                set.add(string);
            }
        }
        return set;
    }

    /**
     * 将邮件转换为向量
     *
     * @param vocabSet
     * @param inputSet
     * @return
     */
    public List<Integer> setOfWords2Vec(Set<String> vocabSet, Email email) {
        List<Integer> returnVec = new ArrayList<Integer>();
        for (String word : vocabSet) {
            returnVec.add(calWordFreq(word, email));
        }
        return returnVec;
    }

    /**
     * 计算一个词在某个集合中的出现次数
     *
     * @return
     */
    private int calWordFreq(String word, Email email) {
        int num = 0;
        for (String string : email.getWordList()) {
            if (string.equals(word)) {
                ++num;
            }
        }
        return num;
    }

    public void trainNB(Set<String> vocabSet, List<Email> dataSet) {
        // 训练文本的数量
        int numTrainDocs = dataSet.size();
        // 训练集中垃圾邮件的概率
        pSpamRatio = (double) calSpamNum(dataSet) / numTrainDocs;

        // 记录每个类别下每个词的出现次数
        List<Integer> p0Num = new ArrayList<Integer>();
        List<Integer> p1Num = new ArrayList<Integer>();
        // 记录每个类别下一共出现了多少词,为防止分母为0，所以在此默认值为2
        double p0Denom = 2.0, p1Denom = 2.0;
        for (Email email : dataSet) {
            List<Integer> list = setOfWords2Vec(vocabSet, email);
            // 如果是垃圾邮件
            if (email.getFlag() == 1) {
                p1Num = vecAddVec(p1Num, list);
                //计算该类别下出现的所有单词数目
                p1Denom += calTotalWordNum(list);
            }else {
                p0Num = vecAddVec(p0Num, list);
                p0Denom += calTotalWordNum(list);
            }
        }
        p0Vec = calWordRatio(p0Num, p0Denom);
        p1Vec = calWordRatio(p1Num, p1Denom);
    }

    /**
     * 两个向量相加
     *
     * @param vec1
     * @param vec2
     * @return
     */
    private List<Integer> vecAddVec(List<Integer> vec1,
                                    List<Integer> vec2) {
        if (vec1.size() == 0) {
            return vec2;
        }
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < vec1.size(); i++) {
            list.add(vec1.get(i) + vec2.get(i));
        }
        return list;
    }

    /**
     * 计算垃圾邮件的数量
     *
     * @param dataSet
     * @return
     */
    private int calSpamNum(List<Email> dataSet) {
        int time = 0;
        for (Email email : dataSet) {
            time += email.getFlag();
        }
        return time;
    }

    /**
     * 统计出现的所有单词数
     * @param list
     * @return
     */
    private int calTotalWordNum(List<Integer> list) {
        int num = 0;
        for (Integer integer : list) {
            num += integer;
        }
        return num;
    }

    /**
     * 计算每个单词在该类别下的出现概率，为防止分子为0，导致朴素贝叶斯公式为0，设置分子的默认值为1
     * @param list
     * @param wordNum
     * @return
     */
    private List<Double> calWordRatio(List<Integer> list, double wordNum) {
        List<Double> vec = new ArrayList<Double>();
        for (Integer i : list) {
            vec.add(Math.log((double)(i+1) / wordNum));
        }
        return vec;
    }

    /**
     * 比较不同类别 p(w0,w1,w2...wn | ci)*p(ci) 的大小   <br>
     *  p(w0,w1,w2...wn | ci) = p(w0|ci)*p(w1|ci)*p(w2|ci)... <br>
     *  由于防止下溢，对中间计算值都取了对数，因此上述公式化为log(p(w0,w1,w2...wn | ci)) + log(p(ci)),即
     *  化为多个式子相加得到结果
     *
     * @param emailVec
     * @return 返回概率最大值
     */
    public int classifyNB(List<Integer> emailVec) {
        double p0 = calProbabilityByClass(p0Vec, emailVec) + Math.log(1 - pSpamRatio);
        double p1 = calProbabilityByClass(p1Vec, emailVec) + Math.log(pSpamRatio);
        if (p0 > p1) {
            return 0;
        }else {
            return 1;
        }
    }

    private double calProbabilityByClass(List<Double> vec,List<Integer> emailVec) {
        double sum = 0.0;
        for (int i = 0; i < vec.size(); i++) {
            sum += (vec.get(i) * emailVec.get(i));
        }
        return sum;
    }

    public double testingNB() {
        List<Email> dataSet = initDataSet();
        List<Email> testSet = new ArrayList<Email>();
        //随机取前10作为测试样本
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int n = random.nextInt(50-i);
            testSet.add(dataSet.get(n));
            //从训练样本中删除这10条测试样本
            dataSet.remove(n);
        }
        Set<String> vocabSet = createVocabList(dataSet);
        //训练样本
        trainNB(vocabSet, dataSet);

        int errorCount = 0;
        for (Email email : testSet) {
            if (classifyNB(setOfWords2Vec(vocabSet, email)) != email.getFlag()) {
                ++errorCount;
            }
        }
//		System.out.println("the error rate is: " + (double) errorCount / testSet.size());
        return (double) errorCount / testSet.size();
    }

    public static void main(String[] args) {
        NaiveBayesian bayesian = new NaiveBayesian();
        double d = 0;
        for (int i = 0; i < 50; i++) {
            d +=bayesian.testingNB();
        }
        System.out.println("total error rate is: " + d / 50);
    }

}
