package com.wang.aspect;


import com.wang.annotation.AddLog;
import com.wang.dao.LogDao;
import com.wang.entity.yx_Admin;
import com.wang.entity.yx_Log;
import com.wang.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect//定义切面
@Configuration//交给spring工厂管理对象
public class LogAspect {
    @Resource
    HttpSession session;
    @Resource
    LogDao logDao;
    //@Around("execution(* com.baizhi.service.impl.*.*(..))&& !execution(* com.baizhi.service.impl.*.query*(..))")  切入点为com.baizhi.service下的所有impl下的所有类中的 所有方法 然后不包括 com.baizhi.service下的所有impl下的所有类中的 query*名的方法   此切入点方式由于方法名的各种不同 非常不便利 所以使用下面第二种种方法  先设置自定义注解@AddLog  然后切入点为自定义注解  自定义注解直接放在要使用的位置上 从而更便利 切面点而且自定义注解设置的value值 可以在打印日志中 可以看到自定义注解中自定义写的值
    public Object addLog(ProceedingJoinPoint joinPoint){
        //谁   时间    操作    是否成功
        yx_Admin admin =(yx_Admin)session.getAttribute("admin");
        //时间
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        //操作   那个方法
        String name = joinPoint.getSignature().getName();
        //放行
        String message=null;
        try {
            Object proceed = joinPoint.proceed();
            message="success";
            System.out.println("管理员："+admin.getUsername()+"--时间："+format+"--操作："+name+"--状态："+message);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            message="error";
            System.out.println("管理员："+admin.getUsername()+"--时间："+format+"--操作："+name+"--状态："+message);
            return null;
        }
    }
    @Around("@annotation(com.wang.annotation.AddLog)")//切入点为自定义注解@AddLog   （只要方法上有这个自定义注解 那么这个方法就是切入点）
    public Object addLogs(ProceedingJoinPoint joinPoint){//原生参数 ProceedingJoinPoint joinPoint
        //谁   时间    操作    是否成功
        yx_Admin admin =(yx_Admin)session.getAttribute("admin");
        //时间
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd : hh:mm:ss");//设置时间格式
        String format = sdf.format(date);
        //操作  方法上的注解信息
        String name = joinPoint.getSignature().getName();
        //先得到方法
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法上的注解
        AddLog annotation = method.getAnnotation(AddLog.class);
        //获取注解中的内容
        String value = annotation.value();
        //放行
        String message=null;
        try {
            Object proceed = joinPoint.proceed();//放行
            message="success";
            System.out.println("管理员："+admin.getUsername()+"  时间："+format+"  操作："+value+"  状态："+message);
            //进行日志数据入库
            yx_Log log = new yx_Log();


            log.setAdmin(admin.getUsername());
            log.setOption(value);
            log.setDate(new Date());
            log.setStatus(message);
            logDao.insert(log);
            System.out.println(log);

            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            message="error";
            System.out.println("管理员："+admin.getUsername()+"  时间："+format+"  操作："+value+"  状态："+message);
            return null;
        }
    }
}
