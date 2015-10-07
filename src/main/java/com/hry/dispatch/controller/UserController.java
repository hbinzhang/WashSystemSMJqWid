package com.hry.dispatch.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hry.dispatch.domain.Message;
import com.hry.dispatch.domain.User;
import com.hry.dispatch.service.UserServiceI;
import com.hry.dispatch.util.Constants;

@Controller
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserServiceI userService;

	@RequestMapping(value = "/user/getuser")
	public @ResponseBody User get(@ModelAttribute User us) {
		LOGGER.info("[get] Get User");
		String appBseDir = System.getProperty("app.base.dir");
		System.out.println("app base dir is: " + appBseDir);
		User user = userService.getUserById("1");
		if (user == null) {
			return new User();
		} else {
			return user;
		}
	}

	@RequestMapping(value = "/user/userauth")
	public @ResponseBody Message auth(@RequestParam("username") String userName,
			@RequestParam("password") String password, HttpServletRequest request) {
		try {
			LOGGER.info("[auth] username " + userName + "\tpassword "+ password);
			User u = userService.auth(userName, password);
			LOGGER.info("[auth] result " + u);
			if (u == null) {
				return new Message("-1", "Login failed", "登陆失败");
			}
			String appBseDir = System.getProperty("app.base.dir");
			// create user dir
			File dir = new File(appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + userName);
			if (!dir.exists()) {
				LOGGER.info("[auth] new user create dir: " + Constants.USER_INFO_DIR + File.separator + userName);
				dir.mkdirs();
			}
			String tmpDir = appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + userName + File.separator + Constants.TMP_DIR;
			File dirt = new File(tmpDir);
			if (!dirt.exists()) {
				LOGGER.info("[auth] new user create tmp dir");
				dirt.mkdirs();
			}
			String oriDataDir = appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + userName + File.separator + Constants.ORI_DATA_DIR;
			File oriDataDirF = new File(oriDataDir);
			if (!oriDataDirF.exists()) {
				LOGGER.info("[auth] new user create tmp dir");
				oriDataDirF.mkdirs();
			}
			request.getSession().setAttribute(Constants.SESSION_KEY_USER_INFO, u);
			return new Message("1","Login success","登陆成功");
		} catch (Exception e) {
			LOGGER.info("[auth] error ", e);
			return new Message("-1", "Login failed", "登陆失败");
		}
	}
	
	@RequestMapping(value = "/user/logout")
	public @ResponseBody Message logout(@RequestParam("username") String userName,
			HttpServletRequest request) {
		try {
			LOGGER.info("[logout] logout " + userName);
			request.getSession().removeAttribute(Constants.SESSION_KEY_USER_INFO);
			// remove tmp files
			String appBseDir = System.getProperty("app.base.dir");
			String path = appBseDir + File.separator + Constants.USER_INFO_DIR + File.separator + userName + File.separator;
			File dir = new File(path);
			String[] fs = dir.list();
			for (String f : fs) {
				if (f.startsWith(Constants.FILE_CONTS_CALC_EXPORT_NAME_XLS)) {
					File ft = new File(path + f);
					ft.delete();
					LOGGER.info("[logout] delete file: " + (path + f));
				}
			}
			return new Message("1","Logout success","登出成功");
		} catch (Exception e) {
			LOGGER.info("[logout] error ", e);
			return new Message("-1", "Logout failed", "登出失败");
		}
	}
	
	@RequestMapping(value = "/user/getCurrentUser")
	public @ResponseBody User getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			LOGGER.info("[getCurrentUser] start");
			User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
			if (u == null) {
				return null;
			}
			LOGGER.info("[getCurrentUser] return : " + u);
			response.setCharacterEncoding("utf-8");
			return u;
		} catch (Exception e) {
			LOGGER.info("[getCurrentUser] error ", e);
			return null;
		}
	}
	
	@RequestMapping(value = "/user/modpass", method=RequestMethod.POST)
	public @ResponseBody Message modpass(@RequestParam("username") String userName,
			@RequestParam("password") String password, @RequestParam("displayname") String displayname, HttpServletRequest request) {
		try {
			LOGGER.info("[modpass] username " + userName + "\tpassword "+ password+ "\tdisplayname "+ displayname);
			userService.modpass(userName, password, displayname);
			return new Message("1","modpass success","修改成功");
		} catch (Exception e) {
			LOGGER.info("[auth] error ", e);
			return new Message("-1", "modpass failed", "修改失败");
		}
	}
}
 