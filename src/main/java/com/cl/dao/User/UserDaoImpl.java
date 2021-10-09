package com.cl.dao.User;

import com.cl.dao.BaseDao;
import com.cl.pojo.Role;
import com.cl.pojo.User;
import com.mysql.jdbc.StringUtils;
//import org.graalvm.compiler.lir.LIRInstruction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{

    public User getLoginUser(Connection connection, String user_code) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {user_code};

            try {
                rs = BaseDao.execute(connection, pstm, rs, sql, params);
                if(rs.next()){
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        BaseDao.closeResource(null, pstm, rs);
        return user;
    }
    //修改用于代码
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement pstm = null;
        String sql = "update smbms_user set userPassword = ? where id = ?";
        Object params[] = {password, id};
        int execute = 0;
        if (connection != null) {
            try {
                execute = BaseDao.execute(connection,pstm,sql,params);
                BaseDao.closeResource(null, pstm, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return execute;

    }
    //根据用户或者角色查询用户总数
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();//存放参数


            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //
            Object[] params = list.toArray();

            System.out.println("UserDaoImpl->getUserCount:"+sql.toString());

            try {
               rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
                if (rs.next()) {
                    //从结果集获取数量
                    count = rs.getInt("count");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            BaseDao.closeResource(null,pstm,rs);

        }

        return count;



    }
    //获取用户列表
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();//存放参数

            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //在数据库中，分页使用limit，startindex，pagesize
            //当前页  （当前页-1）*页面大小
            //0,5      1    0   01234
            //5,5       2   5   56789
            sql.append(" order by creationDate DESC limit ?, ?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql-->" + sql.toString());
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                _user.setAge(_user.getAge());
                userList.add(_user);

            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return userList;
    }

    public boolean addUser(Connection connection, String userCode, String userName, String userPassword, int gender, String birthday,String phone, String address, int userRole) throws SQLException {
        boolean ifExistUser = false;
        boolean addUser = false;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Object> list = new ArrayList<Object>();//存放参数
        list.add(userCode);
        Object[] params = list.toArray();
        if (connection != null) {
            String sql = "select * from smbms_user where userCode = ?";
            try {

                rs = BaseDao.execute(connection,pstm,rs,sql.toString(),params);
                System.out.println(rs);
                if (rs.next()) {
                    ifExistUser = true;
                }else {
                    ifExistUser = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResource(null,pstm,rs);
            }

        }
        System.out.println(ifExistUser);
        if (ifExistUser == false) {
            //用户不存在，可以添加
            pstm = null;
            String sql = "insert into smbms_user (userCode, userName, userPassword, gender, birthday, phone, address, userRole)\n" +
                    "values (?,?,?,?,?,?,?,?)";
            List<Object> list1 = new ArrayList<Object>();//存放参数
            list1.add(userCode);
            list1.add(userName);
            list1.add(userPassword);
            list1.add(gender);
            list1.add(birthday);
            list1.add(phone);
            list1.add(address);
            list1.add(userRole);
            Object[] params1 = list1.toArray();
            try {
                int r = BaseDao.execute(connection,pstm,sql,params1);
                System.out.println(r);
                if (r > 0) {
                    System.out.println("添加成功");
                    addUser = true;
                } else {
                    addUser =false;
                    System.out.println("添加失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }   finally {
                BaseDao.closeResource(null,pstm,null);
            }

        }else {
            System.out.println("用户已存在，不能添加");
        }
        return addUser;
    }

    public boolean delUser(Connection connection, int userId) throws SQLException {
        System.out.println("------------------------------------------------");
        boolean ifDelete = false;
        PreparedStatement pstm = null;
        int rs = 0;
        List<Object> list = new ArrayList<Object>();//存放参数
        list.add(userId);
        Object[] params = list.toArray();
        if (connection != null) {
            String sql = "delete from smbms_user where id = ?";
            System.out.println(sql.toString());
            try {

                rs = BaseDao.execute(connection,pstm,sql.toString(),params);
                System.out.println(rs);
                if (rs > 0) {
                    ifDelete = true;
                    System.out.println("该用户已删除");
                }else {
                    ifDelete = false;
                    System.out.println("删除失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResource(null,pstm,null);
            }

        }
        return ifDelete;
    }


}
