package repository;

import model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean saveBooking(Connection con, Booking b) throws SQLException {
        String sql = "INSERT INTO Bookings(user_id, movie_name, seat_no) VALUES (?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, b.getUserId());
            pst.setString(2, b.getMovieName());
            pst.setString(3, b.getSeatNo());
            int affected = pst.executeUpdate();
            return affected == 1;
        }
    }

    public List<Booking> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM Bookings WHERE user_id = ? ORDER BY booked_on DESC";
        List<Booking> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setMovieName(rs.getString("movie_name"));
                b.setSeatNo(rs.getString("seat_no"));
                b.setBookedOn(rs.getTimestamp("booked_on").toLocalDateTime());
                list.add(b);
            }
        }
        return list;
    }
}
