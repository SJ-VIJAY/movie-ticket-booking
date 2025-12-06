package controller;

import repository.SeatDAO;
import repository.MovieDAO;
import model.Seat;
import model.Movie;
import util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class BookingServlet extends HttpServlet {
    private final SeatDAO seatDAO = new SeatDAO();
    private final MovieDAO movieDAO = new MovieDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String mid = req.getParameter("movieId");
        if (mid == null) {
            res.sendRedirect("ViewMoviesServlet");
            return;
        }
        int movieId = Integer.parseInt(mid);

        String userId = CookieUtil.getCookieValue(req, "userId");
        if (userId == null) {
            res.sendRedirect("index.jsp?login=required");
            return;
        }

        try {
            Movie movie = movieDAO.findById(movieId);

            List<Seat> seats = seatDAO.findByMovieId(movieId);
            if (seats == null || seats.isEmpty()) {
                seatDAO.createDefaultSeatsForMovie(movieId);
                seats = seatDAO.findByMovieId(movieId);
            }

            req.setAttribute("movie", movie);
            req.setAttribute("seats", seats);
            req.getRequestDispatcher("seatSelect.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500);
        }
    }
}
