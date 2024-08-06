package com.xixi.filter;


import com.xixi.pojo.user;
import com.xixi.util.constants;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain Chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        user user = (user)((HttpServletRequest) req).getSession().getAttribute(constants.USER_SESSION);
        if (user==null){
            response.sendRedirect(((HttpServletRequest) req).getContextPath()+"/error.jsp");

        }else {
            Chain.doFilter(req,resp);
        }

    }

    @Override
    public void destroy() {

    }
}
