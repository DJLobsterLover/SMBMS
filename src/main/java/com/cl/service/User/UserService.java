package com.cl.service.User;

import com.cl.pojo.Role;
import com.cl.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //用户登录
    public User login(String user_code, String password);
    //用户修改密码
    public boolean updatePwd(int id, String pwd);
    //查询用户总数
    public int getUserCount(String username, int userRole);
    //获取用户列表
    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize)throws Exception;
    //添加用户信息
    public boolean addUser(String userCode, String userName, String userPassword, int gender, String birthday,String phone, String address, int userRole);

}
