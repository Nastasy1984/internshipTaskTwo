<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add user</title>
<link href="<c:url value="/resources/style.css"/>" rel="stylesheet"></link>
	<script type="text/javascript">
		function ValidateInsert() {
			var specialChars = /[^a-z\'\-A-Z ]/g;
			if (document.addForm.lastName.value.match(specialChars) || document.addForm.firstName.value.match(specialChars)) {
				alert("Only characters A-Z, ', -, a-z in fields first name and last name are allowed!")
				return false;
			}
			if (document.addForm.lastName.value == 0 || document.addForm.firstName.value == 0) {
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
		<h1>Add new user</h1>
		<div class="resultString">${failString}</div>
		<form name = "addForm" id="addForm" action="/SpringRest/add-new-user" method="POST" onSubmit="return ValidateInsert()">
			<label>First name</label>
			<br> 
			<input type="text" name="firstName" placeholder="First Name" value="${user.firstName}"> 
			<br> 
			<label>Last name:</label> 
			<br>
			<input type="text" name="lastName" placeholder="Last Name" value="${user.lastName}"> 
			<br> 
			<label>E-mail:</label>
			<br>
			<input type="text" name="eMail" placeholder="E-mail" value="${user.eMail}"> 
			<br> 
			<label>Phone numbers:</label>
			<br>
			<input type="text" name="number" placeholder="0-000-000-00-00" value="${user.phoneNumbers[0]}">
			<br>
			<input type="text" name="number" placeholder="0-000-000-00-00" value="${user.phoneNumbers[1]}">
			<br>
			<input type="text" name="number" placeholder="0-000-000-00-00" value="${user.phoneNumbers[2]}">
			<br>
			<input class="submit" type="submit" value="Add user">
		</form>


		<div class="footer">
			<hr noshade size="1" color="#327CA2" />
			<p>Created by Anastasiya Spiridonova, 2019</p>
		</div>
	</div>
</body>

</html>