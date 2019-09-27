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
	<div class="resultString">
		${successString}
	</div>
	<table>
		<tr>
			<th>Id</th>
			<th>First Name</th>
			<th>Last Name</th>
			<th>E-mail</th>
			<th>Created</th>
			<th>Phone Numbers</th>
			<th>Update</th>
			<th>Delete</th>
		</tr>
		<%-- getting attribute usersList from model --%>
		<c:forEach var="user" items="${usersList}">
			<tr>
				<td>${user.id}</td>
				<td>${user.firstName}</td>
				<td>${user.lastName}</td>
				<td>${user.eMail}</td>
				<td>${user.createdOn.format(formatter)}</td>
				<td>${user.phoneNumbers}</td>
				<td><a href="/SpringRest/update/${user.id}"class="update">Update</a></td>
				<td><a href="/SpringRest/delete/${user.id}"class="delete">Delete</a></td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<br>
	<hr noshade size="2" color="#4682B4" />
	<div class="action">
		<a href="/SpringRest/add-new-user">Add new user</a>
	</div>
	<div class="action">
		<a href="/SpringRest/find-user">Find user</a>
	</div>
	<div class="action">
		<a href="/SpringRest/">To the main page</a>
	</div>
	<hr noshade size="2" color="#4682B4" />
</div>
</body>
</html>
