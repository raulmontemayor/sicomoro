<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Catalogo de contribuidores</title>
</head>
<body>
	<form:form action="save.html" commandName="contributor">
		<form:hidden path="idContributor" />
		<form:label path="name">Nombre:</form:label><form:input path="name" />
		<br />
		<form:label path="lastName">Apellido Paterno:</form:label><form:input path="lastName" />
		<br />
		<form:label path="secondLastName">Apellido Materno:</form:label><form:input path="secondLastName" />
		<br />
		<form:button>Guardar</form:button>
	</form:form>
</body>
</html>