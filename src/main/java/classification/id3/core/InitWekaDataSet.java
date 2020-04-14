package classification.id3.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classification.id3.dao.InitDataSetDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 数据格式为weka的数据集
 * 
 * @author C_son
 * 
 */
public class InitWekaDataSet implements InitDataSetDao {

	private static final Log log = LogFactory.getLog(InitWekaDataSet.class);

	// 匹配weka的格式
	private static final String ATTR_PARSE_PATTERN = "@attribute(.*)[{](.*?)[}]";

	private List<String> attrbuteNames;
	private List<String[]> metaDataSet;
	private Map<String, List<String>> attrubteOptions;
	private List<String> destination;

	public InitWekaDataSet(String filePath) {
		attrbuteNames = new ArrayList<String>();
		metaDataSet = new ArrayList<String[]>();
		attrubteOptions = new HashMap<String, List<String>>();
		destination = new ArrayList<String>();

		initData(filePath);
	}

	/**
	 * 读取文件
	 * 
	 * @param filePath
	 */
	private void initData(String filePath) {

		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), "utf-8"));
			String line = null;
			Pattern pattern = Pattern.compile(ATTR_PARSE_PATTERN);
			while ((line = bufferedReader.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					String key = matcher.group(1).trim();
					attrbuteNames.add(key.trim());
					String[] values = matcher.group(2).trim().split(",");
					List<String> valuesList = new ArrayList<String>();
					for (String value : values) {
						valuesList.add(value.trim());
					}
					attrubteOptions.put(key, valuesList);
				} else if (line.startsWith("@data")) {
					while ((line = bufferedReader.readLine()) != null) {
						String[] datas = line.split(",");
						metaDataSet.add(datas);
					}
				}
			}
			
			//初始化目标属性集合
			//weka数据集中最后一项为目标属性
			int last = attrbuteNames.size() - 1;
			String option = attrbuteNames.get(last);
			destination.add(option);
			for (String str : attrubteOptions.get(option)) {
				destination.add(str);
			}
			
			log.info("初始化数据集成功");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("文件不存在");
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<String[]> getMeteDataSet() {
		return metaDataSet;
	}

	@Override
	public List<String> getAttrbuteNames() {
		return attrbuteNames;
	}

	@Override
	public Map<String, List<String>> getAttrbuteOptions() {
		return attrubteOptions;
	}

	@Override
	public List<String> getDestinaltion() {
		return destination;
	}

}
