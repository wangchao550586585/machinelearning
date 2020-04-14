package classification.id3.main;

import classification.id3.core.Predict;

import java.util.HashMap;
import java.util.Map;



/**
 * 
 * 测试预测
 * 基于决策树的ID3算法
 * @author C_son
 *
 */
public class TestPredict {

	
	public static void main(String[] args) {
		
		//模拟预测数据，来判断是否可以打球
		Map<String, String> predictData = new HashMap<String, String>();
		predictData.put("outlook", "sunny");
		predictData.put("temperature", "mild");
		predictData.put("humidity", "normal");
		predictData.put("windy", "TRUE");
		
		Predict predict = new Predict("z:\\A.xml");
		String result = predict.pridictResult(predictData);
		System.out.println(result);
	}

}
