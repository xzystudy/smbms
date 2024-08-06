package com.xixi.dao.role;

import com.xixi.dao.baseDao;
import com.xixi.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm=null;
        ResultSet res=null;
        ArrayList<Role> roleList = new ArrayList<>();
        if (connection!=null) {
            String sql = "select * from smbms_role";
            Object[] params = {};
            res = baseDao.execute(connection, sql, params, pstm, res);
            Role _user = null;
            while (res.next()) {
                _user = new Role();
                _user.setId(res.getInt("id"));
                _user.setRoleName(res.getString("roleName"));
                _user.setRoleCode(res.getString("roleCode"));
                roleList.add(_user);
            }
            baseDao.close(connection,pstm,res);
        }
        return roleList;
    }
}
