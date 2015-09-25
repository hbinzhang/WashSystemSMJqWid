package com.hry.dispatch.controller;

import java.io.File;
import java.util.HashMap;
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
				dataService.saveStaticXls(paraMap, u.getUserName());
			} catch (Exception e) {
				LOGGER.error("[staticDownload] download error", e);
				return new Message("-1", "错误", "服务器异常！");
			}
			Message ret = new Message("1", "成功", "成功");
			LOGGER.info("[save] return: " + ret);
			return ret;
		}
	
}
