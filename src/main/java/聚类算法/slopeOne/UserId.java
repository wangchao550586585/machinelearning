package 聚类算法.slopeOne;


public class UserId  {
    String content;
    public UserId(String s) {
        content = s;
    }

    public int hashCode() { return content.hashCode();}
    public String toString() { return content; }
}
