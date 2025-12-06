<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Movie, model.Seat" %>
<%
    Movie movie = (Movie) request.getAttribute("movie");
    List<Seat> seats = (List<Seat>) request.getAttribute("seats");

    Integer movieId = null;
    String movieName = null;
    if (movie != null) {
        movieId = movie.getMovieId();
        movieName = movie.getMovieName();
    } else {
        String midParam = request.getParameter("movieId");
        movieName = request.getParameter("movieName");
        if (midParam != null && !midParam.isEmpty()) {
            try { movieId = Integer.parseInt(midParam); } catch (NumberFormatException e) { movieId = null; }
        }
    }

    String err = request.getParameter("error");
    int totalSeats = (seats != null) ? seats.size() : 0;
    int cols = 5;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Select Seats</title>
    <style>
        body{margin:0;padding:0;font-family:Arial,sans-serif;
             background:linear-gradient(135deg,#1E88E5,#43A047);
             min-height:100vh;display:flex;justify-content:center;align-items:center;}
        .seat-container{background:#fff;width:700px;max-width:95%;padding:25px 30px;border-radius:12px;
             box-shadow:0 0 10px rgba(0,0,0,0.25);}
        h2{text-align:center;color:#1E88E5;margin-top:0;margin-bottom:10px;}
        .sub-title{text-align:center;font-size:14px;color:#555;margin-bottom:15px;}
        .error-msg{text-align:center;margin-bottom:10px;font-size:14px;color:#D32F2F;}
        .legend{text-align:center;margin-bottom:15px;}
        .legend span{display:inline-block;padding:5px 10px;margin:0 5px;border-radius:5px;font-size:13px;}
        .legend .available{background:#9CCC65;}
        .legend .booked{background:#B0BEC5;}
        .legend .selected{background:#E53935;color:#fff;}
        .screen-label{text-align:center;font-size:13px;margin-bottom:8px;color:#555;}
        .screen-box{width:60%;margin:0 auto 15px auto;height:6px;background:#B0BEC5;border-radius:3px;}
        table{border-collapse:collapse;margin:0 auto;}
        th,td{width:45px;height:45px;text-align:center;border:1px solid #444;vertical-align:middle;font-size:14px;}
        th{background:#eee;}
        .seat-cell{cursor:pointer;}
        .seat-cell.available{background:#9CCC65;}
        .seat-cell.booked{background:#B0BEC5;color:#555;cursor:not-allowed;}
        .seat-cell.selected{background:#E53935 !important;color:#fff;}
        .btn-bar{text-align:center;margin-top:18px;}
        .btn-primary{padding:8px 18px;border-radius:6px;border:none;background:#1E88E5;color:#fff;
             font-size:14px;cursor:pointer;margin-right:10px;}
        .btn-primary:hover{background:#0D47A1;}
        a.back-link{color:#1E88E5;text-decoration:none;}
        a.back-link:hover{text-decoration:underline;}
    </style>
</head>
<body>
<div class="seat-container">
    <h2>Select Seats</h2>
    <div class="sub-title">Movie: <strong><%= movieName != null ? movieName : "" %></strong></div>
    <%
        if ("concurrent".equals(err)) { %>
        <div class="error-msg">Some seats were already booked by someone else. Please try again.</div>
    <% } else if ("noseat".equals(err)) { %>
        <div class="error-msg">Please select at least one seat.</div>
    <% } else if ("db".equals(err)) { %>
        <div class="error-msg">Database error occurred. Please try again later.</div>
    <% } %>
    <div class="legend">
        <span class="available">Available</span>
        <span class="booked">Booked</span>
        <span class="selected">Selected</span>
    </div>
    <div class="screen-label">Screen This Side</div>
    <div class="screen-box"></div>
    <form action="TicketConfirmServlet" method="post">
        <input type="hidden" name="movieId" value="<%= movieId != null ? movieId : 0 %>" />
        <input type="hidden" name="movieName" value="<%= movieName != null ? movieName : "" %>" />
        <table>
            <tr><th></th><th>1</th><th>2</th><th>3</th><th>4</th><th>5</th></tr>
            <%
                int index = 0;
                for (char row = 'A'; row <= 'E'; row++) {
            %>
            <tr>
                <th><%= row %></th>
                <%
                    for (int col = 1; col <= cols; col++) {
                        Seat seat = null;
                        if (index < totalSeats) seat = seats.get(index++);
                        if (seat != null) {
                            boolean isBooked = seat.isBooked();
                            String cellClass = isBooked ? "booked" : "available";
                            String label = seat.getSeatNo();
                %>
                <td class="seat-cell <%= cellClass %>">
                    <% if (!isBooked) { %>
                        <input type="checkbox" class="seat-checkbox" name="seatId" value="<%= seat.getSeatId() %>" /><br/>
                        <%= label %>
                    <% } else { %>
                        X<br/><%= label %>
                    <% } %>
                </td>
                <%
                        } else { %>
                <td class="seat-cell available">-</td>
                <%  } } %>
            </tr>
            <% } %>
        </table>
        <div class="btn-bar">
            <button type="submit" class="btn-primary">Confirm Booking</button>
            <a href="ViewMoviesServlet" class="back-link">Back to Movies</a>
        </div>
    </form>
</div>
<script>
document.addEventListener("DOMContentLoaded", function(){
    const checkboxes = document.querySelectorAll(".seat-checkbox");
    const seatCells = document.querySelectorAll(".seat-cell");
    checkboxes.forEach(cb => {
        cb.addEventListener("change", function(){
            const td = this.closest("td");
            if (!td) return;
            if (this.checked) td.classList.add("selected");
            else td.classList.remove("selected");
        });
    });
    seatCells.forEach(cell => {
        if (cell.classList.contains("booked")) return;
        cell.addEventListener("click", function(e){
            const cb = this.querySelector(".seat-checkbox");
            if (!cb) return;
            if (e.target === cb) return;
            cb.checked = !cb.checked;
            cb.dispatchEvent(new Event("change"));
        });
    });
    const form = document.querySelector("form");
    form.addEventListener("submit", function(e){
        const selected = document.querySelectorAll(".seat-checkbox:checked");
        if (selected.length === 0) {
            alert("Please select at least one seat.");
            e.preventDefault();
        }
    });
});
</script>
</body>
</html>
