package com.cycle.reggie_take_out.filter;


import com.alibaba.fastjson.JSON;
import com.cycle.reggie_take_out.common.BaseContext;
import com.cycle.reggie_take_out.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //AntPathMatcher是Spring提供的一个工具类，用来判断两个路径是否匹配
    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("--------------------LoginCheckFilter---------------------");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("请求路径：{}", request.getRequestURI());
        //获取请求路径
        String requestURI = request.getRequestURI();
        //放行的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/frontend/**",
        };
        //判断请求路径是否需要放行
        boolean check = check(requestURI, urls);
        if (check) {
            log.info("不需要登录，放行");
            //放行
            filterChain.doFilter(request, response);
        }else {
            //判断用户是否登录
            Object employee = request.getSession().getAttribute("employee");
            if (employee != null) {
                log.info("已登录，放行,用户id：{}", request.getSession().getAttribute("employee"));
                //将用户id存入ThreadLocal
                BaseContext.setCurrentUserId((Long) request.getSession().getAttribute("employee"));
                long id = Thread.currentThread().getId();
                log.info("当前线程id：{}", id);
                //已登录，放行
                filterChain.doFilter(request, response);
            } else {
                log.info("未登录，重定向到登录页面");
                //未登录，重定向到登录页面
                response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            }
        }
        return;
    }

    /**
     * 判断请求路径是否需要放行
     * @param requestURI
     * @param urls
     * @return
     */
    public boolean check(String requestURI, String[] urls) {
        for (String url : urls) {
            if (ANT_PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
