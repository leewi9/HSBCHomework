package org.example;

import org.example.model.Res;
import org.example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@WebServlet("/UserCreate.jsp")
public class UserCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Res res = new Res(200, "success");
        // parse request
        String username = req.getParameter("username");
        String plain = req.getParameter("password");
        if (username == null || plain == null) {
            res.setCode(201);
            res.setMsg("params error");
        } else {
            String password = Base64.getEncoder().encodeToString((plain).getBytes());
            User user = new User(username, password);
            // save ServletContext
            List<User> users = (List<User>) req.getSession().getServletContext().getAttribute("currentUsers");
            if (users == null) {
                users = new ArrayList<User>();
            } else {
                // check if user exist
                for (User u : users) {
                    if (username.equals(u.getUsername())) {
                        res.setCode(202);
                        res.setMsg("user already existed!");
                    }
                }
            }
            users.add(user);
            req.getSession().getServletContext().setAttribute("currentUsers", users);
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(res);
        out.flush();
    }

}
