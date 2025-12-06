<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Now Showing - Ticket Booking</title>
    <style>
        body{margin:0;padding:0;font-family:Arial,sans-serif;
             background:linear-gradient(135deg,#1E88E5,#43A047);min-height:100vh;color:#fff;}
        .top-bar{padding:15px 30px;background:rgba(0,0,0,0.25);display:flex;justify-content:space-between;align-items:center;}
        .top-bar h1{margin:0;font-size:26px;letter-spacing:1px;}
        .top-links a{color:#fff;font-size:14px;text-decoration:none;margin-left:15px;font-weight:bold;}
        .top-links a:hover{text-decoration:underline;}
        .movie-container{width:90%;max-width:1200px;margin:25px auto;display:grid;
             grid-template-columns:repeat(auto-fill,minmax(240px,1fr));gap:25px;}
        .movie-card{background:#fff;color:#333;border-radius:12px;overflow:hidden;
             box-shadow:0 0 15px rgba(0,0,0,0.25);transition:0.3s;text-align:center;padding-bottom:15px;}
        .movie-card:hover{transform:scale(1.03);}
        .movie-card img{width:100%;height:300px;object-fit:cover;border-bottom:1px solid #ddd;}
        .movie-card h3{margin:10px 0 6px 0;font-size:20px;color:#1E88E5;}
        .movie-detail{font-size:14px;margin:3px 0;}
        .book-btn{display:inline-block;padding:8px 16px;border-radius:6px;background:#1E88E5;color:#fff;
             font-size:14px;font-weight:bold;margin-top:10px;text-decoration:none;transition:0.3s;}
        .book-btn:hover{background:#0D47A1;}
    </style>
</head>
<body>
<div class="top-bar">
    <h1>🎬 Now Showing</h1>
    <div class="top-links">
        <a href="HistoryServlet">My Bookings</a>
        <a href="AdminHomeServlet">Admin</a>
        <a href="LogoutServlet">Logout</a>
    </div>
</div>
<div class="movie-container">
<%
    java.util.List movies = (java.util.List) request.getAttribute("movies");
    if (movies != null) {
        for (Object om : movies) {
            model.Movie m = (model.Movie) om;
%>
    <div class="movie-card">
        <img src="<%= m.getPoster() %>" alt="Movie Poster" />
        <h3><%= m.getMovieName() %></h3>
        <div class="movie-detail">Price: ₹<%= m.getTicketPrice() %></div>
        <div class="movie-detail">Available Seats: <%= m.getAvailableSeats() %></div>
        <a class="book-btn" href="BookingServlet?movieId=<%= m.getMovieId() %>">Book Now</a>
    </div>
<%  }
    }
%>
</div>
</body>
</html>
