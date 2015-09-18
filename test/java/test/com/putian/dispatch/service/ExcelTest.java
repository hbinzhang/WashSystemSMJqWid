package test.com.putian.dispatch.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hry.dispatch.domain.User;
import com.hry.dispatch.service.UserServiceI;
import com.hry.dispatch.service.impl.DataServiceImpl;
import com.hry.dispatch.util.Constants;
import com.mysql.fabric.xmlrpc.base.Array;

public class ExcelTest {

	private DataServiceImpl imp = null;
	
	 @Before
     public void before(){
         DOMConfigurator.configure("D:\\WORK\\binbin\\workspace\\repository\\WashSystemSMJqWid\\src\\main\\resources\\log4j\\log4j.xml");
         ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"spring.xml","spring-mybatis.xml"});
         imp = (DataServiceImpl) ac.getBean("dataService");
     }
	 
	 @Test
     public void testWrite(){
    	 System.out.println("testWrite");
    	 List<List<String>> dd = new ArrayList<List<String>>();
    	 List<String> heads = new ArrayList<String>();
 		heads.add("日期");
 		heads.add("清洗电量");
 		heads.add("累计");
 		heads.add("不清洗电量");
 		heads.add("不清洗累计");
 		heads.add("折算率指数（累计）");
 		heads.add("折算率指数");
 		heads.add("累计折算率");
 		heads.add("折算率");
 		heads.add("不清洗电量");
 		heads.add("累计电量");
 		heads.add("累计折算率");
 		heads.add("反推日降系数");
 		heads.add("损失累计");
 		dd.add(heads);
 		
 		List<String> one = new ArrayList<String>();
		one.add("-");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		one.add("");
		dd.add(one);
 		imp.writeExcel(dd, "D:\\test.xls");
     }
}
