package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import model.User;
import repository.UserDAO;
import util.CookieUtil;

public class AdminUsersServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    private boolean isAdminUser(HttpServletRequest req) {
        try {
            String userIdStr = CookieUtil.getCookieValue(req, "userId");
            if (userIdStr == null) return false;
            int userId = Integer.parseInt(userIdStr);
            User u = userDAO.findById(userId);
            return (u != null && u.isAdmin());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!isAdminUser(req)) {
            res.sendRedirect("index.jsp?login=required");
            return;
        }

        try {
            List<User> users = userDAO.findAll();
            req.setAttribute("users", users);
            req.getRequestDispatcher("adminUsers.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!isAdminUser(req)) {
            res.sendRedirect("index.jsp?login=required");
            return;
        }

        String action = req.getParameter("action");
        String uidStr = req.getParameter("userId");
        if (uidStr != null && !uidStr.isEmpty()) {
            try {
                int userId = Integer.parseInt(uidStr);
                if ("makeAdmin".equals(action)) {
                    userDAO.updateAdminFlag(userId, true);
                } else if ("removeAdmin".equals(action)) {
                    userDAO.updateAdminFlag(userId, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.sendRedirect("AdminUsersServlet");
    }
}
