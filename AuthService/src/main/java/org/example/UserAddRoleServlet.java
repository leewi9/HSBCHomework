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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/UserAddRole.jsp")
public class UserAddRoleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Res res = new Res(200, "success");
        // parse request
        String username = req.getParameter("username");
        String rolename = req.getParameter("role");
        if (username == null || rolename == null) {
            res.setCode(201);
            res.setMsg("params error");
        } else {
            // check if role exist
            List<Role> roles = (List<Role>) req.getSession().getServletContext().getAttribute("currentRoles");
            if (roles == null || roles.size() == 0) {
                res.setCode(303);
                res.setMsg("roles empty!");
            } else {
                List<Role> newRolesTmp = new ArrayList<>();
                for (Role r : roles) {
                    if (!r.getName().equals(rolename)) {
                        newRolesTmp.add(r);
                    }
                }
                if (newRolesTmp.size() == roles.size()) {
                    res.setCode(304);
                    res.setMsg("role " + rolename + " not exist!");
                }
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
                        List<User> newUsers = new ArrayList<>();
                        for (User u : users) {
                            if (!u.getUsername().equals(username)) {
                                newUsers.add(u);
                            } else {
                                // user exist, so next step is check role
                                boolean roleExist = false;
                                if (u.getRoles().size() > 0) {
                                    for (Role r : u.getRoles()) {
                                        if (rolename.equals(r.getName())) {
                                            roleExist = true;
                                            break;
                                        }
                                    }
                                }
                                if (roleExist) {
                                    // nothing happen
                                } else {
                                    // add new role
                                    List<Role> newRoles = u.getRoles();
                                    Role role = new Role(rolename);
                                    newRoles.add(role);
                                    u.setRoles(newRoles);
                                }
                                newUsers.add(u);
                            }
                            req.getSession().getServletContext().setAttribute("currentUsers", newUsers);
                        }
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
