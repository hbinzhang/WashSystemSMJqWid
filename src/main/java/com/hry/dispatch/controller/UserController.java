package com.hry.dispatch.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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
			return new Message("1","Logout success","登出成功");
		} catch (Exception e) {
			LOGGER.info("[logout] error ", e);
			return new Message("-1", "Logout failed", "登出失败");
		}
	}
	
	@RequestMapping(value = "/user/getCurrentUser")
	public @ResponseBody Message getCurrentUser(HttpServletRequest request) {
		try {
			LOGGER.info("[getCurrentUser] start");
			User u = (User)request.getSession().getAttribute(Constants.SESSION_KEY_USER_INFO);
			if (u == null) {
				return new Message("-1","","");
			}
			LOGGER.info("[getCurrentUser] return : " + u);
			return new Message("1",u.getUserName(),"");
		} catch (Exception e) {
			LOGGER.info("[getCurrentUser] error ", e);
			return new Message("-1", "getCurrentUser", "getCurrentUser失败");
		}
	}
	
	@RequestMapping(value = "/user/modpass")
	public @ResponseBody Message modpass(@RequestParam("username") String userName,
			@RequestParam("password") String password, HttpServletRequest request) {
		try {
			LOGGER.info("[modpass] username " + userName + "\tpassword "+ password);
			
			return new Message("1","modpass success","修改成功");
		} catch (Exception e) {
			LOGGER.info("[auth] error ", e);
			return new Message("-1", "modpass failed", "修改失败");
		}
	}
}
 