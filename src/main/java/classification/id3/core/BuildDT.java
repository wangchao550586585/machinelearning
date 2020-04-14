package classification.id3.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import classification.id3.dao.InitDataSetDao;
import classification.id3.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;


/**
 * 算法的核心类，用于创建决策树
 * 
 * @author C_son
 * 
 */
public class BuildDT {

	private Document document = null;
	private InitDataSetDao initDataSet = null;
	//已经访问过的属性集合,递归循环时，将跳过
	private List<String> skipAttrbuteList = null;

	public BuildDT(InitDataSetDao initDataSet) {
		this.initDataSet = initDataSet;
		skipAttrbuteList = new ArrayList<String>();
		Element element = initXML();
		buildDT(element,initDataSet.getMeteDataSet());
	}
	
	private Element initXML() {
		document = XMLUtil.getDocument();
		return document.addElement("root");
	}

	/**
	 * 这里偷个懒，确定节点时，要计算信息增益需计算原有熵减去确定某个属性时的熵， 取最大值 由于计算式时被减数都一样，所以只需使得减数取最小值即可
	 */
	private void buildDT(Element element, List<String[]> dataSet) {
		// 要创建的节点的名字
		String elementName = "";
		double min = Double.MAX_VALUE;

		// 计算每个属性熵值，选出最小值
		for (int i = 0; i < initDataSet.getAttrbuteNames().size() - 1; i++) {
			
			if (!skipAttrbuteList.contains(initDataSet.getAttrbuteNames().get(i))) {
				String attrbuteName = initDataSet.getAttrbuteNames().get(i);
				double sum = 0.0;
				// 计算每个属性的熵
				sum = calOptionEntropy(attrbuteName,dataSet);

				// 找最小值，确定节点
				if (sum < min) {
					min = sum;
					elementName = attrbuteName;
				}
			}
			
		}
		
		for (String attrbuteOption : initDataSet.getAttrbuteOptions().get(
				elementName)) {
			List<String[]> subDataSet = new ArrayList<String[]>();
			
			//使用set集合的无重复的特性来判断是否是叶节点，如果是叶节点，则set集合长度为一
			Set<String> set = new HashSet<String>();
			
			// 目标属性在数据集中的位置
			int decAttrbuteIndex = getValueIndex(initDataSet.getDestinaltion().get(
					0));
			
			for (String[] data : dataSet) {
				if (data[getValueIndex(elementName)].equals(attrbuteOption)) {
					subDataSet.add(data);
					set.add(data[decAttrbuteIndex]);
				}
			}
			
			// 创建节点
			Element subElement = element.addElement(elementName).addAttribute(	
					"value", attrbuteOption);
			
			//长度大于一则进行递归,否则输出
			if (set.size() > 1) {
				//将刚才生成的节点加入到已访问属性集合中
				skipAttrbuteList.add(elementName);
				//递归调用
				buildDT(subElement,subDataSet);
			} else {
				for (String string : set) {	
					subElement.setText(string);
				}
			}
			
		}
	}


	/**
	 * @param valueName
	 * @return 返回某一个属性在集合中的位置，使得便于读取值
	 */
	private int getValueIndex(String valueName) {
		return initDataSet.getAttrbuteNames().indexOf(valueName);
	}

	/**
	 * 计算每一个属性值的熵值
	 * 
	 * @param attrbuteName
	 *            属性名称
	 * @param subDataSet
	 * 			    已经访问的属性集合,递归时，将跳过该元素
	 * @return 返回一个带权值的部分属性熵
	 */
	private double calOptionEntropy(String attrbuteName,List<String[]> subDataSet) {

		// 目标属性在数据集中的位置
		int decAttrbuteIndex = getValueIndex(initDataSet.getDestinaltion().get(
				0));
		// 当前属性在数据集中的位置
		int nowAttrbuteIndex = getValueIndex(attrbuteName);
		//熵值
		double sum = 0.0;
		
		List<String> options = initDataSet.getAttrbuteOptions().get(attrbuteName);
		
		//遍历每一个属性值，计算熵
		for (String option : options) {
			
			// 当属性在某个值时，目标属性的值出现次数
			Map<String, Integer> map = new HashMap<String, Integer>();
			// 属性在某个值时，共出现次数
			int totalNum = 0;
			
			for (String[] data : subDataSet) {
				if (data[nowAttrbuteIndex].equals(option)) {
					// 从1开始，因为0是目标变量的属性名称
					for (int i = 1; i < initDataSet.getDestinaltion().size(); i++) {
						if (data[decAttrbuteIndex].equals(initDataSet
								.getDestinaltion().get(i))) {
							if (map.get(data[decAttrbuteIndex]) == null) {
								map.put(data[decAttrbuteIndex], 1);
							} else {
								map.put(data[decAttrbuteIndex],
										map.get(data[decAttrbuteIndex]) + 1);
							}
							totalNum++;
						}
					}
				}
			}
			
			sum += calOptionEntropy(map, totalNum);
			
		}
		
		return sum;
		
	}

	private double calOptionEntropy(Map<String, Integer> map, int totalNum) {
		double sum = 0.0;
		for (Entry<String, Integer> entry : map.entrySet()) {
			double d = (double) entry.getValue() / totalNum;
			sum += entropy(d);
		}
		return sum * totalNum / initDataSet.getMeteDataSet().size();
	}

	/**
	 * 根据公式求熵
	 * 
	 * @param d
	 * @return
	 */
	private double entropy(double d) {
		return Math.abs(d * log2(d));
	}

	private double log2(double d) {
		if (d <= 0)
			return 0.0;
		return Math.log(d) / Math.log(2);
	}

}
