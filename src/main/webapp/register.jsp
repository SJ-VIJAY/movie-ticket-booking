<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create an Account - Ticket Booking</title>
    <style>
        body{margin:0;padding:0;font-family:Arial,sans-serif;
             background:linear-gradient(135deg,#1E88E5,#43A047);
             height:100vh;display:flex;justify-content:center;align-items:center;}
        .register-container{background:#fff;width:380px;padding:30px 35px;border-radius:12px;
             box-shadow:0 0 18px rgba(0,0,0,0.25);text-align:center;}
        .register-container h2{margin-bottom:15px;color:#43A047;}
        label{display:block;text-align:left;margin-top:12px;font-size:15px;font-weight:bold;color:#333;}
        input[type="text"],input[type="email"],input[type="password"]{width:100%;padding:10px;
             margin-top:6px;border-radius:6px;border:1px solid #bbb;font-size:15px;}
        button{width:100%;padding:10px;background:#43A047;border:none;color:#fff;font-size:15px;
             font-weight:bold;border-radius:6px;margin-top:20px;cursor:pointer;}
        button:hover{background:#2E7D32;}
        .message{margin-bottom:10px;font-size:14px;display:block;}
        .error-msg{color:red;}
        .login-link{margin-top:15px;display:inline-block;color:#1E88E5;font-size:14px;text-decoration:none;}
        .login-link:hover{text-decoration:underline;}
    </style>
</head>
<body>
<div class="register-container">
    <h2>Create Account</h2>
    <%
        String err = request.getParameter("error");
        if ("1".equals(err)) { %>
        <span class="message error-msg">Something went wrong! Try again.</span>
    <% } %>
    <form action="RegisterServlet" method="post">
        <label>Name</label>
        <input type="text" name="name" required />
        <label>Email</label>
        <input type="email" name="email" required />
        <label>Password</label>
        <input type="password" name="password" required />
        <button type="submit">Register</button>
    </form>
    <a class="login-link" href="index.jsp">Back to Login</a>
</div>
</body>
</html>
