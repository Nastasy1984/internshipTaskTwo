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
	<div class="layout">
		<h1>Find user</h1>

		<div class="h2">
			<h2>Find user by id:</h2>
		</div>
<%-- <form action="/SpringRest/find-user" method="GET"> --%>
<%-- instead of this we just make JS function that create new url with adding user's id --%>

		<script type="text/javascript">
		function myFunction(){
		    let actionSrc = "/SpringRest/find-user/" + document.getElementsByName("id")[0].value;
		    let addUserForm = document.getElementById('add_user_form');
		    addUserForm.action = actionSrc ;
		}
		</script>

		<form id = "add_user_form" onsubmit="myFunction()">
			<label>Id</label> <br> <input type="text" name="id" placeholder="Enter user's ID"> 
			<br> 
			<input class="submit" type="submit" value="Find user">
		</form>
		<hr noshade size="2" color ="#4682B4"/>
		<div class="h2">
			<h2>Find user by last name:</h2>
		</div>

		<form action="/SpringRest/find-user-by-last-name" method="GET">
			<label>Last Name</label> <br> <input type="text" name="lastName" placeholder="Enter user's last name"> 
			<br> 
			<input class="submit" type="submit" value="Find user">
		</form>
		
	</div>
</body>
</html>