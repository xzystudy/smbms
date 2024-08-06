package com.xixi.dao.user;

import com.mysql.cj.util.StringUtils;
import com.xixi.dao.baseDao;
import com.xixi.pojo.Role;
import com.xixi.pojo.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    @Override
    public user getLojinUser(Connection connection, String userCode, String userPassword) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        user user = null;
        if (connection!=null){
            String sql="select * from smbms_user where userCode=?" ;
            Object [] params={userCode};
                rs = baseDao.execute(connection, sql, params,pstm, rs);
                if (rs.next()){
                    user = new user();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));
                }
                baseDao.close(null,pstm,rs);
            if (user != null && !user.getUserPassword().equals(userPassword)) user = null;
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String userPassword) throws SQLException {
        PreparedStatement pstm=null;
        int execute=0;
        if (connection!=null){
            String sql="update smbms_user  set userPassword=? where id=?";
            Object[] prams={userPassword,id};
             execute = baseDao.execute(connection, sql, prams,pstm);
             baseDao.close(connection,pstm,null);
        }
        return execute;
    }
    public  int getUserCount(Connection connection,String username,int userRole) throws SQLException {
             PreparedStatement pstm=null;
             ResultSet resultSet=null;
             int count=0;
             if (connection!=null){
                 StringBuilder sql = new StringBuilder();
                 sql.append("select count(1)  as count from smbms_user u,smbms_role r where u.userRole = r.id");
                 ArrayList<Object> list = new ArrayList<>();
                 if (!StringUtils.isNullOrEmpty(username)){
                     sql.append(" and u.userName like ?");
                     list.add("%"+username+"%");
                 }
                 if (userRole>0){
                     sql.append(" and u.userRole = ?");
                     list.add(userRole);
                 }
                 Object[] params= list.toArray();
                 resultSet = baseDao.execute(connection, sql.toString(), params, pstm, resultSet);
                 System.out.println(sql.toString());
             }
             if (resultSet.next()){
                 count = resultSet.getInt("count");
             }
             baseDao.close(connection,pstm,resultSet);

            return count;
    }

    @Override
    //通过用户输入的条件查询用户列表
    public List<user> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        List<user> userList = new ArrayList<user>();
        PreparedStatement pstm=null;
        ResultSet rs=null;
        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //实现分页显示
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = baseDao.execute(connection,sql.toString(),params,pstm,rs);
            while(rs.next()){
                user _user = new user();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            baseDao.close(null, pstm, rs);
        }
        return userList;
    }

    @Override
    public int add(Connection connection, user user) throws Exception {
        PreparedStatement pstm = null;
        int updateNum = 0;
        if (connection != null) {
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(), user.getUserRole(),
                    user.getGender(), user.getBirthday(), user.getPhone(), user.getAddress(), user.getCreationDate(), user.getCreatedBy()};
            updateNum = baseDao.execute(connection, sql, params, pstm);
            baseDao.close(null, pstm, null);
        }
        return updateNum;
    }
    @Override
    public int deleteUserById(Connection connection, Integer delId) throws Exception {
        PreparedStatement pstm=null;
        int deleteNum=0;
        if(connection!=null){
            String sql="DELETE FROM `smbms_user` WHERE id=?";
            Object[] params={delId};
            deleteNum=baseDao.execute(connection,sql,params,pstm);
            baseDao.close(null,pstm,null);
        }
        return deleteNum;
    }

    @Override
    public user getUserById(Connection connection, String id) throws Exception {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        user user = new user();
        if(connection!=null){
            String sql="select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] params={id};
            rs = baseDao.execute(connection, sql, params, pstm, rs);
            while(rs.next()){
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            baseDao.close(null,pstm,rs);
        }
        return user;
    }

    @Override
    public int modify(Connection connection, user user) throws Exception {
        int updateNum = 0;
        PreparedStatement pstm = null;
        if(null != connection){
            String sql = "update smbms_user set userName=?,"+
                    "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
                    user.getModifyDate(),user.getId()};
            updateNum = baseDao.execute(connection, sql,params,pstm);
            baseDao.close(null, pstm, null);
        }
        return updateNum;
    }

}

