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

@WebServlet("/RoleDelete.jsp")
public class RoleDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);
    }

    // todo 已经赋予过的也要同步删除 或者提示还有用户在使用
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Res res = new Res(200, "success");
        // parse request
        String name = req.getParameter("name");
        if (name == null) {
            res.setCode(201);
            res.setMsg("params error");
        } else {
            List<Role> roles = (List<Role>) req.getSession().getServletContext().getAttribute("currentRoles");
            if (roles == null || roles.size() == 0) {
                res.setCode(303);
                res.setMsg("roles empty!");
            } else {
                //
                List<Role> newRoles = new ArrayList<>();
                for (Role r : roles) {
                    if (!r.getName().equals(name)) {
                        newRoles.add(r);
                    }
                    req.getSession().getServletContext().setAttribute("currentRoles", newRoles);
                }
                if (roles.size() == newRoles.size()) {
                    res.setCode(304);
                    res.setMsg("role " + name + " not exist!");
                }
            }
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(res);
        out.flush();
    }

}
