<%@ page import="Dao.ProductDAO" %>
<%@ page import="Model.Product" %><%--
  Created by IntelliJ IDEA.
  User: gluncho
  Date: 6/25/2022
  Time: 2:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        ProductDAO dao = (ProductDAO) request.getServletContext().getAttribute("productDAO");
        Product product = dao.getProduct(request.getParameter("id"));
    %>
    <title> <%= product.getName()%></title>
</head>
<body>
    <h1> <%=product.getName()%></h1>
    <img src = "store-images/<%=product.getImageFilename()%>" alt="invalid image" >
    <form action = "ShoppingServlet" method = "post">
        <input type="hidden" id="productID" name = "productID" value=<%= product.getProductId() %> >
        <%=product.getPrice()+"$"%>
        <input type = "submit" value = "Add to Cart">
    </form>
</body>
</html>
