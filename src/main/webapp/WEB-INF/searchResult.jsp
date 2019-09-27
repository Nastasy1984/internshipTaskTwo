<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>

<link href="<c:url value="/resources/style.css"/>" rel="stylesheet"></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search result</title>

<%-- Declaration of variable --%>
<%! java.time.format.DateTimeFormatter formater = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); %>
</head>

<body>
	<div class="flex-container">
		<div class="action">
			<a class="action" href="/SpringRest/">To the main page</a>
		</div>
		<div class="action">
			<a class="action" href="/SpringRest/add-new-user">Add user</a>
		</div>
		<div class="action">
			<a class="action" href="/SpringRest/show-all-users">Show all
				users</a>
		</div>
		<div class="action">
			<a class="action" href="/SpringRest/find-user">Find user</a>
		</div>
	</div>
	<div class="layout">
		<h1>Search result</h1>

		<table>
			<tr>
				<th>Id</th>
				<th>First Name</th>
				<th>Last Name</th>
				<th>E-Mail</th>
				<th>Created</th>
				<th>Phone Numbers</th>
				<th>Update</th>
				<th>Delete</th>
			</tr>
			<%-- getting attribute usersList from model --%>
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.id}</td>
					<td>${user.firstName}</td>
					<td>${user.lastName}</td>
					<td>${user.eMail}</td>

					<%-- <td>${localDateTimeFormat.parse(user.createdOn)}</td>--%>
					<td>${user.createdOn.format(formatter)}</td>
					<%-- <td>${user.phoneNumbers}</td>--%>
					<td><c:forEach var="number" items="${user.phoneNumbers}">
					${number}
					<br>
						</c:forEach></td>
					<td><a href="/SpringRest/update/${user.id}" class="update">Update</a></td>
					<td><a href="/SpringRest/delete/${user.id}" class="delete">Delete</a></td>
				</tr>
			</c:forEach>
		</table>

		<div class="footer">
			<hr noshade size="1" color="#327CA2" />
			<p>Created by Anastasiya Spiridonova, 2019</p>
		</div>
	</div>
</body>
</html>
