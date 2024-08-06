package com.xixi.servlet.user;

import com.xixi.pojo.user;
import com.xixi.service.user.UserServiceImpl;
import com.xixi.util.constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class loginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login Servlet begin");
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        UserServiceImpl userService = new UserServiceImpl();
        try {
            user user = userService.login(userCode, userPassword);
            if (user!=null){
                req.getSession().setAttribute(constants.USER_SESSION,user);
                resp.sendRedirect("jsp/frame.jsp");
            }else{
                req.setAttribute("error","用户名或者密码不正确");
                req.getRequestDispatcher("login.jsp").forward(req,resp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
