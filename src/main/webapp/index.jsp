<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ticket Booking - Login</title>
    <style>
        body { margin:0; padding:0; font-family:Arial,sans-serif;
               background:linear-gradient(135deg,#1E88E5,#43A047);
               height:100vh; display:flex; justify-content:center; align-items:center; }
        .login-container { background:#fff; width:370px; padding:30px 35px;
               border-radius:12px; box-shadow:0 0 18px rgba(0,0,0,0.25); text-align:center; }
        .login-container h2 { margin-bottom:15px; color:#1E88E5; }
        label { display:block; text-align:left; margin-top:12px; font-size:15px;
               font-weight:bold; color:#333; }
        input[type="email"],input[type="password"]{ width:100%; padding:10px; margin-top:6px;
               border-radius:6px; border:1px solid #bbb; font-size:15px;}
        button{ width:100%; padding:10px; background:#1E88E5; border:none; color:#fff;
               font-size:15px; font-weight:bold; border-radius:6px; margin-top:20px; cursor:pointer;}
        button:hover{ background:#0D47A1;}
        .message{ margin-bottom:10px; font-size:14px; display:block;}
        .error-msg{ color:red;} .info-msg{ color:blue;} .success-msg{ color:green;}
        .register-link{ margin-top:15px; display:inline-block; color:#1E88E5; font-size:14px; text-decoration:none;}
        .register-link:hover{ text-decoration:underline; }
    </style>
</head>
<body>
<div class="login-container">
    <h2>Login</h2>
    <%
        String reg = request.getParameter("registered");
        String loginParam = request.getParameter("login");
        if ("1".equals(reg)) { %>
        <span class="message success-msg">Registration successful. Please login.</span>
    <% }
        if ("failed".equals(loginParam)) { %>
        <span class="message error-msg">Invalid email or password.</span>
    <% } else if ("required".equals(loginParam)) { %>
        <span class="message info-msg">Please login to continue.</span>
    <% } %>
    <form action="LoginServlet" method="post">
        <label>Email</label>
        <input type="email" name="email" required />
        <label>Password</label>
        <input type="password" name="password" required />
        <button type="submit">Login</button>
    </form>
    <a class="register-link" href="RegisterServlet">Create New Account</a>
</div>
</body>
</html>
