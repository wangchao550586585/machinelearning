package classification.id3.core;

import java.util.List;
import java.util.Map;

import classification.id3.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;


/**
 * 根据之前生成的xml预测
 * 
 * @author C_son
 *
 */
public class Predict {
	
	private Document document = null;
	
	public Predict(String filePath) {
		this.document = XMLUtil.readXML(filePath);
	}
	
	@SuppressWarnings("unchecked")
	public String pridictResult(Map<String, String> data) {
		
		Element element = document.getRootElement();
		List<Element> list = element.elements();
		
		while(true) {
			for (Element e : list) {
				if (data.get(e.getName()).equals(e.attributeValue("value"))) {
					list = e.elements();
					if (list.size() == 0) {
						return e.getText();
					}else {
						break;
					}
				}
			}
		}
	}

}
