package com.cl.servlet.User;

import com.alibaba.fastjson.JSONArray;
import com.cl.pojo.Role;
import com.cl.pojo.User;
import com.cl.service.Role.RoleServiceImpl;
import com.cl.service.User.UserService;
import com.cl.service.User.UserServiceImpl;
import com.cl.util.Constants;
import com.cl.util.PageSupport;
import com.mysql.jdbc.StringUtils;
//import com.sun.org.apache.bcel.internal.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//实现Servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null) {
            this.updatePwd(req,resp);
        } else if (method.equals("pwdmodify") && method!=null) {
            this.pwdModify(req, resp);
        } else if (method.equals("query") && method != null) {
            this.query(req, resp);
        } else if (method.equals("add") && method != null) {
            this.addUser(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        //从session获取id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");

        boolean flag = false;
        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if (flag) {
                req.setAttribute("message","修改密码成功，请退出使用新密码登录");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                req.setAttribute("message","修改密码失败");
            }
        }else{
            req.setAttribute("message","新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }

    //验证旧密码,session有用户的密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        //结果集
        Map<String, String> resultMap = new HashMap<String, String>();
        if (o == null) {//session过期了
            resultMap.put("result","sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result","error");
        }else {
            String userPassword = ((User) o).getUserPassword();//session用户中的密码
            if (userPassword.equals(oldpassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray，转换格式
            /**
             * resutlMap = ["result","session","result","error"]
             * JSon格式 = {key：value}
             */
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询用户信息
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        //查询用户列表
        //获取前端信息
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = null;
        //第一次请求，得到第一页,页面大小
        int pageSize = Constants.PageSize;
        int currentPageNo = Constants.CurrentPageNo;

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);//给查询赋值
        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户的总数
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);



        //控制首页和尾页
        int totalPageCount = pageSupport.getTotalPageCount();
        //如果页面要小于1，就显示第一页的内容
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        //获取用户列表展示
        try {
            userList =  userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
            req.setAttribute("userList", userList);

            RoleServiceImpl roleService = new RoleServiceImpl();
            List<Role> roleList = roleService.getRoleList();
            req.setAttribute("roleList", roleList);
            req.setAttribute("totalCount", totalCount);
            req.setAttribute("currentPageNo", currentPageNo);
            req.setAttribute("queryUserName", queryUserName);
            req.setAttribute("queryUserRole", queryUserRole);
            req.setAttribute("totalPageCount",totalPageCount);
            //返回前端
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加用户
    public void addUser(HttpServletRequest req, HttpServletResponse resp) {

    }

}
