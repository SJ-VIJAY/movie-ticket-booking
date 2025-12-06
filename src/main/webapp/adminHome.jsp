<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Movie" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Manage Movies</title>
    <style>
        body{margin:0;font-family:Arial,sans-serif;
             background:linear-gradient(135deg,#1E88E5,#43A047);min-height:100vh;color:#fff;}
        .top-bar{padding:15px 30px;background:rgba(0,0,0,0.25);display:flex;justify-content:space-between;align-items:center;}
        .top-bar h1{margin:0;font-size:24px;}
        .top-links a{color:#fff;text-decoration:none;margin-left:12px;font-size:14px;font-weight:bold;}
        .top-links a:hover{text-decoration:underline;}
        .container{width:90%;max-width:1100px;margin:20px auto;background:#fff;color:#333;
             border-radius:12px;padding:20px 25px;box-shadow:0 0 18px rgba(0,0,0,0.25);}
        h2{margin-top:0;color:#1E88E5;}
        .form-row{margin-bottom:10px;}
        label{display:inline-block;width:120px;font-weight:bold;}
        input[type="text"],input[type="number"]{width:250px;padding:6px;border-radius:4px;border:1px solid #bbb;}
        .btn{padding:7px 14px;border-radius:5px;border:none;background:#1E88E5;color:#fff;cursor:pointer;}
        .btn:hover{background:#0D47A1;}
        table{width:100%;border-collapse:collapse;margin-top:20px;font-size:14px;}
        th,td{border-bottom:1px solid #ddd;padding:8px;text-align:left;}
        th{background:#f5f5f5;}
        a.action-link{color:#1E88E5;text-decoration:none;margin-right:8px;}
        a.action-link:hover{text-decoration:underline;}
    </style>
</head>
<body>
<div class="top-bar">
    <h1>Admin Panel</h1>
    <div class="top-links">
        <a href="ViewMoviesServlet">User View</a>
        <a href="AdminUsersServlet">Manage Users</a>
        <a href="LogoutServlet">Logout</a>
    </div>
</div>
<div class="container">
    <h2>Add New Movie</h2>
    <form action="AdminMovieServlet" method="post">
        <input type="hidden" name="action" value="add" />
        <div class="form-row">
            <label>Movie Name:</label>
            <input type="text" name="movieName" required />
        </div>
        <div class="form-row">
            <label>Ticket Price:</label>
            <input type="number" name="ticketPrice" required />
        </div>
        <div class="form-row">
            <label>Poster Path:</label>
            <input type="text" name="poster" placeholder="assets/posters/MyMovie.jpg" required />
        </div>
        <button type="submit" class="btn">Add Movie</button>
    </form>
    <h2 style="margin-top:25px;">Existing Movies</h2>
    <table>
        <tr><th>ID</th><th>Movie</th><th>Price</th><th>Available Seats</th><th>Poster</th><th>Actions</th></tr>
        <%
            List movies = (List) request.getAttribute("movies");
            if (movies != null && !movies.isEmpty()) {
                for (Object om : movies) {
                    Movie m = (Movie) om;
        %>
        <tr>
            <td><%= m.getMovieId() %></td>
            <td><%= m.getMovieName() %></td>
            <td>₹<%= m.getTicketPrice() %></td>
            <td><%= m.getAvailableSeats() %></td>
            <td><%= m.getPoster() %></td>
            <td>
                <a class="action-link" href="AdminMovieServlet?action=reset&movieId=<%= m.getMovieId() %>">Reset Seats</a>
                <a class="action-link" href="AdminMovieServlet?action=delete&movieId=<%= m.getMovieId() %>"
                   onclick="return confirm('Delete this movie?');">Delete</a>
            </td>
        </tr>
        <%      }
            } else { %>
        <tr><td colspan="6">No movies found.</td></tr>
        <% } %>
    </table>
</div>
</body>
</html>
