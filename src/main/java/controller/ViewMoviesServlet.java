package controller;

import repository.MovieDAO;
import model.Movie;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class ViewMoviesServlet extends HttpServlet {
    private final MovieDAO movieDAO = new MovieDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Movie> movies = movieDAO.findAll();
            req.setAttribute("movies", movies);
            req.getRequestDispatcher("viewMovie.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500);
        }
    }
}
