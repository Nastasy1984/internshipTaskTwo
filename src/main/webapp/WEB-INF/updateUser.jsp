<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Update user</title>
<link href="<c:url value="/resources/style.css"/>" rel="stylesheet"></link>
	<script type="text/javascript">
		function ValidateInsert() {
			var specialChars = /[^a-z\'\-A-Z ]/g;
			if (document.updateForm.lastName.value.match(specialChars) || document.updateForm.firstName.value.match(specialChars)) {
				alert("Only characters A-Z, ', -, a-z in fields first name and last name are allowed!")
				return false;
			}
			if (document.updateForm.lastName.value == 0 || document.updateForm.firstName.value == 0) {
				alert("Fields first name and last name must be filled!")
				return false;
			}
			return (true);
		}
	</script>
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
		<div class="action">
			<a class="logout" href="/SpringRest/logout">Log out</a>
		</div>
	</div>
	<div class="layout">
		<h1>Update user with id ${id}</h1>
		<div class="resultString">${failString}</div>
		<form name = "updateForm" action="/SpringRest/update-user" method="POST" onSubmit="return ValidateInsert()">
			<%-- Hidden field for sending user's id --%>
			<input type="hidden" name="id" value="${id}"> 
			<label>First name</label> 
			<br>
			<input type="text" name="firstName" value="${user.firstName}">
			<br> 
			<label>Last name</label> 
			<br>
			<input type="text" name="lastName" value="${user.lastName}">
			<br> 
			<label>E-mail</label> 
			<br>
			<input type="text" name="eMail" value="${user.eMail}"> 
			<br>
			<label>Phone numbers:</label>
			<br>
			<input type="text" name="number" value="${user.phoneNumbers[0]}">
			<br>
			<input type="text" name="number" value="${user.phoneNumbers[1]}">
			<br>
			<input type="text" name="number" value="${user.phoneNumbers[2]}">
			<br>
			<input class="submit" type="submit" value="Update user">
		</form>

		<div class="footer">
			<hr noshade size="1" color="#327CA2" />
			<p>Created by Anastasiya Spiridonova, 2019</p>
		</div>

	</div>
</body>
</html>