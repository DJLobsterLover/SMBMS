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

    public boolean addUser(String userCode, String userName, String userPassword, int gender, String birthday, String phone, String address, int userRole) {
        Connection connection = null;
        boolean r = false;
        try {
            connection = BaseDao.getConnection();
            r = userDao.addUser(connection, userCode, userName, userPassword, gender, birthday, phone, address, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return r;
    }

    public boolean delUser(int userId) {
        Connection connection = null;
        boolean r = false;
        try {
            connection = BaseDao.getConnection();
            r = userDao.delUser(connection, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            BaseDao.closeResource(connection,null,null);
        }
        return r;
    }

    public User viewUser(int userId) {
        Connection connection = null;
        User u = null;
        try {
            connection = BaseDao.getConnection();
            u = userDao.viewUser(connection, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            BaseDao.closeResource(connection,null,null);
        }
        return u;
    }

    public boolean modifyUser(int userID, String userName, int gender, String birthday, String phone, String address, int userRole) {
        Connection connection = null;
        boolean b = false;
        try {
            connection = BaseDao.getConnection();
            b = userDao.modifyUser(connection,userID,userName,gender,birthday,phone,address,userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return b;

    }


    @Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();
        boolean b = false;
        try {
            b  = userService.modifyUser(17,"郭靖",1,"1987-12-04","124","东湖",3);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void tes1t() {
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 0);
        System.out.println(userCount);
    }
}
