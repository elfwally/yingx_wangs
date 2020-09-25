package com.wang.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 解决Ajax的跨域问题
 * 为响应设置响应头
 * Access-Control-Allow-Origin ---> 设置访问白名单
 * 关闭电脑防火墙    如果电脑存在电脑管家，先把管家卸载，关闭防火墙，并重启电脑
 */
@Component
public class CorsFilter implements Filter {

    final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("执行跨域过滤器");
        HttpServletResponse response = (HttpServletResponse) res;
        //获取前台传过来的的域名,   参数：Origin：是前台传递域名的key
        String originHeader=((HttpServletRequest) req).getHeader("Origin");
        ArrayList<String> domainList = new ArrayList<>();
        domainList.add("http://localhost:8989"); //添加允许访问的域名
        domainList.add("http://localhost:8080");
        //判断该域名是否在白名单中
        if(domainList.contains(originHeader)){
            response.setHeader("Access-Control-Allow-Origin", originHeader);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        chain.doFilter(req, res);
    }
    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}
}