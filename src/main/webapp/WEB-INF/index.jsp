<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<link href="<c:url value="/resources/style.css"/>" rel="stylesheet"></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>

</head>

<body>
	<div class="layout">
		<h1>Welcome</h1>
		<h2 id="center">Please, make a choice:</h2>
		<div id="welcomeActions">
		<hr noshade size="2" color="#4682B4" />
			<div class="action">
				<a href="/SpringRest/users">Show all users</a>
			</div>
			<div class="action">
				<a href="/SpringRest/add-new-user">Add new user</a>
			</div>
			<div class="action">
				<a href="/SpringRest/find-user">Find user</a>
			</div>
			<hr noshade size="2" color="#4682B4" />
		</div>
		
	</div>
</body>
</html>