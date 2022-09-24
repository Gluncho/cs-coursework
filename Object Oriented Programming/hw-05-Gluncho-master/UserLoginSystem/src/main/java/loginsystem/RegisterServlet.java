package loginsystem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("createAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("manager");
        String name = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        if(manager.containsUser(name)){
            RequestDispatcher rd = request.getRequestDispatcher("nameIsInUse.jsp");
            rd.forward(request, response);
        }else{
            manager.addAccount(name, password);
            RequestDispatcher rd = request.getRequestDispatcher("loginSucceeded.jsp");
            rd.forward(request, response);
        }
    }
}
