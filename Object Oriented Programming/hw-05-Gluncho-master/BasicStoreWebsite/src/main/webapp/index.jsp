<%@ page import="Dao.ProductDAO" %>
<%@ page import="Model.Product" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Store</title>
</head>
<body>
<h1> Student Store </h1>
<br/>
<p>Items available:</p>
<ul>
    <%
        ProductDAO productDAO = (ProductDAO) request.getServletContext().getAttribute("productDAO");
        PrintWriter pw = response.getWriter();
        for(Product product : productDAO.getAllProducts()){
            out.println("<li> <a href =\"show-product.jsp?id="+product.getProductId()+"\">" + product.getName() + "</a> </li>");
        }
    %>
</ul>
</body>
</html>