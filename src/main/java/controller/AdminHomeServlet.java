package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import model.Movie;
import model.User;
import repository.MovieDAO;
import repository.UserDAO;
import util.CookieUtil;

public class AdminHomeServlet extends HttpServlet {
    private final MovieDAO movieDAO = new MovieDAO();
    private final UserDAO userDAO = new UserDAO();

    private boolean isAdminUser(HttpServletRequest req) {
        String userIdStr = CookieUtil.getCookieValue(req, "userId");
        if (userIdStr == null) return false;
        try {
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
            List<Movie> movies = movieDAO.findAll();
            req.setAttribute("movies", movies);
            req.getRequestDispatcher("adminHome.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500);
        }
    }
}
