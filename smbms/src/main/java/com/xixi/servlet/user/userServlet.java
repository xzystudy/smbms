package com.xixi.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.xixi.pojo.Role;
import com.xixi.pojo.user;
import com.xixi.service.role.RoleService;
import com.xixi.service.role.RoleServiceImpl;
import com.xixi.service.user.UserService;
import com.xixi.service.user.UserServiceImpl;
import com.xixi.util.PageSupport;
import com.xixi.util.constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class userServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("savepwd")&&method!=null){
            this.updatePwd(req,resp);
        }else if(method.equals("pwdmodify")&&method!=null){
            this.pwdModify(req,resp);
        }else if(method.equals("query")&&method!=null){
            try {
                this.query(req,resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else   if(method != null && method.equals("add")){
            //增加操作
            try {
                this.add(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(method != null && method.equals("getrolelist")){
            //查询用户角色表
            try {
                this.getRoleList(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else if(method != null && method.equals("ucexist")){
            //查询当前用户编码是否存在
            try {
                this.userCodeExist(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(method != null && method.equals("deluser")){
            //删除用户
            try {
                this.delUser(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else if(method != null && method.equals("view")){
            //通过用户id得到用户
            try {
                this.getUserById(req, resp,"userview.jsp");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(method != null && method.equals("modify")){
            //通过用户id得到用户
            try {
                this.getUserById(req, resp,"usermodify.jsp");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(method != null && method.equals("modifyexe")){
            //验证用户
            try {
                this.modify(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter("uid");;
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        //创建一个user对象接收这些参数
        user user = new user();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((user)req.getSession().getAttribute(constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        user.setUserRole(Integer.valueOf(userRole));
        //调用service层
        UserServiceImpl userService = new UserServiceImpl();
        Boolean flag = userService.modify(user);

        //判断是否修改成功来决定跳转到哪个页面
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }

    }
    public void getUserById(HttpServletRequest req, HttpServletResponse resp,String url) throws SQLException, ServletException, IOException {
        String id = req.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            UserService userService = new UserServiceImpl();
            user user = userService.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher(url).forward(req, resp);
        }

    }
    public  void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //用一个hashmap，暂存现在所有现存的用户编码
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            //userCode == null || userCode.equals("")
            //如果输入的这个编码为空或者不存在，说明可用
            resultMap.put("userCode", "exist");
        }else{//如果输入的编码不为空，则需要去找一下是否存在这个用户
            UserService userService = new UserServiceImpl();
            user user = userService.selectUserCodeExist(userCode,userPassword);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }
        //把resultMap转为json字符串以json的形式输出
        //配置上下文的输出类型
        resp.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = resp.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }
    public void  getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ClassNotFoundException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }
    public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("当前正在执行增加用户操作");
        //从前端得到页面的请求的参数即用户输入的值
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        //String ruserPassword = req.getParameter("ruserPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        //把这些值塞进一个用户属性中
        user user = new user();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        //查找当前正在登陆的用户的id
        user.setCreatedBy(((user)req.getSession().getAttribute(constants.USER_SESSION)).getId());
        UserServiceImpl userService = new UserServiceImpl();
        Boolean flag = userService.add(user);
        //如果添加成功，则页面转发，否则重新刷新，再次跳转到当前页面
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("useradd.jsp").forward(req,resp);
        }
    }
    public void  delUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, IOException {
        String id = req.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        //需要判断是否能删除成功
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            UserService userService = new UserServiceImpl();
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();

    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String queryUserName = req.getParameter("queryUserName");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole=0;
        UserServiceImpl userService = new UserServiceImpl();
        List<user> userList=null;
        int  pageSize=5;
        int currentPageSize=1;

        if (queryUserName==null){
            queryUserName="";
        }
        if (temp!=null&&!temp.equals( "")){
            queryUserRole=Integer.parseInt(temp);
        }
        if (pageIndex!=null){
            currentPageSize  = Integer.parseInt(pageIndex);
        }
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageSize);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalPageCount(totalCount);
        int totalPageCount = (totalCount/pageSize) +1;
        if (currentPageSize<1){
            currentPageSize=1;
        } else if (currentPageSize>totalPageCount) {
            currentPageSize=totalPageCount;
        }
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageSize, pageSize);
        req.setAttribute("userList",userList);
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageSize);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);
        req.getRequestDispatcher("userlist.jsp").forward(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
    public void  updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute(constants.USER_SESSION);
        String newpassword=req.getParameter("newpassword");
        System.out.println(newpassword);
        boolean flag;
        if (o!=null&& !StringUtils.isNullOrEmpty(newpassword)){
            UserServiceImpl userService = new UserServiceImpl();
            try {
                flag=userService.updatePwd(newpassword,((user)o).getId());
                System.out.println(flag);
                if (flag==true){
                    req.setAttribute("message","恭喜您修改密码成功，请您重新登录");
                    req.getSession().removeAttribute(constants.USER_SESSION);
                }else {
                    req.setAttribute("message","密码修改失败");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else{
            req.setAttribute("message","新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        //万能的Map 结果集;
        HashMap<String, String> resultMap = new HashMap<>();

        if (o==null){//session失效了
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        }else {
            String userPassword = ((user) o).getUserPassword();
            if (oldpassword.equals(userPassword)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
            try {
                resp.setContentType("application/json");
                PrintWriter writer = resp.getWriter();
                writer.write(JSONArray.toJSONString(resultMap));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
