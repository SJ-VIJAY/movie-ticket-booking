package controller;

import repository.BookingDAO;
import model.Booking;
import util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class HistoryServlet extends HttpServlet {
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userIdStr = CookieUtil.getCookieValue(req, "userId");
        if (userIdStr == null) {
            res.sendRedirect("index.jsp?login=required");
            return;
        }
        int userId = Integer.parseInt(userIdStr);
        try {
            List<Booking> bookings = bookingDAO.findByUserId(userId);
            req.setAttribute("bookings", bookings);
            req.getRequestDispatcher("history.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500);
        }
    }
}
