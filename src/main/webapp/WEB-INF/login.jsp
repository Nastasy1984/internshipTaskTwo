<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%-- Including class from jstl library to the jsp page --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>Login page</title>
<style type="text/css">
* {
	font-family: Helvetica, Verdana;
	color: #696969;
	margin-top: 0px;
}

h1 {
	color: #C70039;
	font-size: 30pt;
	font-weight: 500;
	text-align: center;
	margin-top: 20px;
}

h2 {
	color: #327CA2;
	font-size: 20pt;
	font-weight: 500;
	text-align: center;
	margin-top: 20px;
}

.layout {
	background-color: white;
	width: 80%;
	margin-right: auto;
	margin-left: auto;
	padding-top: 20px;
	padding-bottom: 20px;
	padding-right: 20px;
	padding-left: 20px;
	margin-bottom: 40px;
}

body {
	background-color: #0093b3;
}

input {
	height: 40px;
	font-size: 15pt;
	font-weight: 300;
	border-radius: 4px;
	margin-top: 10px;
	margin-bottom: 20px;
}

form {
	width: 80%;
	margin-right: auto;
	margin-left: auto;
}

.resultString {
	width: 90%;
	margin-right: auto;
	margin-left: auto;
	color: #C70039;
	margin-bottom: 10px;
}

input.submit {
	margin-top: 30px;
	margin-bottom: 30px;
	border-radius: 4px;
	background: none;
	text-decoration: none;
	border: 3px solid #4682B4;
	padding: 5px;
	width: 150px;
	height: 50px;
}

input.submit:hover {
	background-color: #4682B4;
	border-color: #4682B4;
	color: white;
}

label {
	color: #327CA2;
}
</style>
</head>

<body>
	<div class="layout">
	<h1>Welcome to my super secret application!</h1>
	<h2> Please, enter your login and password:</h2>
		<div class="resultString">
			<c:if test="${logout == true}"> 
			You have been log out successfully
			</c:if>
		</div>

		<div class = "resultString">
			<c:if test="${error == true}">  
			Wrong login or password
			</c:if>
		</div>
		
		<form name='loginForm' action="login" method='POST'>
			<label>Login:</label> 
			<br> 
			<input type='text' name='username' placeholder='User'> 
			<br> 
			<label>Password:</label> 
			<br>
			<input type='password' name='password' /> 
			<br> 
			<input class="submit" name="submit" type="submit" value="Log in" />
		</form>
	</div>
</body>
</html>