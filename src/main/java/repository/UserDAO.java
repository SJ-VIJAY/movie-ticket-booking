package repository;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUserName(rs.getString("user_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setAdmin(rs.getBoolean("is_admin"));
                return u;
            }
        }
        return null;
    }

    public boolean save(User user) throws SQLException {
        String sql = "INSERT INTO Users(user_name, email, password) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());
            int affected = pst.executeUpdate();
            if (affected == 1) {
                ResultSet g = pst.getGeneratedKeys();
                if (g.next()) user.setUserId(g.getInt(1));
                return true;
            }
        }
        return false;
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUserName(rs.getString("user_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setAdmin(rs.getBoolean("is_admin"));
                return u;
            }
        }
        return null;
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM Users ORDER BY user_id";
        List<User> users = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUserName(rs.getString("user_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setAdmin(rs.getBoolean("is_admin"));
                users.add(u);
            }
        }
        return users;
    }

    public void updateAdminFlag(int userId, boolean isAdmin) throws SQLException {
        String sql = "UPDATE Users SET is_admin = ? WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setBoolean(1, isAdmin);
            pst.setInt(2, userId);
            pst.executeUpdate();
        }
    }
}
