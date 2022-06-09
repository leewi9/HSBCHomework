package org.example;

import org.example.model.Res;
import org.example.model.Role;
import org.example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

@WebServlet("/AllRoles.jsp")
public class AllRolesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Res res = new Res(200, "success");
        // parse request
        String token = req.getParameter("token");
        if (token == null) {
            res.setCode(201);
            res.setMsg("params error");
        } else {
            // get username
            String[] lst = token.split("\\$");
            byte[] base64decodedBytes1 = Base64.getDecoder().decode(lst[0]);
            String username = new String(base64decodedBytes1, "utf-8");
            // check if token expired
            if (Utils.checkToken(token)) {
                res.setCode(403);
                res.setMsg("token expired!");
            }
            // check if token valid
            List<String> tokens = (List<String>) req.getSession().getServletContext().getAttribute("currentTokens");
            if (!tokens.contains(token)) {
                // token not exist
                res.setCode(402);
                res.setMsg("invalid token!");
            } else {
                // check if user belongs to role
                List<User> users = (List<User>) req.getSession().getServletContext().getAttribute("currentUsers");
                if (users == null || users.size() == 0) {
                    res.setCode(203);
                    res.setMsg("users empty!");
                } else {
                    //
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
                        // check if role in user
                        List<Role> roles = existUser.getRoles();
                        StringBuilder rs = new StringBuilder();
                        for (Role r : roles) {
                            rs.append(r.getName()).append(";");
                        }
                        res.setMsg(rs.toString());
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
