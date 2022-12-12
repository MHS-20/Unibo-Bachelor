package it.unibo.tw.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import it.unibo.tw.web.pojo.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;

public class CalculatorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private Random rand;

	@Override
	public void init() {
		gson = new Gson();
	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		String op = (String) request.getParameter("op");
		// calcolo ...
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Object result = engine.eval(op);
		response.getWriter().println(gson.toJson(result));
	}
}