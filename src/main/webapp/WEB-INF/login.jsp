<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login page</title>
</head>
<body>
	<div class="layout">
	<h1>Hi! Welcome to my super secret application. Please, enter your login and password:</h1>
	<form name='loginForm' action="login" method='POST'>
      <table>
         <tr>
            <td>Login:</td>
            <td><input type='text' name='username' value='user'></td>
         </tr>
         <tr>
            <td>Password:</td>
            <td><input type='password' name='password' /></td>
         </tr>
         <tr>
            <td><input name="submit" type="submit" value="Send" /></td>
         </tr>
      </table>
  </form>
	</div>
</body>
</html>