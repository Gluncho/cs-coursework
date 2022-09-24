package Listeners;

import Model.Cart;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
       HttpSession session = se.getSession();
       session.setAttribute("cart", new Cart());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

}
