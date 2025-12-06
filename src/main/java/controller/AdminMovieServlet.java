package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

import model.Movie;
import model.User;
import repository.MovieDAO;
import repository.UserDAO;
import util.CookieUtil;

public class AdminMovieServlet extends HttpServlet {
    private final MovieDAO movieDAO = new MovieDAO();
    private final UserDAO userDAO = new UserDAO();

    private User getCurrentUser(HttpServletRequest req) throws Exception {
        String userIdStr = CookieUtil.getCookieValue(req, "userId");
        if (userIdStr == null) return null;
        int userId = Integer.parseInt(userIdStr);
        return userDAO.findById(userId);
    }

    private boolean ensureAdmin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            User u = getCurrentUser(req);
            if (u == null || !u.isAdmin()) {
                res.sendRedirect("index.jsp?login=required");
                return false;
            }
            return true;
        } catch (Exception e) {
            res.sendRedirect("index.jsp?login=required");
            return false;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!ensureAdmin(req, res)) return;

        String action = req.getParameter("action");
        if ("add".equals(action)) {
            String name = req.getParameter("movieName");
            String priceStr = req.getParameter("ticketPrice");
            String poster = req.getParameter("poster");

            int price = 0;
            try { price = Integer.parseInt(priceStr); } catch (NumberFormatException e) { }

            Movie m = new Movie();
            m.setMovieName(name);
            m.setTicketPrice(price);
            m.setPoster(poster);

            try {
                movieDAO.addMovie(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res.sendRedirect("AdminHomeServlet");
        } else {
            res.sendRedirect("AdminHomeServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!ensureAdmin(req, res)) return;

        String action = req.getParameter("action");
        String midStr = req.getParameter("movieId");

        if (midStr == null || midStr.isEmpty()) {
            res.sendRedirect("AdminHomeServlet");
            return;
        }

        int movieId = Integer.parseInt(midStr);

        try {
            if ("delete".equals(action)) {
                movieDAO.deleteById(movieId);
            } else if ("reset".equals(action)) {
                movieDAO.resetSeatsAndAvailability(movieId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        res.sendRedirect("AdminHomeServlet");
    }
}
