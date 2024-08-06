package com.xixi.service.role;

import com.xixi.dao.baseDao;
import com.xixi.dao.role.RoleDao;
import com.xixi.dao.role.RoleDaoImpl;
import com.xixi.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    //导入包
    private RoleDao roleDao;
    public RoleServiceImpl(){ roleDao=new RoleDaoImpl();}

    @Override
    public List<Role> getRoleList() throws SQLException {
        Connection connection=null;
        List<Role> roleList=null;
        try {
            connection= baseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            baseDao.close(connection,null,null);
        }
        return roleList;
    }
    @Test
    public void test() throws SQLException, ClassNotFoundException {
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role r: roleList){
            System.out.println(r);
        }
    }
}
