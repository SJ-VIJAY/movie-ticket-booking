<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Manage Users</title>
    <style>
        body{margin:0;font-family:Arial,sans-serif;
             background:linear-gradient(135deg,#1E88E5,#43A047);min-height:100vh;color:#fff;}
        .top-bar{padding:15px 30px;background:rgba(0,0,0,0.25);display:flex;justify-content:space-between;align-items:center;}
        .top-bar h1{margin:0;font-size:24px;}
        .top-links a{color:#fff;text-decoration:none;margin-left:12px;font-size:14px;font-weight:bold;}
        .top-links a:hover{text-decoration:underline;}
        .container{width:90%;max-width:900px;margin:20px auto;background:#fff;color:#333;
             border-radius:12px;padding:20px 25px;box-shadow:0 0 18px rgba(0,0,0,0.25);}
        h2{margin-top:0;color:#1E88E5;}
        table{width:100%;border-collapse:collapse;margin-top:10px;font-size:14px;}
        th,td{border-bottom:1px solid #ddd;padding:8px;text-align:left;}
        th{background:#f5f5f5;}
        form.inline{display:inline;}
        button{padding:4px 10px;border-radius:4px;border:none;cursor:pointer;font-size:12px;}
        .btn-make{background:#43A047;color:#fff;}
        .btn-remove{background:#E53935;color:#fff;}
    </style>
</head>
<body>
<div class="top-bar">
    <h1>Admin - Users</h1>
    <div class="top-links">
        <a href="AdminHomeServlet">Manage Movies</a>
        <a href="ViewMoviesServlet">User View</a>
        <a href="LogoutServlet">Logout</a>
    </div>
</div>
<div class="container">
    <h2>User List</h2>
    <table>
        <tr><th>ID</th><th>Name</th><th>Email</th><th>Admin?</th><th>Action</th></tr>
        <%
            List users = (List) request.getAttribute("users");
            if (users != null && !users.isEmpty()) {
                for (Object ou : users) {
                    User u = (User) ou;
        %>
        <tr>
            <td><%= u.getUserId() %></td>
            <td><%= u.getUserName() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.isAdmin() ? "Yes" : "No" %></td>
            <td>
                <% if (!u.isAdmin()) { %>
                <form class="inline" action="AdminUsersServlet" method="post">
                    <input type="hidden" name="action" value="makeAdmin" />
                    <input type="hidden" name="userId" value="<%= u.getUserId() %>" />
                    <button type="submit" class="btn-make">Make Admin</button>
                </form>
                <% } else { %>
                <form class="inline" action="AdminUsersServlet" method="post">
                    <input type="hidden" name="action" value="removeAdmin" />
                    <input type="hidden" name="userId" value="<%= u.getUserId() %>" />
                    <button type="submit" class="btn-remove">Remove Admin</button>
                </form>
                <% } %>
            </td>
        </tr>
        <%      }
            } else { %>
        <tr><td colspan="5">No users found.</td></tr>
        <% } %>
    </table>
</div>
</body>
</html>
