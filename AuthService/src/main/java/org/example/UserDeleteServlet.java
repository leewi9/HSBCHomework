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
import java.util.List;

@WebServlet("/UserDelete.jsp")
public class UserDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Res res = new Res(200, "success");
        // parse request
        String username = req.getParameter("username");
        if (username == null) {
            res.setCode(201);
            res.setMsg("params error");
        } else {
            //
            List<User> users = (List<User>) req.getSession().getServletContext().getAttribute("currentUsers");
            if (users == null || users.size() == 0) {
                res.setCode(203);
                res.setMsg("users empty!");
            } else {
                // todo
                List<User> newUsers = new ArrayList<>();
                for (User u : users) {
                    if (!u.getUsername().equals(username)) {
                        newUsers.add(u);
                    }
                    req.getSession().getServletContext().setAttribute("currentUsers", newUsers);
                }
                if (users.size() == newUsers.size()) {
                    res.setCode(204);
                    res.setMsg("user " + username + " not exist!");
                }
            }
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(res);
        out.flush();
    }

}
