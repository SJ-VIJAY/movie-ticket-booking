package controller;

import model.User;
import repository.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");
        try {
            User u = userDAO.findByEmail(email);
            if (u != null && u.getPassword().equals(pass)) {
                Cookie cookie = new Cookie("userId", String.valueOf(u.getUserId()));
                cookie.setMaxAge(60 * 60);
                res.addCookie(cookie);

                HttpSession session = req.getSession();
                session.setAttribute("userName", u.getUserName());
                session.setAttribute("isAdmin", u.isAdmin());

                if (u.isAdmin()) {
                    res.sendRedirect("AdminHomeServlet");
                } else {
                    res.sendRedirect("ViewMoviesServlet");
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.sendRedirect("index.jsp?login=failed");
    }
}
