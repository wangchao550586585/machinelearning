package classification.id3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 辅助工具类，用于将最后结果保存到xml
 * 想到用xml，是因为xml本身结构就是一颗树，能够直观的表现
 * @author C_son
 *
 */
public class XMLUtil {
	
	private static final Log log = LogFactory.getLog(XMLUtil.class);
	
	private static Document document = DocumentHelper.createDocument();;
	
	private XMLUtil(){};
	
	public static Document getDocument() {
		return document;
	}
	
	public static void write2XML(String filePath) {
		try {
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file);
            OutputFormat format = OutputFormat.createPrettyPrint(); // 美化格式
            XMLWriter output = new XMLWriter(fw, format);
            output.write(document);
            output.close();
            log.info("写入成功!");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static Document readXML(String filePath) {
		
		InputStream inputStream = null;
		Document doc = null;
		try{
			inputStream = new FileInputStream(filePath);
			//创建SAXReader读取器，专门用于读取xml 
			SAXReader saxReader = new SAXReader();
			//根据saxReader的read重写方法可知，既可以通过inputStream输入流来读取，也可以通过file对象来读取   
			doc = saxReader.read(inputStream);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}

}
