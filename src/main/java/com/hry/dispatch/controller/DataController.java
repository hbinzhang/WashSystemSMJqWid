package com.hry.dispatch.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hry.dispatch.domain.Message;
import com.hry.dispatch.domain.User;
import com.hry.dispatch.service.UserServiceI;
import com.hry.dispatch.service.impl.DataServiceImpl;
import com.hry.dispatch.util.Constants;
import com.hry.dispatch.util.TimeConvertor;

@Controller
public class DataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private DataServiceImpl dataService;

	@RequestMapping(value = "/data/dataupload")
	public String upload(@RequestParam(value = "fname", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {
		LOGGER.info("[upload] start");
		try {
			String appBseDir = System.getProperty("app.base.dir");
			User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
			if (u == null) {
				LOGGER.info("[upload] user is null");
				return "calcUpLoadError";
			}
			String fileName = file.getOriginalFilename();
			String tmpDir = appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + u.getUserName() + File.separator + Constants.TMP_DIR;
			File targetFile = new File(tmpDir, fileName);
			// LOGGER.info("[upload] path: " + path);
			LOGGER.info("[upload] fileName: " + fileName);
			if (targetFile.exists()) {
				targetFile.delete();
			}
			try {
				file.transferTo(targetFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);
	 		String userInfoFile = appBseDir + File.separator + 
	 				Constants.USER_INFO_DIR + File.separator + u.getUserName() + File.separator + Constants.FILE_CONTS_CALC_DATA;
			dataService.uploadExcel(tmpDir + File.separator + fileName, userInfoFile, u.getUserName());
			return "calc";
		} catch (Exception e) {
			LOGGER.error("[upload] error", e);
			return "calcUpLoadError";
		}
	}

	@RequestMapping(value = "/data/save", method=RequestMethod.POST, 
			headers = {"content-type=application/json","content-type=application/xml"})
	public @ResponseBody Message save(@RequestBody Map<String, ? extends Object> paraMap, HttpServletRequest request) {
		LOGGER.info("[save] start paraMap is: " + paraMap);
		// save to user/<username>/calcData.json
		User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
		if (u == null) {
			LOGGER.info("[save] user is null");
			return new Message("-1", "错误", "未登录，请从新登陆！");
		}
		try {
			String appBseDir = System.getProperty("app.base.dir");
			String userInfoFile = appBseDir + File.separator + 
	 				Constants.USER_INFO_DIR + File.separator + u.getUserName() + File.separator + Constants.FILE_CONTS_CALC_DATA;
			dataService.saveJson(paraMap, userInfoFile, u.getUserName());
		} catch (Exception e) {
			LOGGER.error("[save] save error", e);
			return new Message("-1", "错误", "服务器异常！");
		}
		Message ret = new Message("1", "成功", "成功");
		LOGGER.info("[save] return: " + ret);
		return ret;
	}
	
	@RequestMapping(value = "/data/calcAllLine", method=RequestMethod.POST, 
			headers = {"content-type=application/json","content-type=application/xml"})
	public @ResponseBody Message calcAllLine(@RequestBody Map<String, ? extends Object> paraMap, HttpServletRequest request) {
		LOGGER.info("[save] start paraMap is: " + paraMap);
		// save to user/<username>/calcData.json
		User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
		Map ret = new HashMap();
		if (u == null) {
			LOGGER.info("[calcAllLine] user is null");
			return new Message("-1", "错误", "用户未登陆！");
		}
		try {
		ret = dataService.calcAllLine(paraMap);
		String appBseDir = System.getProperty("app.base.dir");
		String userInfoFile = appBseDir + File.separator + 
 				Constants.USER_INFO_DIR + File.separator + u.getUserName() + File.separator + Constants.FILE_CONTS_CALC_DATA;
		dataService.saveJson(ret, userInfoFile,u.getUserName());
		} catch (Exception e) {
				LOGGER.error("[calcAllLine] save error", e);
				return new Message("-1", "错误", "服务器异常！");
		}
		LOGGER.info("[calcAllLine] return: " + ret);
		return new Message("1", "成功", "计算成功！");
	}
	
	@RequestMapping(value = "/data/download", method=RequestMethod.POST, 
		headers = {"content-type=application/json","content-type=application/xml"})
	public @ResponseBody Message download(@RequestBody Map<String, ? extends Object> paraMap, HttpServletRequest request) {
		LOGGER.info("[download] start paraMap is:" + paraMap);
		// save to user/<username>/calcData.json and user/<username>/calcData.xls
		User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
		if (u == null) {
			LOGGER.info("[download] user is null");
			return new Message("-1", "错误", "未登录，请从新登陆！");
		}
		try {
			String appBseDir = System.getProperty("app.base.dir");
			String userInfoFile = appBseDir + File.separator + 
	 				Constants.USER_INFO_DIR + File.separator + u.getUserName() + File.separator + Constants.FILE_CONTS_CALC_DATA;
			dataService.saveJson(paraMap, userInfoFile,u.getUserName());
			dataService.saveXls(paraMap, u.getUserName());
		} catch (Exception e) {
			LOGGER.error("[download] download error", e);
			return new Message("-1", "错误", "服务器异常！");
		}
		Message ret = new Message("1", "成功", "成功");
		LOGGER.info("[save] return: " + ret);
		return ret;
	}
	
	@RequestMapping(value = "/static/download", method=RequestMethod.POST, 
			headers = {"content-type=application/json","content-type=application/xml"})
		public @ResponseBody Message staticDownload(@RequestBody Map<String, ? extends Object> paraMap, HttpServletRequest request) {
			LOGGER.info("[download] start paraMap is:" + paraMap);
			// save to user/<username>/calcData.json and user/<username>/calcData.xls
			User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
			if (u == null) {
				LOGGER.info("[staticDownload] user is null");
				return new Message("-1", "错误", "未登录，请从新登陆！");
			}
			try {
				String time = TimeConvertor.date2String(new Date(), TimeConvertor.FORMAT_NONSYMBOL_24HOUR);
				String fileName = Constants.FILE_CONTS_CALC_EXPORT_NAME_XLS + time + Constants.EXT_NAME_XLS;
				LOGGER.info("[staticDownload] fileName is: " + fileName);
				dataService.saveStaticXls(paraMap, u.getUserName(), fileName);
				
				Message ret = new Message("1", fileName, "成功");
				LOGGER.info("[staticDownload] return: " + ret);
				return ret;
			} catch (Exception e) {
				LOGGER.error("[staticDownload] download error", e);
				return new Message("-1", "错误", "服务器异常！");
			}
	}
	
	@RequestMapping(value = "/static/calcReportData", method=RequestMethod.POST, 
			headers = {"content-type=application/json","content-type=application/xml"})
	public @ResponseBody Message calcReportData(@RequestBody Map<String, ? extends Object> paraMap, 
			@RequestParam("stationName") String stationName, 
			HttpServletRequest request) {
			LOGGER.info("[calcReportData] start stationName is: " + stationName + "\nparaMap is:" + paraMap);
			// save to user/<username>/calcData.json and user/<username>/calcData.xls
			User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
			if (u == null) {
				LOGGER.info("[calcReportData] user is null");
				return new Message("-1", "错误", "未登录，请从新登陆！");
			}
			try {
				List res = dataService.calcReportData(paraMap, stationName, u.getUserName());
				Message ret = new Message("1", "成功", "成功");
				ret.setData(res);
				LOGGER.info("[calcReportData] return: " + ret);
				return ret;
			} catch (Exception e) {
				LOGGER.error("[calcReportData] download error", e);
				return new Message("-1", "错误", "服务器异常！");
			}
	}
	
	@RequestMapping(value = "/static/calcDeduceData", method=RequestMethod.POST, 
			headers = {"content-type=application/json","content-type=application/xml"})
	public @ResponseBody Message calcDeduceData(@RequestBody Map<String, ? extends Object> paraMap, 
			@RequestParam("stationName") String stationName, 
			HttpServletRequest request) {
			LOGGER.info("[calcDeduceData] start stationName is: " + stationName + "\nparaMap is:" + paraMap);
			// save to user/<username>/calcData.json and user/<username>/calcData.xls
			User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
			if (u == null) {
				LOGGER.info("[calcDeduceData] user is null");
				return new Message("-1", "错误", "未登录，请从新登陆！");
			}
			try {
				List res = dataService.calcDeduceData(paraMap, stationName, u.getUserName());
				Message ret = new Message("1", "成功", "成功");
				ret.setData(res);
				LOGGER.info("[calcDeduceData] return: " + ret);
				return ret;
			} catch (Exception e) {
				LOGGER.error("[calcDeduceData] download error", e);
				return new Message("-1", "错误", "服务器异常！");
			}
	}
	
	@RequestMapping(value = "/data/stations", method=RequestMethod.GET, 
			headers = {"content-type=application/json","content-type=application/xml"})
	public @ResponseBody Message getStations(HttpServletRequest request) {
		LOGGER.info("[save] start getStations");
		// save to user/<username>/calcData.json
		User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
		Map ret = new HashMap();
		if (u == null) {
			LOGGER.info("[getStations] user is null");
			return new Message("-1", "错误", "用户未登陆！");
		}
		try {
		List dataMap =  dataService.getStations(u.getUserName());
		Message result = new Message("1", "成功", "计算成功！");
		result.setData(dataMap);
		LOGGER.info("[getStations] return: " + result);
		return result;
		} catch (Exception e) {
				LOGGER.error("[calcAllLine] save error", e);
				return new Message("-1", "错误", "服务器异常！");
		}
	}
	
	@RequestMapping(value = "/data/getStationData", method=RequestMethod.GET)
	public @ResponseBody List getStationData(@RequestParam("stationName") String stationName, HttpServletRequest request) {
		LOGGER.info("[getStationData] start getStationData");
		// save to user/<username>/calcData.json
		User u = (User) request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
		Map ret = new HashMap();
		if (u == null) {
			LOGGER.info("[getStationData] user is null");
			return new ArrayList();
		}
		try {
			List dataMap = dataService.getStationData(u.getUserName(), stationName);
			return dataMap;
		} catch (Exception e) {
			LOGGER.error("[calcAllLine] save error", e);
			return new ArrayList();
		}
	}
}
