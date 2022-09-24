package loginsystem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("manager");
        String name = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        if(manager.isValidAccount(name,password)){
            RequestDispatcher rd = request.getRequestDispatcher("loginSucceeded.jsp");
            rd.forward(request, response);
        }else{
            RequestDispatcher rd = request.getRequestDispatcher("loginFailed.jsp");
            rd.forward(request, response);
        }
    }
}
