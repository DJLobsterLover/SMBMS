package com.cl.servlet.User;

import com.cl.pojo.User;
import com.cl.service.User.UserServiceImpl;
import com.cl.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    //Service:控制层调用业务层


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet--Start....");
        //获取前端用户名和密码
        String user_code = req.getParameter("userCode");
        String input_password = (String) req.getParameter("userPassword");
        //和数据库中的密码进行对比，调用业务层代码
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login(user_code, input_password);
        String password = user.getUserPassword();
        if ((input_password.equals(password)) && (user!=null)) {
            //查有此人，可以登录
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //挑战到内部主页
            resp.sendRedirect("jsp/frame.jsp");
        }else{
            //查无此人，无法登录
            //转发回登录页面，提示用户名or密码错误
            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
