package controller;

import model.User;
import repository.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        User u = new User();
        u.setUserName(name);
        u.setEmail(email);
        u.setPassword(pass);

        try {
            boolean ok = userDAO.save(u);
            if (ok) {
                Cookie cookie = new Cookie("userId", String.valueOf(u.getUserId()));
                cookie.setMaxAge(60 * 60);
                res.addCookie(cookie);
                res.sendRedirect("ViewMoviesServlet");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.sendRedirect("register.jsp?error=1");
    }
}
