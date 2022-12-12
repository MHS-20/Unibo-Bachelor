<!-- pagina per la gestione di errori -->
<%@ page errorPage="../errors/failure.jsp"%>

<!-- accesso alla sessione -->
<%@ page session="true"%>

<!-- import di classi Java -->
<%@ page import="it.unibo.tw.web.beans.Cart"%>
<%@ page import="it.unibo.tw.web.beans.Item"%>
<%@ page import="it.unibo.tw.web.beans.Catalogue"%>
<%@ page import="java.util.List"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="javax.servlet.http.HttpSession"%>

<%!void save(Cart cart) {
		File file;
		PrintWriter pw;
		try {
			file = File.createTempFile("cart", ".txt");
			file.setWritable(true);
			pw = new PrintWriter(file);
		} catch (Exception e) {
			return;
		}

		for (Item itemInCart : cart.getItems()) {
			pw.write(itemInCart.getDescription() + " " + itemInCart.getPrice() + " " + itemInCart.getQuantity() + "\n");
		}

		pw.flush();
		pw.close();
		return;
	}

	void updateCatalogue(Cart cart, Catalogue catalogue) {
		%>
		
		<script>
		console.log("Bella");	
		</script>
		
		<%!
		for (Item itemInCart : cart.getItems()) {
			for (Item itemInCatalogue : catalogue.getItems()) {
				if (itemInCart.getDescription().equals(itemInCatalogue.getDescription()))
					itemInCatalogue.setQuantity(itemInCatalogue.getQuantity() - itemInCart.getQuantity());
			}
		}

		cart.empty(); //svuoto il carrello
	}%>

<html>
<head>
<meta name="Author" content="pisi79">
<title>Checkout JSP</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/default.css" type="text/css" />
</head>

<body>
	<%@ include file="../fragments/header.jsp"%>
	<%@ include file="../fragments/menu.jsp"%>

	<div id="main" class="clear">
		<jsp:useBean id="catalogue" class="it.unibo.tw.web.beans.Catalogue" scope="application" />
		<jsp:useBean id="cart" class="it.unibo.tw.web.beans.Cart" scope="session" />
			
		<%
		//HttpSession sess = request.getSession();
		if (request.getParameter("save") != null && request.getParameter("save").equals("checkout")) {
			save(cart);
			updateCatalogue(cart, catalogue); 
		}
		%>
		

		<div id="center" style="float: center; width: 48%">
			<p>Current cart:</p>
			<table class="formdata">
				<tr>
					<th style="width: 31%">Description</th>
					<th style="width: 31%">Price</th>
					<th style="width: 31%">Available quantity</th>
					<th style="width: 7%"></th>
				</tr>
				<%
				Item[] items = cart.getItems().toArray(new Item[0]);
				for (Item anItem : items) {
				%>
				<tr>
					<td><%=anItem.getDescription()%></td>
					<td><%=anItem.getPrice()%> &#8364;</td>
					<td><%=anItem.getQuantity()%></td>
				</tr>
				<%
				}
				%>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
			<input type="submit" name="save" value="checkout" style="width: 100%" />
		</div>
	</div>

	<%@ include file="../fragments/footer.jsp"%>

</body>
</html>