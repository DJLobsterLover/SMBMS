package com.cl.service.Role;

import com.cl.dao.BaseDao;
import com.cl.dao.Role.RoleDao;
import com.cl.dao.Role.RoleDaoImpl;
import com.cl.pojo.Role;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    //获取用户列表
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> rs = null;
        try {
            connection = BaseDao.getConnection();
            rs = roleDao.getRoleList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return rs;
    }

    @Test
    public void test(){
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roles = roleService.getRoleList();
        for (Role role : roles) {
            System.out.println(role.getRoleName());
        }
    }
}
