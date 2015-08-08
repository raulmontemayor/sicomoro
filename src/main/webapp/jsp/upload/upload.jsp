<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="movement.edit.title" /></title>
<title>Upload</title>
</head>
<body>
<form:form acceptCharset="UTF-8" action="upload.html" method="post" enctype="multipart/form-data">
		<div class="span-12">
			<input type="text" name="name" />
		</div>
		<div class="span-12">
			<input type="file" name="file" />
		</div>
		<div class="span-12">
			<input type="submit" name="Subir" />
		</div>
</form:form>
</body>
</html>