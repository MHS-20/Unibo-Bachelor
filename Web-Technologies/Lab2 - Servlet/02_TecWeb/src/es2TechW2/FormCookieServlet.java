package es2TechW2;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormCookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String firstName = null, lastName = null;

// ----------------------- RESPONSE -----------------------
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Request Parameters Example</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h3>Request Parameters Example</h3>");
		out.println("Parameters in this request:<br>");
		if (firstName != null || lastName != null) {
			out.println("First Name:");
			out.println(" = " + firstName + "<br>");
			out.println("Last Name:");
			out.println(" = " + lastName);
		} else {
			out.println("No Parameters, Please enter some");
		}
		out.println("<P>");
		out.print("<form action=\"");
		out.print("Cookie\" "); //nome servlet (file web xml)
		out.println("method=POST>");
		out.println("First Name:");
		out.println("<input type=text size=20 name=firstname>");
		out.println("<br>");
		out.println("Last Name:");
		out.println("<input type=text size=20 name=lastname>");
		out.println("<br>");
		out.println("<input type=submit>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String firstName = null, lastName = null;
		//Cookie[] cookies = request.getCookies();

		firstName = request.getParameter("firstname");
		lastName = request.getParameter("lastname");

		Cookie cookie1 = new Cookie("firstname", firstName);
		Cookie cookie2 = new Cookie("surname", lastName);
		response.addCookie(cookie1);
		response.addCookie(cookie2);

		if (firstName != null || lastName != null) {
			out.println("First Name:");
			out.println(" = " + firstName + "<br>");
			out.println("Last Name:");
			out.println(" = " + lastName);
		} else {
			out.println("No Parameters, Please enter some");
		}
	}
}