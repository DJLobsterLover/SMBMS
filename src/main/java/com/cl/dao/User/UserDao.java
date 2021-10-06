package com.cl.dao.User;

import com.cl.pojo.Role;
import com.cl.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到登录的用户
    public User getLoginUser(Connection connection, String user_code) throws SQLException;
    //修改当前用户密码
    public int updatePwd(Connection connection, int id, String password) throws SQLException;
    //获取用户个数
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException;
    //获取用户列表
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize)throws Exception;

}
