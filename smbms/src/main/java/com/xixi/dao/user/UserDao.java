package com.xixi.dao.user;

import com.xixi.pojo.Role;
import com.xixi.pojo.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public user getLojinUser(Connection connection,String userCode,String userPassword) throws SQLException;
    //修改用户密码
    public  int  updatePwd(Connection connection,int id,String userPassword) throws SQLException;
    //根据用户名或者角色来查找用户总数
    public  int getUserCount(Connection connection,String username,int userRole) throws SQLException;
    //通过用户输入的条件查询用户列表
    public List<user> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws  Exception;
    //增加用户信息
    public  int add(Connection connection,user user) throws Exception;

    //通过用户id删除用户信息
    public int deleteUserById(Connection connection, Integer delId)throws Exception;

    //通过userId查看当前用户信息
    public user getUserById(Connection connection, String id)throws Exception;
    //修改用户信息
    public int modify(Connection connection, user user)throws Exception;

}
