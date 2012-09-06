<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" type="image/x-icon" href="/sicomoro/resources/favicon.ico" />
<link rel="stylesheet" href="/sicomoro/resources/blueprint/screen.css"
	type="text/css" media="screen, projection">
<link rel="stylesheet" href="/sicomoro/resources/blueprint/print.css"
	type="text/css" media="print">
<!--[if lt IE 8]>
	  <link rel="stylesheet" href="/sicomoro/resources/blueprint/ie.css" type="text/css" media="screen, projection">
	<![endif]-->
</head>
<body onload='document.f.j_username.focus();'>

	<div class="container">
		<div class="span-24">
			<span class="large">Bienvenido</span>
			<c:if test="${not empty error}">
				<div class="error">
					Login no exitoso, intenta de nuevo.<br /> Causa:
					${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
				</div>
			</c:if>
		</div>
		<div class="span-4"></div>
		<div class="span-16 last">
			<form name='f' action="<c:url value='j_spring_security_check' />"
				method='POST'>
				<table>
					<tr>
						<td>Usuario:</td>
						<td><input type='text' name='j_username' value=''></td>
					</tr>
					<tr>
						<td>Contraseña:</td>
						<td><input type='password' name='j_password' /></td>
					</tr>
					<tr>
						<td><input name="submit" type="submit"
							value="Entrar" /></td>
						<td><input name="reset" type="reset" value="Limpiar" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>