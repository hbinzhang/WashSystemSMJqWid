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
    
    public static final String EXCEL_CONTS_STATIC_itemname =  "指标名称";
    public static final String EXCEL_CONTS_STATIC_decuderesult =  "推演结果";
    public static final String EXCEL_CONTS_STATIC_itemunit =  "指标单位";
     
    public static final String JSON_CONTS_STATIC_comment = "comment";
    public static final String JSON_CONTS_STATIC_ratio = "ratio";
    public static final String JSON_CONTS_STATIC_data = "data";
    public static final String JSON_CONTS_STATIC_unit = "unit";
    public static final String JSON_CONTS_STATIC_source ="source";
     
	public static final String FILE_CONTS_CALC_DATA = "calcData.json";
	public static final String FILE_CONTS_CALC_DATA_XLS = "calcData.xls";
	public static final String FILE_CONTS_CALC_STATIC_DATA_XLS = "staticResult.xls";
	public static final String FILE_CONTS_CALC_EXPORT_NAME_XLS = "ShuJuDaoChu";
	
	public static final String JSON_GET_STATIONS_KEY ="name";

	public static final String EXT_NAME_XLS =".xls";
	

	public static final int ORI_INDEX_STATION_SCALE = 1;
	public static final int ORI_INDEX_GROUP_CHUAN_VOLUME = 2;
	public static final int ORI_INDEX_BEI_RATE = 3;
	public static final int ORI_INDEX_CAL_INDEX = 4;// 折算系数
	public static final int ORI_INDEX_ELEC_PRICE = 5;
	public static final int ORI_INDEX_WASH_PRICE = 6;
	public static final int ORI_INDEX_FIRST_BLANK_LINE = 7;
	public static final int ORI_INDEX_SECOND_BLANK_LINE = 9;
	public static final int ORI_INDEX_FIRST_DATA_LINE = 10;

	public static final int STATIC_INDEX_STATION_NAME = 0;
	public static final int STATIC_INDEX_STATION_SCALE = 1;
	public static final int STATIC_INDEX_GROUP_CHUAN_VOLUME = 2;
	public static final int STATIC_INDEX_START_TIME = 3;
	public static final int STATIC_INDEX_END_TIME = 4;
	public static final int STATIC_INDEX_CURRENT_DAY_SHOULD_PRODUCE_ELEC_AMOUT = 5;
	public static final int STATIC_INDEX_CURRENT_DAY_ACTUAL_PROCUDE_ELEC_AMOUT = 6;
	public static final int STATIC_INDEX_CURRENT_DAY_LOST_ELEC_AMOUT = 7;
	public static final int STATIC_INDEX_SUM_LOST_ELEC_AMOUT = 8;
	public static final int STATIC_INDEX_SUM_LOST_ELEC_AMOUT_PRICE = 9;
	public static final int STATIC_INDEX_BEST_WASH_PERIOD = 10;
	public static final int STATIC_INDEX_YEAS_BEST_WASH_PRICE = 11;
	public static final int STATIC_INDEX_YEAR_BEST_LOST_ELEC_AMOUT_PRICE = 12;
	public static final int STATIC_INDEX_YEAR_LOWEST_SUM_PRICE = 13;
	public static final int STATIC_INDEX_SUGGESTED_NEXT_WATH_START_DAY = 14;
	
	public static final int DEDUCE_INDEX_START_TIME = 3;
	public static final int DEDUCE_INDEX_END_TIME = 4;
	public static final int DEDUCE_INDEX_PLAN_WASH_PERIOD = 12;
	public static final int DEDUCE_INDEX_YEAR_PLAN_WASH_PRICE = 13;
	public static final int DEDUCE_INDEX_YEAR_PLAN_LOST_ELEC_PRICE = 14;
	public static final int DEDUCE_INDEX_YEAR_PLAN_SUM_PRICE = 15;
	public static final int DEDUCE_INDEX_DEDUCE_PERIOD_LOST_PRICE = 16;
	
}