package com.hry.dispatch.util;

import java.io.File;

public class Constants {

	public static final String USER_INFO_DIR = "data" + File.separator + "user";
	public static final String COMPANY_INFO_DIR = "data" + File.separator + "company";
	public static final String EXCEL_INFO_DIR = "data" + File.separator + "excel";
	public static final String JSON_INFO_DIR = "data" + File.separator + "json";
	
	public static final String USER_INFO_FILE = "userinfo.db";
	public static final String USER_INFO_LOGIN_NAME = "登录名";
	
	public static final String SESSION_KEY_USER_INFO = "userinfo";
	
	public static final String EXCEL_CONTS_DATE = "日期";
	public static final String EXCEL_CONTS_WASH_AMOUT = "清洗电量";
	public static final String EXCEL_CONTS_CALC_INDEX = "折算率指数";
	
	public static final String JSON_CONTS_DATE = "date";
	public static final String JSON_CONTS_WASH_AMOUT = "wash_elec_amout";
	public static final String JSON_CONTS_CALC_INDEX = "cal_rate_index";
	
	public static final String FILE_CONTS_CALC_DATA = "calcData.json";
}
