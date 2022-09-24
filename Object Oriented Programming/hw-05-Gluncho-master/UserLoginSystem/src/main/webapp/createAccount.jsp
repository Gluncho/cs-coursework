<%--
  Created by IntelliJ IDEA.
  User: gluncho
  Date: 6/17/2022
  Time: 12:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Create Account</title>
<h1>Create New Account</h1>
<p>Please enter proposed name and password.</p>
<body>
<form action = "RegisterServlet" method = "post" style="line-height:250%">
    User name: <input type = "text" name = "username">
    <br>Password: <input type = "text" name = "password">
    <input type = "submit"  name = "Login" value = "Register">
</form>
</body>
</html>
