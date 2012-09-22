<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="movement.edit.title" /></title>
<script type="text/javascript" src="/sicomoro/js/controller/movement.js"></script>
</head>
<body>
	<form:form action="save.html" commandName="movementForm">
		<form:hidden path="movement.idMovement" />
		<div class="span-16">
			<form:label path="movement.amount">
				<spring:message code="movement.form.amount" />
			</form:label>
		</div>
		<div class="span-16">
			<form:input path="movement.amount" />
		</div>
		<div class="span-16">
			<form:button>
				<spring:message code="form.save" />
			</form:button>
		</div>
		<form:select path="movementType">
			<form:options items="${movementTypes}" />
		</form:select>
		<form:select cssClass="hide" path="movement.idContributor"></form:select>
	</form:form>
</body>
</html>