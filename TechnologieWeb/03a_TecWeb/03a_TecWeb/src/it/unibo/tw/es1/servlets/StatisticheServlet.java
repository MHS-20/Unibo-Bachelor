
package it.unibo.tw.es1.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unibo.tw.es1.beans.Articolo;
import it.unibo.tw.es1.beans.InsiemeDiArticoli;

public class StatisticheServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// RequestDispatcher dispatcher =
		// getServletContext().getRequestDispatcher("/inServlet");
		
		int firstDay, lastDay;
		float guadagno = 0;
		String id = null;
		Cookie cid, cfirst, clast, cres;

		String richiesta = req.getParameter("req");
		if (richiesta != null && richiesta.equals("calcola")) {

			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/statistiche.jsp"); 
			ServletContext context = this.getServletContext();
			InsiemeDiArticoli articoli = (InsiemeDiArticoli) context.getAttribute("merceVenduta");
			Vector<Articolo> merce = articoli.getMerce();

			firstDay = Integer.parseInt(req.getParameter("firstDay"));
			lastDay = Integer.parseInt(req.getParameter("lastDay"));

			if (req.getParameter("id") != null)
				id = req.getParameter("id");

			for (Articolo ar : merce) {
				if (ar.getDay() >= firstDay && ar.getDay() <= lastDay) {
					if (id == null)
						guadagno += ar.getAmount() * ar.getPrice();
					else if (id.compareTo(ar.getId()) == 0)
						guadagno += ar.getAmount() * ar.getPrice();
				}
			}

			cid = new Cookie("id", id);
			cfirst = new Cookie("firstDay", firstDay + "");
			clast = new Cookie("lastDay", lastDay + "");
			cres = new Cookie("guadagno", guadagno + "");

			resp.addCookie(cid);
			resp.addCookie(cfirst);
			resp.addCookie(clast);
			resp.addCookie(cres);
			req.setAttribute("guadagno", guadagno);
			dispatcher.forward(req, resp);
		}
	}
}