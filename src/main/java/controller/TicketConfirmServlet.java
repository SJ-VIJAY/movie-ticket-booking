package controller;

import repository.SeatDAO;
import repository.BookingDAO;
import repository.MovieDAO;
import repository.DBConnection;
import model.Booking;
import model.Movie;
import util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TicketConfirmServlet extends HttpServlet {
    private final SeatDAO seatDAO = new SeatDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final MovieDAO movieDAO = new MovieDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String userIdStr = CookieUtil.getCookieValue(req, "userId");
        if (userIdStr == null) {
            res.sendRedirect("index.jsp?login=required");
            return;
        }
        int userId = Integer.parseInt(userIdStr);

        String[] seatIdsArr = req.getParameterValues("seatId");
        String movieName = req.getParameter("movieName");
        String movieIdStr = req.getParameter("movieId");

        if (movieIdStr == null || movieIdStr.isEmpty()) {
            res.sendRedirect("ViewMoviesServlet");
            return;
        }

        if (seatIdsArr == null || seatIdsArr.length == 0) {
            res.sendRedirect("BookingServlet?movieId=" + movieIdStr + "&error=noseat");
            return;
        }

        int movieId = Integer.parseInt(movieIdStr);
        List<Integer> seatIds = new ArrayList<>();
        for (String sId : seatIdsArr) {
            seatIds.add(Integer.parseInt(sId));
        }

        try (Connection con = DBConnection.getConnection()) {
            try {
                con.setAutoCommit(false);

                int booked = seatDAO.bookSeatsAtomically(con, userId, seatIds);

                if (booked != seatIds.size()) {
                    con.rollback();
                    res.sendRedirect("BookingServlet?movieId=" + movieId + "&error=concurrent");
                    return;
                }

                List<String> seatNos = new ArrayList<>();

                for (Integer sid : seatIds) {
                    String seatNo = seatDAO.getSeatNoById(con, sid);
                    seatNos.add(seatNo);

                    Booking b = new Booking();
                    b.setUserId(userId);
                    b.setMovieName(movieName);
                    b.setSeatNo(seatNo);
                    bookingDAO.saveBooking(con, b);
                }

                movieDAO.reduceAvailableSeats(con, movieId, seatIds.size());

                Movie movie = movieDAO.findById(movieId);
                int ticketPrice = (movie != null) ? movie.getTicketPrice() : 0;
                int totalPrice = ticketPrice * seatIds.size();

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                String bookingTime = now.format(fmt);

                con.commit();

                req.setAttribute("movieName", movieName);
                req.setAttribute("ticketCount", seatIds.size());
                req.setAttribute("seatList", String.join(", ", seatNos));
                req.setAttribute("totalPrice", totalPrice);
                req.setAttribute("bookingTime", bookingTime);

                req.getRequestDispatcher("success.jsp").forward(req, res);

            } catch (Exception e) {
                e.printStackTrace();
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
                res.sendRedirect("BookingServlet?movieId=" + movieId + "&error=db");
            } finally {
                try { con.setAutoCommit(true); } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("BookingServlet?movieId=" + movieIdStr + "&error=db");
        }
    }
}
