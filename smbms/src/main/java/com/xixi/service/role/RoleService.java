package com.xixi.service.role;

import com.xixi.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
         public List<Role> getRoleList() throws SQLException, ClassNotFoundException;
}
