package classification.naivebayesian;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/6/21.
 */
public class Email {
    //每封邮件分好词的集合
    private List<String> wordList;
    //每封邮件是否是垃圾邮件
    private int flag;

    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public List<String> getWordList() {
        return wordList;
    }
    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

}
