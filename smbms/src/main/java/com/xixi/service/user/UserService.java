package com.xixi.service.user;

import com.xixi.pojo.user;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public user login(String userCode,String password) throws SQLException, ClassNotFoundException;
    public boolean updatePwd(String userPassword,int id) throws SQLException, ClassNotFoundException;
    public int getUserCount(String username,int  userRole) throws SQLException, ClassNotFoundException;
    //根据条件查询用户列表
    public List<user> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) throws Exception;
    //增加用户
    public Boolean add(user user) throws Exception;

    //修改用户信息
    public Boolean modify(user user) throws Exception;

    //根据用户id删除用户
    public boolean deleteUserById(Integer delId) throws SQLException, ClassNotFoundException;

    //根据用户id得到当前用户
    public user getUserById(String id) throws SQLException;
    public user selectUserCodeExist(String userCode,String userPassword) throws SQLException;

}
