package test.com.putian.dispatch.service;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hry.dispatch.domain.User;
import com.hry.dispatch.service.UserServiceI;

public class MyBatisTest {
 
     private UserServiceI userService;
     
     @Before
     public void before(){
         DOMConfigurator.configure("D:\\Workspace\\EclipseMarsProjs\\WashSystemSMJqWid\\src\\main\\resources\\log4j\\log4j.xml");
         ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"spring.xml","spring-mybatis.xml"});
         userService = (UserServiceI) ac.getBean("userService");
     }
     
     @Test
     public void testAuthUser(){
    	 System.out.println("Start test2");
         User u = userService.auth("sgcc", "123456");
         System.out.println("sgcc is: " + u);
         
         User u2 = userService.auth("sgsp", "123456");
         System.out.println("sgsp is: " + u2);
         
         User u3 = userService.auth("sgsp", "12345632");
         System.out.println("sgsp wpass is: " + u3);
     }
     
}