package 聚类算法.slopeOne;

/**
 * Created by Administrator on 2017/6/28.
 */
public class ItemId {
        String content;
        public ItemId(String s) {
            content = s;
        }
        public int hashCode() { return content.hashCode();}
        public String toString() { return content; }
}
