<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="movementcut.edit.title" /></title>
<script type="text/javascript"
	src="/sicomoro/js/controller/movementcut.js"></script>
</head>
<body>
	<form:form acceptCharset="UTF-8" action="save.html"
		commandName="movementCutForm">
		<form:hidden path="movementCut.idMovementCut" />
		<div class="span-12">
			<form:label path="movementCut.cutDate">
				<spring:message code="movementCut.form.cutDate" />
			</form:label>
		</div>
		<div class="span-12 last">
			<form:label path="movementCut.description">
				<spring:message code="form.description" />
			</form:label>
		</div>
		<div class="span-12">
			<form:input cssClass="date" path="movementCut.cutDate" />
		</div>
		<div class="span-12 last">
			<form:input path="movementCut.description" />
		</div>
		<!-- Segunda Línea -->
		<div class="span-16 append-8 last">
			<form:button>
				<spring:message code="form.save" />
			</form:button>
		</div>

	</form:form>
</body>
</html>