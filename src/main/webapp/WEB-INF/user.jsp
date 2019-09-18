<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<link href="<c:url value="/resources/style.css"/>" rel="stylesheet"></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Users list</title>


</head>

<body>
<div class="layout">
	<h1>Users</h1>
	<table>
		<tr>
			<th>Id</th>
			<th>First Name</th>
			<th>Last Name</th>
		</tr>
		<%-- getting attribute usersList from model --%>
		<c:forEach var="user" items="${usersList}">
			<tr>
				<td>${user.id}</td>
				<td>${user.firstName}</td>
				<td>${user.lastName}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<br>
	<div class="action">
		<a href="/SpringRest/add-new-user">Add new user</a>
	</div>
	<div class="action">
		<a href="/SpringRest/find-user">Find user</a>
	</div>
</div>
</body>
</html>
