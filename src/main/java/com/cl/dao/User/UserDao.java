package com.cl.dao.User;

import com.cl.pojo.Role;
import com.cl.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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
    //添加用户信息
    public boolean addUser(Connection connection, String userCode, String userName, String userPassword, int gender, String birthday,String phone, String address, int userRole) throws SQLException;
    //删除用户信息
    public boolean delUser(Connection connection, int userId)throws SQLException;

}
