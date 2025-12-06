package repository;

import model.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    public List<Seat> findByMovieId(int movieId) throws SQLException {
        String sql = "SELECT * FROM Seats WHERE movie_id = ? ORDER BY seat_no";
        List<Seat> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, movieId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Seat s = new Seat();
                s.setSeatId(rs.getInt("seat_id"));
                s.setMovieId(rs.getInt("movie_id"));
                s.setSeatNo(rs.getString("seat_no"));
                s.setBooked(rs.getBoolean("is_booked"));
                list.add(s);
            }
        }
        return list;
    }

    public void createDefaultSeatsForMovie(int movieId) throws SQLException {
        String sql = "INSERT INTO Seats (movie_id, seat_no, is_booked, booked_by) VALUES (?, ?, 0, NULL)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            for (char row = 'A'; row <= 'E'; row++) {
                for (int col = 1; col <= 5; col++) {
                    String code = "" + row + col;
                    pst.setInt(1, movieId);
                    pst.setString(2, code);
                    pst.addBatch();
                }
            }
            pst.executeBatch();
        }
    }

    public int bookSeatsAtomically(Connection con, int userId, java.util.List<Integer> seatIds) throws SQLException {
        String sql = "UPDATE Seats SET is_booked = 1, booked_by = ? WHERE seat_id = ? AND is_booked = 0";
        int success = 0;
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            for (Integer sid : seatIds) {
                pst.setInt(1, userId);
                pst.setInt(2, sid);
                int updated = pst.executeUpdate();
                if (updated == 1) success++;
            }
        }
        return success;
    }

    public String getSeatNoById(Connection con, int seatId) throws SQLException {
        String sql = "SELECT seat_no FROM Seats WHERE seat_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, seatId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getString("seat_no");
        }
        return null;
    }
}
