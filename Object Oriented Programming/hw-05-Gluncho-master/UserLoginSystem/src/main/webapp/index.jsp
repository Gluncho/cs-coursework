<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <title> Welcome </title>
    <h1>Welcome to Homework 5</h1>
    <p>Please log in.</p>
    <body>
        <form action = "LoginServlet" method = "post" style="line-height:250%">
            User name: <input type = "text" name = "username">
            <br>Password: <input type = "password"  name = "password">
            <input type = "submit"  name = "Login" value = "Login">
            <br><a href="createAccount.jsp">Create New Account</a>
        </form>
    </body>
</html>