<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page errorPage="../errors/failure.jsp"%>
<%@ page import="it.unibo.tw.web.beans.Feed"%>
<%@ page import="it.unibo.tw.web.beans.FeedDb"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.io.PrintWriter"%>

<!DOCTYPE html>

<jsp:useBean id="feedDb" class="it.unibo.tw.web.beans.FeedDb"
	scope="application" />

<%
String category = request.getParameter("category");
List<String> categories = feedDb.getCategories(category); //categorie dal prefisso

response.setContentType("text/plain");
PrintWriter output = response.getWriter(); //per scrivere risposta

if (!categories.isEmpty()) {
	response.setStatus(200);
	output.println(categories.get(0));
} else {
	response.setStatus(404);
}
%>

<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
</body>
</html>