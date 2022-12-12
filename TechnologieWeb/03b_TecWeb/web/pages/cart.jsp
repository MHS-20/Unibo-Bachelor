<!-- pagina per la gestione di errori -->
<%@ page errorPage="../errors/failure.jsp"%>

<!-- accesso alla sessione -->
<%@ page session="true"%>

<!-- import di classi Java -->
<%@ page import="it.unibo.tw.web.beans.Catalogue"%>
<%@ page import="it.unibo.tw.web.beans.Item"%>
<%@ page import="it.unibo.tw.web.beans.Cart"%>
<%@ page import="java.util.List"%>

<!-- metodi richiamati nel seguito -->
<%!void add(Cart cart, Item item) {
		boolean alreadyInCart = false;

		for (Item itemInCart : cart.getItems()) {
			if (itemInCart.getDescription().equals(item.getDescription())) {
				itemInCart.setQuantity(itemInCart.getQuantity() + item.getQuantity());
				alreadyInCart = true;
				break;
			}
		}

		if (!alreadyInCart) {
			cart.put(item, item.getQuantity());
		}
	}

	void remove(Cart cart, String description) {
		for (Item itemInCart : cart.getItems()) {
			if (itemInCart.getDescription().equals(description)) {
				cart.getItems().remove(itemInCart);
				break;
			}
		}
	}%>

<!-- codice html restituito al client -->
<html>
<head>
<meta name="Author" content="pisi79">
<title>Cart JSP</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/default.css" type="text/css" />
</head>

<body>

	<%@ include file="../fragments/header.jsp"%>
	<%@ include file="../fragments/menu.jsp"%>

	<div id="main" class="clear">

		<jsp:useBean id="catalogue" class="it.unibo.tw.web.beans.Catalogue"
			scope="application" />
		<jsp:useBean id="cart" class="it.unibo.tw.web.beans.Cart"
			scope="session" />

		<%
		String description = request.getParameter("description");

		if (description != null && !description.equals("")) {

			if (description.contains(" ")) {
				throw new Exception("Blanks are not allowed in the description field!");
			}

			if (request.getParameter("add") != null && request.getParameter("add").equals("submit item")) {
				Item item = new Item();
				item.setDescription(description);
				item.setPrice(Double.parseDouble(request.getParameter("price")));
				item.setQuantity(Integer.parseInt(request.getParameter("quantity")));
				add(cart, item);
			} else if (request.getParameter("remove") != null && request.getParameter("remove").equals("ok")) {
				remove(cart, description);
			}

		}
		%>

		<div id="left"
			style="float: left; width: 48%; border-right: 1px solid grey">

			<p>Add an item to the cart:</p>
			<form>
				<table>
					<tr>
						<td><label for="desc">Description:</label></td>
						<td><input type="text" name="description" /></td>
					</tr>
					<tr>
						<td><label for="price">Price (&#8364;):</label></td>
						<td><input type="text" name="price" /></td>
					</tr>
					<tr>
						<td><label for="price">Quantity:</label></td>
						<td><input type="text" name="quantity" /></td>
					</tr>
					<tr>
						<td colspan="2"><input type="submit" name="add"
							value="submit item" style="width: 100%" /></td>
					</tr>
				</table>
			</form>

		</div>

		<div id="right" style="float: right; width: 48%">

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
					<td><a
						href="?remove=ok&description=<%=anItem.getDescription()%>"> <img
							src="../images/remove.gif" alt="remove" />
					</a></td>
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
		</div>

		<div class="clear">
			<p>&nbsp;</p>
		</div>

	</div>

	<%@ include file="../fragments/footer.jsp"%>

</body>
</html>