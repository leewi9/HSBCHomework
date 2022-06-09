package org.example;

import org.example.model.Res;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/Invalidate.jsp")
public class InvalidateServlet extends HttpServlet {

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
            List<String> tokens = (List<String>) req.getSession().getServletContext().getAttribute("currentTokens");
            if (tokens == null || tokens.size() == 0) {
                res.setCode(401);
                res.setMsg("tokens empty!");
            } else {
                if (tokens.contains(token)) {
                    tokens.remove(token);
                    req.getSession().getServletContext().setAttribute("currentTokens", tokens);
                } else {
                    // token not exist, aka, invalid token
                    res.setCode(402);
                    res.setMsg("invalid token!");
                }
            }
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(res);
        out.flush();
    }

}
