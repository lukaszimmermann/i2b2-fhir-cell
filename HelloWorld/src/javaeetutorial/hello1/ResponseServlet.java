package javaeetutorial.hello1;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/response")
public class ResponseServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		try (PrintWriter out = response.getWriter()) {
	
						// then write the data of the response
						String username = request.getParameter("username"); 
						if (username != null && username.length()> 0) {
						out.println("<h2>Hello ResponseServlet, " + username + "!</h2>"); }
						
					} 
	}		
}