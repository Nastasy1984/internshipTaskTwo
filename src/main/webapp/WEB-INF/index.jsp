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
	<div class="flex-container">
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
		<div class="action">
			<a class="logout" href="/SpringRest/logout">Log out</a>
		</div>
	</div>
	<div class="layout">
		<h1>Please, make a choice:</h1>

		<div id="welcomeActions">

			<div class="menu">
				<a class="menu" href="/SpringRest/show-all-users">Show all users</a>
			</div>
			<div class="menu">
				<a class="menu" href="/SpringRest/add-new-user">Add new user</a>
			</div>
			<div class="menu">
				<a class="menu" href="/SpringRest/find-user">Find user</a>
			</div>

		</div>
		<div class="footer">
			<hr noshade size="1" color="#327CA2" />
			<p>Created by Anastasiya Spiridonova, 2019</p>
		</div>
	</div>
</body>
</html>