<%--
  Created by IntelliJ IDEA.
  User: gluncho
  Date: 6/17/2022
  Time: 12:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Welcome <%= request.getParameter("username") %> </title>
<h1> Welcome <%= request.getParameter("username") %>
</h1>
</html>