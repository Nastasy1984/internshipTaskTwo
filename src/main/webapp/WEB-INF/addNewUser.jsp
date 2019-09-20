<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add user</title>
<link href="<c:url value="/resources/style.css"/>" rel="stylesheet"></link>
</head>
<body>
<div class="layout">
	<h1>Add new user</h1>
	<div class="resultString">
		${failString}
	</div>
	<form action="/SpringRest/add-new-user" method="POST">
		<label>First name</label> <input type="text" name="firstName">
		<br>
		<label>Last name</label> <input type="text" name="lastName"> 
		<br>
		<input class="submit" type="submit" value="Add user">
	</form>

		<hr noshade size="2" color="#4682B4" />
		<div class="action">
			<a href="/SpringRest/show-all-users">Show all users</a>
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