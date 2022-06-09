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

@WebServlet("/Authenticate.jsp")
public class AuthenticateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Res res = new Res(200, "success");
        // parse request
        String username = req.getParameter("username");
        String plain = req.getParameter("password");
        if (username == null || plain == null) {
            res.setCode(201);
            res.setMsg("params error");
        } else {
            // encrypt password
            String password = Base64.getEncoder().encodeToString((plain).getBytes());
            //
            List<User> users = (List<User>) req.getSession().getServletContext().getAttribute("currentUsers");
            if (users == null || users.size() == 0) {
                res.setCode(203);
                res.setMsg("users empty!");
            } else {
                User existUser = new User();
                boolean userExist = false;
                for (User u : users) {
                    if (u.getUsername().equals(username)) {
                        existUser = u;
                        userExist = true;
                    }
                }
                if (!userExist) {
                    res.setCode(204);
                    res.setMsg("user " + username + " not exist!");
                } else {
                    if (!existUser.getPassword().equals(password)) {
                        res.setCode(205);
                        res.setMsg("password incorrect!");
                    } else {
                        // create token by username and timestamp
                        String now = Long.toString(System.currentTimeMillis());
                        String token = Base64.getEncoder().encodeToString((username).getBytes("utf-8")) + "$" + Base64.getEncoder().encodeToString(now.getBytes("utf-8"));
                        // save to ServletContext
                        List<String> tokens = (List<String>) req.getSession().getServletContext().getAttribute("currentTokens");
                        if (tokens == null) {
                            tokens = new ArrayList<String>();
                        }
                        tokens.add(token);
                        req.getSession().getServletContext().setAttribute("currentTokens", tokens);
                        //
                        res.setMsg(token);
                    }
                }
            }
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(res);
        out.flush();
    }
}