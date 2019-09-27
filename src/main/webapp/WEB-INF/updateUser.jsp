<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Update user</title>
<link href="<c:url value="/resources/style.css"/>" rel="stylesheet"></link>
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
		<h1>Update user with id ${id}</h1>
		<div class="resultString">${failString}</div>
		<form action="/SpringRest/update-user" method="POST">
			<%-- Hidden field for sending user's id --%>
			<input type="hidden" name="id" value="${id}"> <label>First
				name</label> <br>
			<input type="text" name="firstName" value="${user.firstName}">
			<br> <label>Last name</label> <br>
			<input type="text" name="lastName" value="${user.lastName}">
			<br> <label>E-mail</label> <br>
			<input type="text" name="eMail" value="${user.eMail}"> <br>
			<input class="submit" type="submit" value="Update user">
		</form>

		<div class="footer">
			<hr noshade size="1" color="#327CA2" />
			<p>Created by Anastasiya Spiridonova, 2019</p>
		</div>

	</div>
</body>
</html>