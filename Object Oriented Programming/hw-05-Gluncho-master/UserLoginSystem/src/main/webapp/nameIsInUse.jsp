<%--
  Created by IntelliJ IDEA.
  User: gluncho
  Date: 6/17/2022
  Time: 12:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<title> Create Account </title>
<h1>The Name  <%= request.getParameter("username") %> is Already In Use</h1>
<p>Please enter another name and password.</p>
<body>
<form action = "RegisterServlet" method = "post" style="line-height:250%">
    User name: <input type = "text" name = "username">
    <br>Password: <input type = "password" name = "password">
    <input type = "submit"  name = "Login" value = "Login">
</form>
</body>
</html>