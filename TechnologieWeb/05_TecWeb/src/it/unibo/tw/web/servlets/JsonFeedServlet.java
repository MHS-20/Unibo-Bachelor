package it.unibo.tw.web.servlets;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.unibo.tw.web.beans.*;

public class JsonFeedServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson g;

	public void init() {
		g = new Gson();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String category = request.getParameter("category");
		FeedDb feedDb = new FeedDb();
		List<Feed> someFeeds = feedDb.find(category);

		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(someFeeds));
	}
}