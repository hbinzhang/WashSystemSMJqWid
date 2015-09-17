package com.hry.dispatch.service.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hry.dispatch.domain.User;
import com.hry.dispatch.service.UserServiceI;
import com.hry.dispatch.util.Constants;
import com.hry.dispatch.util.FileInfoReader;

@Service("userService")
 public class UserServiceImpl implements UserServiceI {
 
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
     /**
      * ʹ��@Autowiredע���עuserMapper������
      * ����Ҫʹ��UserMapperʱ��Spring�ͻ��Զ�ע��UserMapper
      */
//     @Autowired
//     private UserMapper userMapper;//ע��dao
 
//    public UserMapper getUserMapper() {
//		return userMapper;
//	}
//
//	public void setUserMapper(UserMapper userMapper) {
//		this.userMapper = userMapper;
//	}

	//     @Override
     public void addUser(User user) {
//         userMapper.insert(user);
    	 LOGGER.info("[addUser] Get User");
     }
 
//     @Override
     public User getUserById(String userId) {
//         return userMapper.selectByPrimaryKey(userId);
    	 LOGGER.info("[getUserById] Get User");
    	 return null;
     }
     
//     @Override
     public List<User> getAllUser() {
//         return userMapper.getAllUser();
    	 LOGGER.info("[getAllUser] Get User");
    	 return null;
     }
     
     public User auth(String userName, String password) {
    	 LOGGER.info("[auth] username " + userName + "\tpassword "+ password);
 		String appBseDir = System.getProperty("app.base.dir");
 		String userInfoFile = appBseDir + File.separator + 
 				Constants.USER_INFO_DIR + File.separator + Constants.USER_INFO_FILE;
 		LOGGER.info("[auth] userInfoFile " + userInfoFile);
 		FileInfoReader fis = new FileInfoReader(userInfoFile);
 		List<String> uInfo = fis.readAll();
 		Iterator<String> uIt = uInfo.iterator();
 		while(uIt.hasNext()) {
 			String u = uIt.next();
 			if (u.startsWith(Constants.USER_INFO_LOGIN_NAME)) {
 				continue;
 			}
 			String[] infos = u.split(",");
 			if (infos[0].trim().equals(userName)) {
 				if(infos[1].trim().equals(password)) {
 					// make user dir
 					String userDir = appBseDir + File.separator + 
 			 				Constants.USER_INFO_DIR;
 					File dir = new File(userDir + File.separator + userName);
 					if (!dir.exists()) {
 						dir.mkdirs();
 					}
 					// construct User object and return
 					return new User("", infos[0].trim(),infos[1].trim(),infos[2].trim()
 							,infos[3].trim(),infos[4].trim());
 				}
 			}
 		}
 		return null;
     }
}