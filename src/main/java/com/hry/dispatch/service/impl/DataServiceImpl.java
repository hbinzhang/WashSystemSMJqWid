package com.hry.dispatch.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hry.dispatch.util.*;

import jxl.Cell;
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

	public void uploadExcel(String excelPath, String outputJsonPath) throws BiffException, IOException {
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
		saveJson(paraMap, outputJsonPath);
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
	
	public void saveJson(Map<String, ? extends Object> paraMap, String uname) 
			throws BiffException, IOException {
		Collection data = (Collection) paraMap.get("data");
		String appBseDir = System.getProperty("app.base.dir");
		ObjectMapper objectMapper = null;
		objectMapper = new ObjectMapper();
		String jsonstr = null;
		// In case of one user handle data on two different terminals at the same time
		synchronized(JsonLock.class) {
			objectMapper.writeValue(new FileOutputStream(appBseDir + File.separator + 
					Constants.USER_INFO_DIR + File.separator + uname + File.separator + Constants.FILE_CONTS_CALC_DATA),
					data);
			jsonstr = objectMapper.writeValueAsString(data);
		}
		LOGGER.debug("[saveJson] write json end: " + jsonstr);
	}
	
	public void saveXls(Map<String, ? extends Object> paraMap, String uname) {
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
		synchronized(XlsLock.class) {
			writeExcel(data, appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + uname + File.separator +
				Constants.FILE_CONTS_CALC_DATA_XLS);
		}
	}
	
	public void saveStaticXls(Map<String, ? extends Object> paraMap, String uname) {
		String appBseDir = System.getProperty("app.base.dir");
		LOGGER.info("[saveStaticXls] start appBseDir " + appBseDir);
		List<List<String>> data = new ArrayList<List<String>>();
		List<String> heads = new ArrayList<String>();
		heads.add(Constants.EXCEL_CONTS_STATIC_comment);
		heads.add(Constants.EXCEL_CONTS_STATIC_ratio);
		heads.add(Constants.EXCEL_CONTS_STATIC_data);
		heads.add(Constants.EXCEL_CONTS_STATIC_unit);
		heads.add(Constants.EXCEL_CONTS_STATIC_source);
		data.add(heads);
		
		Collection dataJ = (Collection) paraMap.get("data");
		Iterator dataIt = dataJ.iterator();
		while (dataIt.hasNext()) {
			Map line = (Map)dataIt.next();
			List<String> one = new ArrayList<String>();
			one.add(getStr(line, Constants.JSON_CONTS_STATIC_comment));
			one.add(getStr(line, Constants.JSON_CONTS_STATIC_ratio));
			one.add(getStr(line, Constants.JSON_CONTS_STATIC_data));
			one.add(getStr(line, Constants.JSON_CONTS_STATIC_unit));
			one.add(getStr(line, Constants.JSON_CONTS_STATIC_source));
			data.add(one);
		}
		// In case of one user handle data on two different terminals at the same time
		synchronized(DataServiceImpl.class) {
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

		// 创建输入流
		InputStream stream = new FileInputStream(excelPath);

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

				// 获取第i行，第j列的值
				cell = sheet.getCell(j, i);
				str[j] = cell.getContents();

			}
			// 把刚获取的列存入list
			list.add(str);
		}
		return list;
		// for (int i = 0; i < list.size(); i++) {
		// String[] str = (String[]) list.get(i);
		// for (int j = 0; j < str.length; j++) {
		// System.out.println(str[j]);
		// }
		// }
	}

	public static void writeExcel(List<List<String>> data, String path) {
		try {
			LOGGER.debug("[writeExcel] start");
			// 获得开始时间
			long start = System.currentTimeMillis();
			// 输出的excel的路径
			String filePath = path;
			// 创建Excel工作薄
			WritableWorkbook wwb;
			// 新建立一个jxl文件,即在d盘下生成testJXL.xls
			OutputStream os = new FileOutputStream(filePath);
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
		DataServiceImpl i = new DataServiceImpl();
//		i.writeExcel();
		try {
			List l = i.readExcel("D:\\testJXL.xls");
			System.out.println(l);
		} catch (BiffException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}