package com.hry.dispatch.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component 
@Aspect
public class AspectAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AspectAdvice.class);
	
//	@Pointcut("execution(* com.hry.dispatch.service.*.*(..))")// 切入点表达式  
//    private void anyMethod() {  
//    }
//	
//	@AfterReturning(value = "anyMethod()", returning = "result")
//    public void doAfter(JoinPoint jp, String result) {
//        System.out.println("==========进入after advice=========== \n");
//        System.out.println("切入点方法执行完了 \n");
//
//        System.out.print(jp.getArgs()[0] + "在");
//        System.out.print(jp.getTarget().getClass() + "对象上被");
//        System.out.print(jp.getSignature().getName() + "方法删除了");
//        System.out.print("只留下：" + result + "\n\n");
//    }
	
	@Around("execution(* com.hry.dispatch.controller1.*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		LOGGER.info("===========进入around环绕方法！=========== \n");
        // 调用目标方法之前执行的动作
		LOGGER.info("调用方法之前: 执行！\n");
        // 调用方法的参数
        Object[] args = pjp.getArgs();
        // 调用的方法名
        String method = pjp.getSignature().getName();
        // 获取目标对象(形如：com.action.admin.LoginAction@1a2467a)
        Object target = pjp.getTarget();
       //获取目标对象的类名(形如：com.action.admin.LoginAction)
        String targetName = pjp.getTarget().getClass().getName();
        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        Object result = pjp.proceed();//result的值就是被拦截方法的返回值
        LOGGER.info("输出：" + args[0] + ";" + method + ";" + target + ";" + result + "\n");
        LOGGER.info("调用方法结束：之后执行！\n");
        
      return result;
    }
}
