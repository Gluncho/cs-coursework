<%--
  Created by IntelliJ IDEA.
  User: gluncho
  Date: 6/17/2022
  Time: 12:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title> Information Incorrect </title>
<h1>Please Try Again</h1>
<p>Either your username or password is incorrect. Please try again</p>
<body>
<form action = "LoginServlet" method = "post" style="line-height:250%">
    User name: <input type = "text" name = "username">
    <br>Password: <input type = "password" name = "password">
    <input type = "submit"  name = "Login" value = "Login">
    <br><a href="createAccount.jsp">Create New Account</a>
</form>
</body>
</html>