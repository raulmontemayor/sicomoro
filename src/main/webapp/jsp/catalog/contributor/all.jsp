<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	function goToEdit(idContributor) {
		window.location.href = "/sicomoro/catalog/contributor/" + idContributor + "/edit.html";
	}
</script>
</head>
<body>
	${table}
	<a href="/sicomoro/catalog/contributor/new.html">Nuevo</a>
</body>
</html>