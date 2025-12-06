<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Booking History - Ticket Booking</title>
    <style>
        body{margin:0;padding:0;font-family:Arial,sans-serif;
             background:linear-gradient(135deg,#1E88E5,#43A047);
             min-height:100vh;display:flex;justify-content:center;align-items:center;}
        .history-container{background:#fff;width:800px;max-width:95%;padding:25px 30px;
             border-radius:12px;box-shadow:0 0 18px rgba(0,0,0,0.25);}
        .header-row{display:flex;justify-content:space-between;align-items:center;margin-bottom:10px;}
        h2{margin:0;color:#1E88E5;}
        .top-links a{color:#1E88E5;font-size:14px;text-decoration:none;margin-left:12px;font-weight:bold;}
        .top-links a:hover{text-decoration:underline;}
        .divider{height:1px;background:#e0e0e0;margin:12px 0 18px 0;}
        table{width:100%;border-collapse:collapse;font-size:14px;}
        th,td{padding:10px 8px;text-align:left;border-bottom:1px solid #eee;}
        th{background:#f5f5f5;color:#333;}
        tr:nth-child(even) td{background:#fafafa;}
        tr:hover td{background:#e3f2fd;}
        .no-data{text-align:center;padding:15px 0;color:#777;font-style:italic;}
    </style>
</head>
<body>
<div class="history-container">
    <div class="header-row">
        <h2>Your Bookings</h2>
        <div class="top-links">
            <a href="ViewMoviesServlet">Back to Movies</a>
            <a href="LogoutServlet">Logout</a>
        </div>
    </div>
    <div class="divider"></div>
    <table>
        <tr><th>Movie</th><th>Seat</th><th>Date</th></tr>
        <%
            java.util.List bookings = (java.util.List) request.getAttribute("bookings");
            if (bookings != null && !bookings.isEmpty()) {
                for (Object ob : bookings) {
                    model.Booking b = (model.Booking) ob;
        %>
        <tr>
            <td><%= b.getMovieName() %></td>
            <td><%= b.getSeatNo() %></td>
            <td><%= b.getBookedOn() %></td>
        </tr>
        <%      }
            } else { %>
        <tr><td colspan="3" class="no-data">No bookings found.</td></tr>
        <% } %>
    </table>
</div>
</body>
</html>
