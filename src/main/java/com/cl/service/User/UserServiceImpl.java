package com.cl.service.User;

import com.cl.dao.BaseDao;
import com.cl.dao.User.UserDao;
import com.cl.dao.User.UserDaoImpl;
import com.cl.pojo.Role;
import com.cl.pojo.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{
    //引入Dao层
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }
    public User login(String user_code, String password) {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        try {
            user = userDao.getLoginUser(connection, user_code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        BaseDao.closeResource(connection, null, null);
        return user;

    }

    public boolean updatePwd(int id, String pwd) {
        Connection connection = null;
        boolean flag = false;
        //修改密码
        try {
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection,id, pwd)>0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize) throws Exception {
        Connection connection = null;
        List<User> userList = null;
        System.out.println("queryUserName:"+username);
        System.out.println("queryUserRole:"+userRole);
        System.out.println("currentPageNo:" + currentPageNo);
        System.out.println("pageSize:" + pageSize);
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, username, userRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;

    }


    @Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();
        List<User> users = null;
        try {
            users = userService.getUserList(null,0,2,5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (User user : users) {
            System.out.println(user.getAddress());
        }
    }

    @Test
    public void tes1t() {
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 0);
        System.out.println(userCount);
    }
}
