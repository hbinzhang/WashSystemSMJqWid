package com.hry.dispatch.util;

import java.io.File;

public class Constants {

	public static final String USER_INFO_DIR = "data" + File.separator + "user";
	public static final String COMPANY_INFO_DIR = "data" + File.separator + "company";
	public static final String EXCEL_INFO_DIR = "data" + File.separator + "excel";
	public static final String JSON_INFO_DIR = "data" + File.separator + "json";
	public static final String TMP_DIR = "tmp";
	public static final String ORI_DATA_DIR = "电站原始数据";
	
	public static final String USER_INFO_FILE = "userinfo.db";
	public static final String USER_INFO_LOGIN_NAME = "登录名";
	
	public static final String SESSION_KEY_USER_INFO = "userinfo";
	
	public static final String EXCEL_CONTS_DATE = "日期";
	public static final String EXCEL_CONTS_WASH_AMOUT = "清洗电量";
	public static final String EXCEL_CONTS_CALC_INDEX = "折算率指数";
	public static final String EXCEL_CONTS_CALC_sumary = "累计";
	public static final String EXCEL_CONTS_CALC_no_wash_elec_amout =  "不清洗电量";
	public static final String EXCEL_CONTS_CALC_no_wash_elec_sumary = "不清洗累计";
	public static final String EXCEL_CONTS_CALC_cal_rate_index_sumary =  "折算率指数（累计）";
	public static final String EXCEL_CONTS_CALC_cal_rate_sumary = "累计折算率";
	public static final String EXCEL_CONTS_CALC_cal_rate =  "折算率";
	public static final String EXCEL_CONTS_CALC_no_wash_elec_amout_2 = "不清洗电量";
	public static final String EXCEL_CONTS_CALC_sumary_elec_amout =  "累计电量";
	public static final String EXCEL_CONTS_CALC_sumary_cal_rate = "累计折算率";
	public static final String EXCEL_CONTS_CALC_reduce_ratio =  "反推日降系数";
	public static final String EXCEL_CONTS_CALC_lose_sumary =  "损失累计";
    	
	public static final String JSON_CONTS_DATE = "date";
	public static final String JSON_CONTS_WASH_AMOUT = "wash_elec_amout";
	public static final String JSON_CONTS_CALC_sumary = "sumary";
    public static final String JSON_CONTS_CALC_no_wash_elec_amout = "no_wash_elec_amout";
	public static final String JSON_CONTS_CALC_no_wash_elec_sumary = "no_wash_elec_sumary";
	public static final String JSON_CONTS_CALC_cal_rate_index_sumary = "cal_rate_index_sumary";
	public static final String JSON_CONTS_CALC_INDEX = "cal_rate_index";
	public static final String JSON_CONTS_CALC_cal_rate_sumary = "cal_rate_sumary";
	public static final String JSON_CONTS_CALC_cal_rate =  "cal_rate";
	public static final String JSON_CONTS_CALC_no_wash_elec_amout_2 = "no_wash_elec_amout_2";
	public static final String JSON_CONTS_CALC_sumary_elec_amout =  "sumary_elec_amout";
	public static final String JSON_CONTS_CALC_sumary_cal_rate = "sumary_cal_rate";
	public static final String JSON_CONTS_CALC_reduce_ratio = "reduce_ratio";
	public static final String JSON_CONTS_CALC_lose_sumary = "lose_sumary"; 
	
	public static final String EXCEL_CONTS_STATIC_comment = "名称";
    public static final String EXCEL_CONTS_STATIC_ratio =  "系数";
    public static final String EXCEL_CONTS_STATIC_data =  "数值";
    public static final String EXCEL_CONTS_STATIC_unit =  "单位";
    public static final String EXCEL_CONTS_STATIC_source =  "参数来源";
     
    public static final String JSON_CONTS_STATIC_comment = "comment";
    public static final String JSON_CONTS_STATIC_ratio = "ratio";
    public static final String JSON_CONTS_STATIC_data = "data";
    public static final String JSON_CONTS_STATIC_unit = "unit";
    public static final String JSON_CONTS_STATIC_source ="source";
     
	public static final String FILE_CONTS_CALC_DATA = "calcData.json";
	public static final String FILE_CONTS_CALC_DATA_XLS = "calcData.xls";
	public static final String FILE_CONTS_CALC_STATIC_DATA_XLS = "staticResult.xls";
	
	public static final String JSON_GET_STATIONS_KEY ="name";

	public static final String EXT_NAME_XLS =".xls";
	
}
