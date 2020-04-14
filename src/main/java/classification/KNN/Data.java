package classification.KNN;


public class Data implements Comparable<Data>{

    /**
     * 每年获得的飞行常客里程数
     */
    private double mile;
    /**
     * 玩视频游戏所耗时间百分比
     */
    private double time;
    /**
     * 每周消费的冰淇淋公升数
     */
    private double icecream;
    /**
     *  1 代表不喜欢的人
     *  2 代表魅力一般的人
     *  3 代表极具魅力的人
     */
    private int type;
    /**
     * 两个数据距离
     */
    private double distance;

    public double getMile() {
        return mile;
    }
    public void setMile(double mile) {
        this.mile = mile;
    }
    public double getTime() {
        return time;
    }
    public void setTime(double time) {
        this.time = time;
    }
    public double getIcecream() {
        return icecream;
    }
    public void setIcecream(double icecream) {
        this.icecream = icecream;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     *  这里进行倒排序
     */
    public int compareTo(Data o) {
        if (this.distance < o.getDistance()) {
            return -1;
        }else if (this.distance  > o.getDistance()) {
            return 1;
        }
        return 0;
    }
}
