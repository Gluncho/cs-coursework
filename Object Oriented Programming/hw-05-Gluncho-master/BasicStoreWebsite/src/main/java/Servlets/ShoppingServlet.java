package Servlets;

import Dao.ProductDAO;
import Model.Cart;
import Model.Product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ShoppingServlet", value = "/ShoppingServlet")
public class ShoppingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("shopping-cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        ProductDAO dao = (ProductDAO) req.getServletContext().getAttribute("productDAO");
        Product product = dao.getProduct(req.getParameter("productID"));
        if(product != null){
            Cart cart = (Cart) session.getAttribute("cart");
            cart.addToCart(product);
        }else{
            Map<String, String[]> productMap = req.getParameterMap();
            Cart cart = (Cart) req.getSession().getAttribute("cart");
            Map<String, Integer> data = cart.getData();
            for(String id : data.keySet()){
                int frequency = Integer.parseInt(productMap.get(id)[0]);
                cart.updateFrequency(id, frequency);
            }
        }
        resp.sendRedirect("shopping-cart.jsp");
        //req.getRequestDispatcher("shopping-cart.jsp").forward(req, resp);
    }
}
