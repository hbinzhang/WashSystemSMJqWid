package com.hry.dispatch.controller;

import java.io.File;
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

	@RequestMapping(value = "/data/fileupload")
	public String upload(@RequestParam(value = "fname", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {
		LOGGER.info("[upload] start");
		try {
			String fileName = file.getOriginalFilename();
			String tmpDir = System.getProperty("app.tmp.dir");
			if (tmpDir == null || tmpDir.length() == 0) {
				tmpDir = "D:\\";
			}
			File targetFile = new File(tmpDir, fileName);
			// LOGGER.info("[upload] path: " + path);
			LOGGER.info("[upload] fileName: " + fileName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);
			User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
			String appBseDir = System.getProperty("app.base.dir");
	 		String userInfoFile = appBseDir + File.separator + 
	 				Constants.USER_INFO_DIR + File.separator + u.getUserName() + File.separator + Constants.FILE_CONTS_CALC_DATA;
			dataService.uploadExcel(tmpDir + fileName, userInfoFile);
		return "calc";
		} catch (Exception e) {
			LOGGER.error("[upload] error", e);
			return "calcUpLoadError";
		}
		
	}

	@RequestMapping(value = "/data/save", method=RequestMethod.POST, 
			headers = {"content-type=application/json","content-type=application/xml"})
	public @ResponseBody Message save(@RequestBody Map<String, ? extends Object> paraMap, HttpServletRequest request) {
		LOGGER.info("[save] start uname is:" + "\nparaMap is: " + paraMap);
		User usInSess = (User)request.getAttribute(Constants.SESSION_KEY_USER_INFO);
		LOGGER.info("[save] usInSess is:" + usInSess);
		return new Message("1", "b", "c");
	}
}
