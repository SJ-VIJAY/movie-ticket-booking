<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Booking Confirmation - Ticket Booking</title>
    <style>
        body{margin:0;padding:0;font-family:Arial,sans-serif;
             background:linear-gradient(135deg,#1E88E5,#43A047);
             height:100vh;display:flex;justify-content:center;align-items:center;}
        .result-container{background:#fff;width:500px;max-width:95%;padding:25px 30px;
             border-radius:12px;box-shadow:0 0 18px rgba(0,0,0,0.25);}
        h2{text-align:center;color:#1E88E5;margin-top:0;margin-bottom:10px;}
        .detail-row{margin:6px 0;font-size:15px;}
        .detail-label{font-weight:bold;}
        .status-success{color:#2E7D32;font-size:16px;margin-bottom:10px;text-align:center;}
        .nav-links{text-align:center;margin-top:18px;}
        .nav-links a{margin:0 8px;font-size:14px;text-decoration:none;color:#1E88E5;font-weight:bold;}
        .nav-links a:hover{text-decoration:underline;}
        .divider{height:1px;background:#e0e0e0;margin:15px 0;}
    </style>
</head>
<body>
<%
    Integer ticketCount = (Integer) request.getAttribute("ticketCount");
    String movieName = (String) request.getAttribute("movieName");
    String seatList = (String) request.getAttribute("seatList");
    Integer totalPrice = (Integer) request.getAttribute("totalPrice");
    String bookingTime = (String) request.getAttribute("bookingTime");
%>
<div class="result-container">
    <h2>Booking Confirmation</h2>
    <%
        if (ticketCount != null && ticketCount > 0) {
    %>
    <div class="status-success">
        Successfully booked <strong><%= ticketCount %></strong> seat(s).
    </div>
    <div class="detail-row">
        <span class="detail-label">Movie:</span>
        <span><%= movieName != null ? movieName : "-" %></span>
    </div>
    <div class="detail-row">
        <span class="detail-label">Seats:</span>
        <span><%= seatList != null ? seatList : "-" %></span>
    </div>
    <div class="detail-row">
        <span class="detail-label">Total Price:</span>
        <span>₹<%= totalPrice != null ? totalPrice : 0 %></span>
    </div>
    <div class="detail-row">
        <span class="detail-label">Booked On:</span>
        <span><%= bookingTime != null ? bookingTime : "-" %></span>
    </div>
    <div class="divider"></div>
    <div class="nav-links">
        <a href="ViewMoviesServlet">Back to Movies</a>
        <a href="HistoryServlet">My Bookings</a>
        <a href="LogoutServlet">Logout</a>
    </div>
    <% } else { %>
    <div class="status-success" style="color:#D32F2F;">
        Booking details not found. Please try again from the movies page.
    </div>
    <div class="nav-links">
        <a href="ViewMoviesServlet">Back to Movies</a>
    </div>
    <% } %>
</div>
</body>
</html>
