package com.cl.dao.Role;

import com.cl.dao.BaseDao;
import com.cl.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<Role> roles = new ArrayList<Role>();
        if (connection != null) {
            String sql = "select * from smbms_role";
            Object[] params = {};
            try {
                rs = BaseDao.execute(connection,pstm,rs,sql,params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (rs.next()) {
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleName(rs.getString("roleName"));
                _role.setRoleCode(rs.getString("roleCode"));
                roles.add(_role);
            }
            BaseDao.closeResource(null, pstm, rs);

        }
        return roles;
    }
}
