package classification.id3.dao;

import java.util.List;
import java.util.Map;

/**
 * 此接口规定了要求的数据集提供的数据集合
 * 以便此后扩展其他方式初始化数据集
 * 
 * @author C_son
 *
 */
public interface InitDataSetDao {
	
	/**
	 * @return 返回样本数据集中的每一条数据
	 */
	public List<String[]> getMeteDataSet();
	
	/**
	 * @return 返回数据集中属性的名字
	 */
	public List<String> getAttrbuteNames();
	
	/**
	 * @return map集合的键为每一个属性名称，值为属性值的集合
	 */
	public Map<String,List<String>> getAttrbuteOptions();
	
	/**
	 * @return 得到目标属性的名称与值，第一项为属性名称其余为值
	 */
	public List<String> getDestinaltion();

}
