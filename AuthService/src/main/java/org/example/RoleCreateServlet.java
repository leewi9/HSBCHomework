package org.example;

import org.example.model.Res;
import org.example.model.Role;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RoleCreate.jsp")
public class RoleCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Res res = new Res(200, "success");
        // parse request
        String name = req.getParameter("name");
        if (name == null) {
            res.setCode(201);
            res.setMsg("params error");
        } else {
            Role role = new Role(name);
            // save ServletContext
            List<Role> roles = (List<Role>) req.getSession().getServletContext().getAttribute("currentRoles");
            if (roles == null) {
                roles = new ArrayList<Role>();
            } else {
                // check if user exist
                for (Role r : roles) {
                    if (name.equals(r.getName())) {
                        res.setCode(302);
                        res.setMsg("role already existed!");
                    }
                }
            }
            roles.add(role);
            req.getSession().getServletContext().setAttribute("currentRoles", roles);
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(res);
        out.flush();
    }

}
