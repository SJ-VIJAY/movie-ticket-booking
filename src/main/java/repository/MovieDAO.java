package repository;

import model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public List<Movie> findAll() throws SQLException {
        String sql = "SELECT * FROM Movies";
        List<Movie> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Movie m = new Movie();
                m.setMovieId(rs.getInt("movie_id"));
                m.setMovieName(rs.getString("movie_name"));
                m.setTicketPrice(rs.getInt("ticket_price"));
                m.setPoster(rs.getString("poster"));
                m.setAvailableSeats(rs.getInt("available_seats"));
                list.add(m);
            }
        }
        return list;
    }

    public Movie findById(int id) throws SQLException {
        String sql = "SELECT * FROM Movies WHERE movie_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Movie m = new Movie();
                m.setMovieId(rs.getInt("movie_id"));
                m.setMovieName(rs.getString("movie_name"));
                m.setTicketPrice(rs.getInt("ticket_price"));
                m.setPoster(rs.getString("poster"));
                m.setAvailableSeats(rs.getInt("available_seats"));
                return m;
            }
        }
        return null;
    }

    public void reduceAvailableSeats(Connection con, int movieId, int count) throws SQLException {
        String sql = "UPDATE Movies SET available_seats = available_seats - ? WHERE movie_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, count);
            pst.setInt(2, movieId);
            pst.executeUpdate();
        }
    }

    public boolean addMovie(Movie m) throws SQLException {
        String sql = "INSERT INTO Movies(movie_name, ticket_price, poster, available_seats) VALUES (?, ?, ?, 25)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, m.getMovieName());
            pst.setInt(2, m.getTicketPrice());
            pst.setString(3, m.getPoster());
            return pst.executeUpdate() == 1;
        }
    }

    public boolean deleteById(int movieId) throws SQLException {
        String sql = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, movieId);
            return pst.executeUpdate() == 1;
        }
    }

    public void resetSeatsAndAvailability(int movieId) throws SQLException {
        String resetMovie = "UPDATE Movies SET available_seats = 25 WHERE movie_id = ?";
        String deleteSeats = "DELETE FROM Seats WHERE movie_id = ?";
        String insertSeat = "INSERT INTO Seats (movie_id, seat_no, is_booked, booked_by) VALUES (?, ?, 0, NULL)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pstReset = con.prepareStatement(resetMovie);
                 PreparedStatement pstDel = con.prepareStatement(deleteSeats);
                 PreparedStatement pstIns = con.prepareStatement(insertSeat)) {

                pstReset.setInt(1, movieId);
                pstReset.executeUpdate();

                pstDel.setInt(1, movieId);
                pstDel.executeUpdate();

                for (char row = 'A'; row <= 'E'; row++) {
                    for (int col = 1; col <= 5; col++) {
                        String code = "" + row + col;
                        pstIns.setInt(1, movieId);
                        pstIns.setString(2, code);
                        pstIns.addBatch();
                    }
                }
                pstIns.executeBatch();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
