<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Find user</title>
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
			<a class="action" href="/SpringRest/show-all-users">Show all users</a>
		</div>
		<div class="action">
			<a class="logout" href="/SpringRest/logout">Log out</a>
		</div>
	</div>

	<div class="layout">
		<h1>Find user</h1>
		<div class="resultString">${failString}</div>
		<div class="h2">
			<h2>Enter user's ID or last Name:</h2>
		</div>
		<%-- <form action="/SpringRest/find-user" method="GET"> --%>
		<%-- instead of this we just make JS function that create new url with adding user's id --%>

		<script type="text/javascript">
		function myFunction(){
			<%--let actionSrc = "/SpringRest/find-user/" + document.getElementsByName("id")[0].value;--%>
			let actionSrc = "/SpringRest/find-user/" + document.getElementById("idUser").value
		    let findUserForm = document.getElementById('find_user_form');
		    findUserForm.action = actionSrc;
		}
		</script>

		<form id="find_user_form" onsubmit="myFunction()">
			<label>Id or last name</label>
			<br> 
			<%--<input type="text" name="id" placeholder="User's ID or last Name"> --%>
			<input type="text" id="idUser" placeholder="User's ID or last Name">
			<br> 
			<input class="submit" type="submit" value="Find user">
		</form>





		<div class="footer">
			<hr noshade size="1" color="#327CA2" />
			<p>Created by Anastasiya Spiridonova, 2019</p>
		</div>

	</div>
</body>
</html>