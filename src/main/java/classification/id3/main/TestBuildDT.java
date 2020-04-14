package main;


import classification.id3.core.BuildDT;
import classification.id3.core.InitWekaDataSet;
import classification.id3.dao.InitDataSetDao;
import classification.id3.util.XMLUtil;

/**
 *
 * 测试建模
 * @author C_son
 *
 */
public class TestBuildDT {

	public static void main(String[] args) {
		InitDataSetDao initDataSet = new InitWekaDataSet("D:\\weka\\Weka-3-7\\data\\weather.nominal.arff");
		new BuildDT(initDataSet);
		XMLUtil.write2XML("Z:\\A.xml");
	}

}
