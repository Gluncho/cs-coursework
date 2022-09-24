<%@ page import="Model.Cart" %>
<%@ page import="Model.Product" %>
<%@ page import="java.util.Map" %>
<%@ page import="Dao.ProductDAO" %><%--
  Created by IntelliJ IDEA.
  User: gluncho
  Date: 6/29/2022
  Time: 11:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        ProductDAO dao = (ProductDAO) request.getServletContext().getAttribute("productDAO");
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        Map<String, Integer> data = cart.getData();
        double totalCost = 0;
    %>
    <title>Shopping Cart</title>
</head>
<h1>Shopping Cart</h1>
<body>
<form action="ShoppingServlet" method="post">
    <ul>
    <%
        for(String id : data.keySet()){
            Product p = dao.getProduct(id);
            int frequency = data.get(id);
            totalCost += frequency*p.getPrice();
    %>
        <li>
            <label>
                <input name="<%=id%>" type="number" value="<%=frequency%>">
                <%=p.getName() + ", " + p.getPrice()%>
            </label>
        </li>
    <%
        }
    %>
    </ul>
    Total: $<%=totalCost%> <input type="submit" value="Update Cart">
</form>
<a href="index.jsp">Continue Shopping</a>
</body>
</html>
