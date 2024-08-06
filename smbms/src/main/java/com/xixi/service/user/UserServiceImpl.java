package com.xixi.service.user;

import com.xixi.dao.baseDao;
import com.xixi.dao.user.UserDao;
import com.xixi.dao.user.UserDaoImpl;
import com.xixi.pojo.user;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }
    public user login(String userCode, String userPassword) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        user user = null;

        try {
            connection = baseDao.getConnection();
            //通过业务层调用对应的具体的数据库操作
            user = userDao.getLojinUser(connection, userCode,userPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            baseDao.close(connection,null,null);
        }
        return user;
    }

    @Override
    public boolean updatePwd(String userPassword, int id) throws SQLException, ClassNotFoundException {
         Connection connection=null;
        connection = baseDao.getConnection();
        boolean flag=false;
        if (userDao.updatePwd(connection,id,userPassword)>0){
            flag=true;
        }
        baseDao.close(connection,null,null);
        return flag;
    }
    public int getUserCount(String username,int userRole) throws SQLException, ClassNotFoundException {
        Connection connection = baseDao.getConnection();
        int count = userDao.getUserCount(connection, username, userRole);
        baseDao.close(connection,null,null);
        return  count;
    }

    @Override
    public List<user> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) throws Exception {
         Connection connection=null;
         List<user> userList=null;
          connection = baseDao.getConnection();
        userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
         baseDao.close(connection,null,null);
         return userList;
    }

    @Override
    public Boolean add(user user) throws SQLException {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = baseDao.getConnection();//获得连接
            connection.setAutoCommit(false);//开启JDBC事务管理
            int updateRows = userDao.add(connection,user);
            connection.commit();
            if(updateRows > 0){
                flag = true;
                System.out.println("add success!");
            }else{
                System.out.println("add failed!");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                System.out.println("rollback==================");
                connection.rollback();//失败就回滚
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }finally{
            //在service层进行connection连接的关闭
            baseDao.close(connection, null, null);
        }
        return flag;
    }


    @Override
    public Boolean modify(user user) throws Exception {

        boolean flag=false;
            Connection connection=null;
          connection = baseDao.getConnection();
        connection.setAutoCommit(false);
        int updateNum = userDao.modify(connection, user);
        connection.commit();
        if (updateNum>0){
            flag=true;
            System.out.println("modify成功");
        }else {
            flag=false;
            System.out.println("modify失败");
        }
        baseDao.close(connection,null,null);
        return  flag;
    }

    public boolean deleteUserById(Integer delId) throws SQLException, ClassNotFoundException {
        Boolean flag=false;
        Connection connection=null;
        connection=baseDao.getConnection();
        try {
            int deleteNum=userDao.deleteUserById(connection,delId);
            if(deleteNum>0)flag=true;
        } catch (Exception e) {

        }finally {
            baseDao.close(connection,null,null);
        }
        return flag;
    }

    public user getUserById(String id) throws SQLException {
        user user = new user();
        Connection connection=null;
        try {
            connection=baseDao.getConnection();
            user = userDao.getUserById(connection,id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            baseDao.close(connection,null,null);
        }
        return  user;
    }

    @Override
    public user selectUserCodeExist(String userCode, String userPassword) throws SQLException {
        Connection connection = null;
        user user = null;
        try {
            connection = baseDao.getConnection();
            user = userDao.getLojinUser(connection, userCode,userPassword);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            baseDao.close(connection, null, null);
        }
        return user;
    }


    //@Test
   // public void test() throws SQLException, ClassNotFoundException {
     //   UserServiceImpl userService = new UserServiceImpl();
     //   user admin = userService.login("admin","1234567");
   //     System.out.println(admin.getUserPassword());

    //}
    @Test
    public void test() throws SQLException, ClassNotFoundException {
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 1);
        System.out.println(userCount);
    }
}
