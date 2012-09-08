<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="contributor.edit.title" /></title>
</head>
<body>
		<form:form action="save.html" commandName="contributor">
			<form:hidden path="idContributor" />
			<div class="span-16">
				<form:label path="name"><spring:message code="contributor.form.name" /></form:label>
			</div>
			<div class="span-16">
				<form:input path="name" />
			</div>
			<div class="span-16">
				<form:label path="lastName"><spring:message code="contributor.form.lastName" /></form:label>
			</div>
			<div class="span-16">
				<form:input path="lastName" />
			</div>
			<div class="span-16">
				<form:label path="secondLastName"><spring:message code="contributor.form.secondLastName" /></form:label>
			</div>
			<div class="span-16">
				<form:input path="secondLastName" />
			</div>
			<div class="span-16">
				<form:button><spring:message code="form.save" /></form:button>
			</div>
		</form:form>
</body>
</html>