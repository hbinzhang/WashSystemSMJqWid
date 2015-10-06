package com.hry.dispatch.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.hry.dispatch.domain.Message;
import com.hry.dispatch.domain.Station;
import com.hry.dispatch.util.*;

import jxl.Cell;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

@Service("dataService")
public class DataServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataServiceImpl.class);

	public void uploadExcel(String excelPath, String outputJsonPath, String username) 
			throws BiffException, IOException , Exception {
		LOGGER.info("[uploadExcel] excelPath is: " + excelPath + " outputJsonPath is: " + outputJsonPath);
		List list = readExcel(excelPath);
		if (list == null || list.size() == 0) {
			throw new IOException("excel is null");
		}
		String[] headers = (String[])list.get(0);
		int dateInd = getIndex(headers, Constants.EXCEL_CONTS_DATE);
		int amoutInd = getIndex(headers, Constants.EXCEL_CONTS_WASH_AMOUT);
		int indexInd = getIndex(headers, Constants.EXCEL_CONTS_CALC_INDEX);
		if (dateInd == -1) {
			throw new IOException("DATE not exist");
		}
		if (amoutInd == -1) {
			throw new IOException("amout not exist");
		}
		if (indexInd == -1) {
			throw new IOException("index not exist");
		}
		List<Map<String, Object>> jsonObj = new ArrayList<Map<String, Object>>();
		for (int i = 1; i < list.size(); i++) {
			String[] dd = (String[])list.get(i);
			Map<String, Object> oneLine = new HashMap<String, Object>();
			oneLine.put(Constants.JSON_CONTS_DATE, dd[dateInd]);
			oneLine.put(Constants.JSON_CONTS_WASH_AMOUT, dd[amoutInd]);
			oneLine.put(Constants.JSON_CONTS_CALC_INDEX, dd[indexInd]);
			jsonObj.add(oneLine);
		}
		// change to json and write to file
		Map paraMap = new HashMap();
		paraMap.put("data", jsonObj);
		Map retParaMap = calcAllLine(paraMap);
		saveJson(retParaMap, outputJsonPath, username);
        LOGGER.info("[uploadExcel] write json end");
	}
	
	public int getIndex(String[] headers, String cont) {
		if (headers == null || headers.length == 0) {
			return -1;
		}
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].trim().equals(cont)) {
				return i;
			}
		}
		return -1;
	}
	
	public void saveJson(Map<String, ? extends Object> paraMap, String path, String username) 
			throws BiffException, IOException, Exception {
		Collection data = (Collection) paraMap.get("data");
		String appBseDir = System.getProperty("app.base.dir");
		ObjectMapper objectMapper = null;
		objectMapper = new ObjectMapper();
		String jsonstr = null;
		// In case of one user handle data on two different terminals at the same time
		Lock l = LockCache.acquire(username);
		synchronized(l) {
			FileOutputStream fos = new FileOutputStream(path);
			try {
			objectMapper.writeValue(fos,
					data);
			jsonstr = objectMapper.writeValueAsString(data);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				fos.close();
			}
		}
		LOGGER.debug("[saveJson] write json end: " + jsonstr);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Map calcAllLine(Map<String, ? extends Object> paraMap) {
		List data = (List) paraMap.get("data");
		LOGGER.debug("[calcAllLine] data: " + data);
		Map ret = new HashMap();
		List dataList = new ArrayList();
		if (data == null || data.size() == 0) {
			ret.put("message", new Message("-1", "错误", "数据为空！"));
			return ret;
		}
		int len = data.size();
		for (int i = 0; i < len; i++) {
			Map calcResult = new HashMap();
			Map line = null;
			Map lastRec = null;
			if (i > 0) {
				lastRec = (Map)data.get(i - 1);
			}
			line = (Map)data.get(i);
			String value = null;
			calcResult.put("date", String.format("%.6f", (getDoubleFromMap(line, "date"))));
			calcResult.put("wash_elec_amout", String.format("%.6f", (getDoubleFromMap(line, "wash_elec_amout"))));
			calcResult.put("cal_rate_index", String.format("%.6f", (getDoubleFromMap(line, "cal_rate_index"))));
			// sumary
			if (lastRec != null) {
				value = String.format("%.6f", (getDoubleFromMap(line, "wash_elec_amout") + getDoubleFromMap(lastRec, "sumary")));
				calcResult.put("sumary", value);
				line.put("sumary", value);
			} else {
				value = String.format("%.6f", getDoubleFromMap(line, "wash_elec_amout"));
				calcResult.put("sumary", value);  
				line.put("sumary", value);
			}
			
			// cal_rate_index_sumary
			value = String.format("%.6f", Math.pow(getDoubleFromMap(line, "cal_rate_index"), getDoubleFromMap(line, "date")));
			calcResult.put("cal_rate_index_sumary", value);  
			line.put("cal_rate_index_sumary", value);
			// no_wash_elec_amout
			value = String.format("%.6f", getDoubleFromMap(line, "wash_elec_amout") * getDoubleFromMap(line, "cal_rate_index_sumary"));
			calcResult.put("no_wash_elec_amout", value);  
			line.put("no_wash_elec_amout", value);
			// no_wash_elec_sumary
			if (lastRec != null) {
				value = String.format("%.6f", getDoubleFromMap(line, "no_wash_elec_amout") + getDoubleFromMap(lastRec, "no_wash_elec_sumary"));
			 	calcResult.put("no_wash_elec_sumary", value);
			 	line.put("no_wash_elec_sumary", String.format("%.6f", getDoubleFromMap(line, "no_wash_elec_amout") + getDoubleFromMap(lastRec, "no_wash_elec_sumary")));
			} else {
				value = String.format("%.6f", getDoubleFromMap(line, "no_wash_elec_amout"));
				calcResult.put("no_wash_elec_sumary", value);
				line.put("no_wash_elec_sumary", value);
			}
			
			// cal_rate_sumary
			value = String.format("%.6f", getDoubleFromMap(line, "no_wash_elec_sumary") / getDoubleFromMap(line, "sumary"));
			calcResult.put("cal_rate_sumary", value);
			line.put("cal_rate_sumary", value);
			// cal_rate
			value = String.format("%.6f", 1 - getDoubleFromMap(line, "date") * 0.0004);
			calcResult.put("cal_rate", value);
			line.put("cal_rate", value);
			// no_wash_elec_amout_2
			value = String.format("%.6f", getDoubleFromMap(line, "cal_rate") * getDoubleFromMap(line, "wash_elec_amout"));
			calcResult.put("no_wash_elec_amout_2", value);
			line.put("no_wash_elec_amout_2", value);
			// sumary_elec_amout
			if (lastRec != null) {
				value = String.format("%.6f", getDoubleFromMap(line, "no_wash_elec_amout_2") + getDoubleFromMap(lastRec, "sumary_elec_amout"));
			 	calcResult.put("sumary_elec_amout", value);
			 	line.put("sumary_elec_amout", String.format("%.6f", getDoubleFromMap(line, "no_wash_elec_amout_2") + getDoubleFromMap(lastRec, "sumary_elec_amout")));
			} else {
				value = String.format("%.6f", getDoubleFromMap(line, "no_wash_elec_amout_2"));
				calcResult.put("sumary_elec_amout", value);
				line.put("sumary_elec_amout", value);
			}
			
			// sumary_cal_rate
			value = String.format("%.6f", getDoubleFromMap(line, "sumary_elec_amout") / getDoubleFromMap(line, "sumary"));
			calcResult.put("sumary_cal_rate", value);
			line.put("sumary_cal_rate", value);
			// reduce_ratio
			value = String.format("%.6f", (getDoubleFromMap(line, "sumary") - getDoubleFromMap(line, "sumary_elec_amout")) * 2 / getDoubleFromMap(line, "sumary") / (getDoubleFromMap(line, "date") + 1));
			calcResult.put("reduce_ratio", value);
			line.put("reduce_ratio", value);
			// lose_sumary
			value = String.format("%.6f", getDoubleFromMap(line, "sumary") - getDoubleFromMap(line, "no_wash_elec_sumary") * 50000 / 6 / 10000);
			calcResult.put("lose_sumary", value);
			line.put("lose_sumary", value);
			dataList.add(calcResult);
		}
		ret.put("data", dataList);
		return ret;
	}
	
	public double getDoubleFromMap(Map map, String key) {
		if (map.containsKey(key)) {
			return Double.parseDouble(map.get(key).toString());
		} else {
			return 0.0;
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List getStations(String uName) {
		LOGGER.warn("[getStations] uName is: " + uName);
		String appBseDir = System.getProperty("app.base.dir");
		String userInfoFile = appBseDir + File.separator + 
 				Constants.USER_INFO_DIR + File.separator + uName + File.separator + Constants.ORI_DATA_DIR;
		File f = new File(userInfoFile);
		List allMap = new ArrayList();
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files == null || files.length <= 0) {
				LOGGER.warn("[getStations] no stations here");
				return allMap;
			}
			for (String file : files) {
				file = file.substring(0, file.indexOf("."));
				allMap.add(new Station( file));
			}
			return allMap;
		} else {
			LOGGER.warn("[getStations] ORI_DATA_DIR not exist");
			return allMap;
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List getStationData(String uName, String sName) {
		LOGGER.warn("[getStationData] uName is: " + uName + "\tsName: " + sName);
		String appBseDir = System.getProperty("app.base.dir");
		String userInfoFile = appBseDir + File.separator + 
 				Constants.USER_INFO_DIR + File.separator + uName + File.separator + Constants.ORI_DATA_DIR;
		File f = new File(userInfoFile);
		List allMap = new ArrayList();
		if (f.isDirectory()) {
			String fPath = userInfoFile + File.separator + sName + Constants.EXT_NAME_XLS;
			try {
				List cont = readExcel(fPath);
				if (cont == null || cont.size() == 0) {
					LOGGER.warn("[getStationData] cont is null");
					return allMap;
				}
				for (String[] line : (List<String[]>)cont) {
					Map t = new HashMap();
					int si = line.length;
					if (si > 0) {
						t.put("item_1", line[0]);
					}
					if (si > 1) {
						t.put("item_2", line[1]);
					}
					if (si > 2) {
						t.put("item_3", line[2]);
					}
					allMap.add(t);
				}
			} catch (Exception e) {
				LOGGER.error("[getStationData] error",  e);
			}
			return allMap;
		} else {
			LOGGER.warn("[getStationData] ORI_DATA_DIR not exist");
			return allMap;
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List calcReportData(Map<String, ? extends Object> paraMap, 
			String sName, String uName) {
		String appBseDir = System.getProperty("app.base.dir");
		String userInfoFile = appBseDir + File.separator + 
 				Constants.USER_INFO_DIR + File.separator + uName + File.separator + Constants.ORI_DATA_DIR;
		File f = new File(userInfoFile);
		List allList = new ArrayList();
		if (f.isDirectory()) {
			String fPath = userInfoFile + File.separator + sName + Constants.EXT_NAME_XLS;
			try {
				List cont = readExcel(fPath);
				if (cont == null || cont.size() == 0) {
					LOGGER.warn("[calcReportData] cont is null");
					return allList;
				}
				List reqDataList = (List)paraMap.get("data");
				Map stMap = (Map)reqDataList.get(2);
				Map etMap = (Map)reqDataList.get(3);
				String st = stMap.get("item_2").toString();
				String et = stMap.get("item_2").toString();
				int stIndex = -1;
				int etIndex = -1;
				int ind = 0;
				for (String[] line : (List<String[]>)cont) {
					Map t = new HashMap();
					int si = line.length;
					if (si > 0) {
						if ( line[0].trim().equals(st) ) {
							stIndex = ind;
						}
						if ( line[0].trim().equals(et) ) {
							etIndex = ind;
						}
						t.put("item_1", line[0]);
					}
					if (si > 1) {
						t.put("item_2", line[1]);
					}
					if (si > 2) {
						t.put("item_3", line[2]);
					}
					allList.add(t);
					ind++;
				}
				LOGGER.info("[calcReportData] stIndex: " + stIndex + "\tetIndex: " + etIndex);
				if (stIndex < 0) {
					stIndex = 9;// 起始 行
				}
				double beiRate = getStaticDataFromMap(cont, 2, 1);
				double calcIndex = getStaticDataFromMap(cont, 3, 1);
				double chuanNum = getStaticDataFromMap(cont, 1, 1);
				double elecPrice = getStaticDataFromMap(cont, 4, 1);
				double onceWashCost = getStaticDataFromMap(cont, 5, 1);
				
				Map dayShouldAmoutMap = (Map)reqDataList.get(4);
				double selecedDayWashData = getStaticDataFromMap(cont, etIndex, 1);
				double selecedLastDayWashData = getStaticDataFromMap(cont, etIndex-1, 1);
				double dayShouldAmout = (selecedDayWashData - selecedLastDayWashData) * beiRate * chuanNum;
				dayShouldAmoutMap.put("item_2", String.format("%.6f", dayShouldAmout));
				
				Map dayActualAmoutMap = (Map)reqDataList.get(5);
				double selecedDaySampleData = getStaticDataFromMap(cont, etIndex, 2);
				double selecedLastDaySampleData = getStaticDataFromMap(cont, etIndex-1, 2);
				double dayActualAmout = (selecedDaySampleData - selecedLastDaySampleData) * beiRate * chuanNum;
				dayActualAmoutMap.put("item_2", String.format("%.6f", dayActualAmout));
				
				Map dayLostAmoutMap = (Map)reqDataList.get(6);
				double dayLostAmout = (dayShouldAmout - dayActualAmout);
				dayLostAmoutMap.put("item_2", String.format("%.6f", dayLostAmout));
				
				Map sumLostAmoutMap = (Map)reqDataList.get(7);
				double startDayWashData = getStaticDataFromMap(cont, stIndex, 1);
				double sumLostAmout = (selecedDayWashData - startDayWashData - selecedDaySampleData + selecedLastDaySampleData) * beiRate * chuanNum;
				sumLostAmoutMap.put("item_2", String.format("%.6f", sumLostAmout));
				
				Map sumLostAmoutCostMap = (Map)reqDataList.get(8);
				double sumLostAmoutCost = sumLostAmout * elecPrice;
				sumLostAmoutCostMap.put("item_2", String.format("%.6f", sumLostAmoutCost));
				
				// calc day
				double firstValueModelBoard = 0;
				double currentValueModelBoard = 0;
				double dayCount = 0;
				double firstValueJudgeBoard = 0;
				double currentValueJudgeBoard = 0;
				double costCheckPeriod = 365;
				double dayAvgProcudeElecAmout = 0;
				double dayFallIndex = 0;
				double theoryWashPeriod = 0;
				double theoryWashTimeYear = 0;
				double washPeriodLostAmout = 0;
				double judgePeriodLostAmout = 0;
				double judgePeriodLostCost = 0;
				double theoryYearWashCost = 0;
				double theoryTotalCost = 0;
				
				
				Map bestWashPeriodMap = (Map)reqDataList.get(9);
				bestWashPeriodMap.put("item_2", String.format("%.1f", theoryWashPeriod));
				
				Map yearBestWashCostMap = (Map)reqDataList.get(10);
				yearBestWashCostMap.put("item_2", String.format("%.6f", theoryYearWashCost));
				
				Map yearBestLostAmoutCostMap = (Map)reqDataList.get(11);
				yearBestLostAmoutCostMap.put("item_2", String.format("%.6f", judgePeriodLostCost));
				
				Map yearLowestSumCostMap = (Map)reqDataList.get(12);
				double yearLowestSumCost = theoryYearWashCost + judgePeriodLostCost;
				yearLowestSumCostMap.put("item_2", String.format("%.6f", yearLowestSumCost));
				
				Map suggestedNextWashDayMap = (Map)reqDataList.get(13);
				Date startDate = TimeConvertor.stringTime2Date(st, TimeConvertor.FORMAT_SLASH_DAY);
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.add(Calendar.DATE, (int)theoryWashPeriod);
				String nextWashDate = TimeConvertor.date2String(cal.getTime(), TimeConvertor.FORMAT_SLASH_DAY);
				suggestedNextWashDayMap.put("item_2", nextWashDate);
				
				LOGGER.info("[calcReportData] result: " + reqDataList);
				return reqDataList;
			} catch (Exception e) {
				LOGGER.error("[calcReportData] error",  e);
			}
			return allList;
		} else {
			LOGGER.warn("[calcReportData] ORI_DATA_DIR not exist");
			return allList;
		}
	}
	
	public double getStaticDataFromMap (List oriData, int line, int col)
	throws Exception {
		try {
			String[] one = (String[])oriData.get(line);
			String v = one[col];
			double d = Double.parseDouble(v);
			return d;
		} catch (Exception e) {
			LOGGER.error("[getStaticDataFromMap] error line is:" + line + "\tcol" + col,  e);
			throw new Exception("原始数据不正确，行号：" + (line+1));
		}
	}
	
	public void saveXls(Map<String, ? extends Object> paraMap, String uname) throws Exception {
		String appBseDir = System.getProperty("app.base.dir");
		LOGGER.info("[saveXls] start appBseDir " + appBseDir);
		List<List<String>> data = new ArrayList<List<String>>();
		List<String> heads = new ArrayList<String>();
		heads.add("日期");
		heads.add("清洗电量");
		heads.add("累计");
		heads.add("不清洗电量");
		heads.add("不清洗累计");
		heads.add("折算率指数（累计）");
		heads.add("折算率指数");
		heads.add("累计折算率");
		heads.add("折算率");
		heads.add("不清洗电量");
		heads.add("累计电量");
		heads.add("累计折算率");
		heads.add("反推日降系数");
		heads.add("损失累计");
		data.add(heads);
		
		Collection dataJ = (Collection) paraMap.get("data");
		Iterator dataIt = dataJ.iterator();
		while (dataIt.hasNext()) {
			Map line = (Map)dataIt.next();
			List<String> one = new ArrayList<String>();
			one.add(getStr(line, Constants.JSON_CONTS_DATE));
			one.add(getStr(line, Constants.JSON_CONTS_WASH_AMOUT));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_sumary));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_no_wash_elec_amout));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_no_wash_elec_sumary));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_cal_rate_index_sumary));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_INDEX));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_cal_rate_sumary));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_cal_rate));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_no_wash_elec_amout_2));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_sumary_elec_amout));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_sumary_cal_rate));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_reduce_ratio));
			one.add(getStr(line, Constants.JSON_CONTS_CALC_lose_sumary));
			data.add(one);
		}
		// In case of one user handle data on two different terminals at the same time
		Lock l = LockCache.acquire(uname);
		synchronized(l) {
			writeExcel(data, appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + uname + File.separator +
				Constants.FILE_CONTS_CALC_DATA_XLS);
		}
	}
	
	public void saveStaticXls(Map<String, ? extends Object> paraMap, String uname)
	throws Exception {
		String appBseDir = System.getProperty("app.base.dir");
		LOGGER.info("[saveStaticXls] start appBseDir " + appBseDir);
		List<List<String>> data = new ArrayList<List<String>>();
		List<String> heads = new ArrayList<String>();
		heads.add(Constants.EXCEL_CONTS_STATIC_itemname);
		heads.add(Constants.EXCEL_CONTS_STATIC_decuderesult);
		heads.add(Constants.EXCEL_CONTS_STATIC_itemunit);
		data.add(heads);
		
		Collection dataJ = (Collection) paraMap.get("data");
		Iterator dataIt = dataJ.iterator();
		while (dataIt.hasNext()) {
			Map line = (Map)dataIt.next();
			List<String> one = new ArrayList<String>();
			one.add(getStr(line, "item_1"));
			one.add(getStr(line, "item_2"));
			one.add(getStr(line, "item_3"));
			data.add(one);
		}
		// In case of one user handle data on two different terminals at the same time
		Lock l = LockCache.acquire(uname);
		synchronized(l) {
			writeExcel(data, appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + uname + File.separator +
				Constants.FILE_CONTS_CALC_STATIC_DATA_XLS);
		}
	}
	
	public String getStr(Map pa, String key) {
		Object o = pa.get(key);
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	public static List readExcel(String excelPath) throws BiffException, IOException {
		// 创建一个list 用来存储读取的内容
		List list = new ArrayList();
		Workbook rwb = null;
		Cell cell = null;
		NumberCell ncell = null;
		DateCell dcell = null;
		
		// 创建输入流
		InputStream stream = null;
		try {
		stream = new FileInputStream(excelPath);

		// 获取Excel文件对象
		rwb = Workbook.getWorkbook(stream);

		// 获取文件的指定工作表 默认的第一个
		Sheet sheet = rwb.getSheet(0);

		// 行数(表头的目录不需要，从1开始)
		for (int i = 0; i < sheet.getRows(); i++) {

			// 创建一个数组 用来存储每一列的值
			String[] str = new String[sheet.getColumns()];

			// 列数
			for (int j = 0; j < sheet.getColumns(); j++) {
				cell = sheet.getCell(j, i);
				if (cell instanceof NumberCell) {
					ncell = (NumberCell)cell;
					str[j] = String.valueOf(ncell.getValue());
				} else if (cell instanceof DateCell) {
					dcell = (DateCell)cell;
					str[j] = TimeConvertor.date2String(dcell.getDate(), TimeConvertor.FORMAT_SLASH_DAY);
				} else {
					str[j] = cell.getContents();
				}
				
//				System.out.print(str[j] + "\t");
			}
//			System.out.println("");
			// 把刚获取的列存入list
			list.add(str);
			
		}
		return list;
		} catch (Exception e) {
			LOGGER.error("[readExcel] error " + excelPath, e);
			throw e;
		} finally {
			stream.close();
		}
		// for (int i = 0; i < list.size(); i++) {
		// String[] str = (String[]) list.get(i);
		// for (int j = 0; j < str.length; j++) {
		// System.out.println(str[j]);
		// }
		// }
	}

	public static void writeExcel(List<List<String>> data, String path) {
		OutputStream os = null;
		try {
			LOGGER.debug("[writeExcel] start");
			// 获得开始时间
			long start = System.currentTimeMillis();
			// 输出的excel的路径
			String filePath = path;
			// 创建Excel工作薄
			WritableWorkbook wwb;
			// 新建立一个jxl文件,即在d盘下生成testJXL.xls
			os = new FileOutputStream(filePath);
			wwb = Workbook.createWorkbook(os);
			// 添加第一个工作表并设置第一个Sheet的名字
			WritableSheet sheet = wwb.createSheet("清洗数据", 0);
			// 下面是填充数据
			int rowNum = 0;
			for (List<String> line : data) {
				int colNum = 0;
				for (String one : line) {
					// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
					// 在Label对象的子对象中指明单元格的位置和内容
					Label label = new Label(colNum, rowNum, one);
					// 将定义好的单元格添加到工作表中
					sheet.addCell(label);
					colNum++;
				}
				rowNum++;
			}
			/*
			 * 保存数字到单元格，需要使用jxl.write.Number 必须使用其完整路径，否则会出现错误
			 */
			// 填充产品编号
//			jxl.write.Number number = new jxl.write.Number(0, 1, 20071001);
//			sheet.addCell(number);
//			// 填充产品名称
//			label = new Label(1, 1, "金鸽瓜子");
//			sheet.addCell(label);
//			/*
//			 * 定义对于显示金额的公共格式 jxl会自动实现四舍五入 例如 2.456会被格式化为2.46,2.454会被格式化为2.45
//			 */
//			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#,###.00");
//			jxl.write.WritableCellFormat wcf = new jxl.write.WritableCellFormat(nf);
//			// 填充产品价格
//			jxl.write.Number nb = new jxl.write.Number(2, 1, 200000.45, wcf);
//			sheet.addCell(nb);
//			// 填充产品数量
//			jxl.write.Number numb = new jxl.write.Number(3, 1, 200);
//			sheet.addCell(numb);
//			/*
//			 * 定义显示日期的公共格式 如:yyyy-MM-dd hh:mm
//			 */
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			String newdate = sdf.format(new Date());
//			// 填充出产日期
//			label = new Label(4, 1, newdate);
//			sheet.addCell(label);
//			// 填充产地
//			label = new Label(5, 1, "陕西西安");
//			sheet.addCell(label);
//			/*
//			 * 显示布尔值
//			 */
//			jxl.write.Boolean bool = new jxl.write.Boolean(6, 1, true);
//			sheet.addCell(bool);
//			/*
//			 * 合并单元格 通过writablesheet.mergeCells(int x,int y,int m,int n);来实现的
//			 * 表示将从第x+1列，y+1行到m+1列，n+1行合并
//			 * 
//			 */
//			sheet.mergeCells(0, 3, 2, 3);
//			label = new Label(0, 3, "合并了三个单元格");
//			sheet.addCell(label);
//			/*
//			 * 
//			 * 定义公共字体格式 通过获取一个字体的样式来作为模板 首先通过web.getSheet(0)获得第一个sheet
//			 * 然后取得第一个sheet的第二列，第一行也就是"产品名称"的字体
//			 */
//			CellFormat cf = wwb.getSheet(0).getCell(1, 0).getCellFormat();
//			WritableCellFormat wc = new WritableCellFormat();
//			// 设置居中
//			wc.setAlignment(Alignment.CENTRE);
//			// 设置边框线
//			wc.setBorder(Border.ALL, BorderLineStyle.THIN);
//			// 设置单元格的背景颜色
//			wc.setBackground(jxl.format.Colour.RED);
//			label = new Label(1, 5, "字体", wc);
//			sheet.addCell(label);
//
//			// 设置字体
//			jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont.createFont("隶书"), 20);
//			WritableCellFormat font = new WritableCellFormat(wfont);
//			label = new Label(2, 6, "隶书", font);
//			sheet.addCell(label);

			// 写入数据
			wwb.write();
			// 关闭文件
			wwb.close();
			long end = System.currentTimeMillis();
			LOGGER.debug("[writeExcel] costs: " + (end - start) / 1000);
		} catch (Exception e) {
			LOGGER.error("[writeExcel] error", e);
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				LOGGER.error("[writeExcel] close error", e);
			}
		}
	}

	public static WritableCellFormat getHeader() {
		WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);// 定义字体
		try {
			font.setColour(Colour.BLUE);// 蓝色字体
		} catch (WriteException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);// 黑色边框
			format.setBackground(Colour.YELLOW);// 黄色背景
		} catch (WriteException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return format;
	}
	
	public static void main (String[] s) {
//		DataServiceImpl i = new DataServiceImpl();
////		i.writeExcel();
//		try {
//			List l = i.readExcel("D:\\testJXL.xls");
////			System.out.println(l);
//		} catch (BiffException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String re = String.format("%.6f", 0.892372873d);
		System.out.println(re);
	}

}