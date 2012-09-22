<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" type="image/x-icon" href="/sicomoro/resources/favicon.ico" />
<title>Sicomoro <decorator:title /></title>
<link rel="stylesheet" href="/sicomoro/resources/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/sicomoro/resources/blueprint/print.css" type="text/css" media="print">
<!--[if lt IE 8]>
  <link rel="stylesheet" href="/sicomoro/resources/blueprint/ie.css" type="text/css" media="screen, projection">
<![endif]-->
<link rel="stylesheet" href="/sicomoro/resources/pepper-grinder/jquery-ui-1.8.22.custom.css"></link>
<link rel="stylesheet" href="/sicomoro/resources/jmesa/jmesa.css"></link>
<style type="text/css">

.loadingDiv {
    display:    none;
    position:   fixed;
    z-index:    1000;
    top:        0;
    left:       0;
    height:     100%;
    width:      100%;
    background: rgba( 255, 255, 255, .8 ) 
                url('http://i.stack.imgur.com/FhHRx.gif') 
                50% 50% 
                no-repeat;
}

/* When the body has the loading class, we turn
   the scrollbar off with overflow:hidden */
body.loading {
    overflow: hidden;   
}

/* Anytime the body has the loading class, our
   modal element will be visible */
body.loading .loadingDiv {
    display: block;
}
</style>
<script type="text/javascript" src="/sicomoro/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/sicomoro/js/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="/sicomoro/js/jmesa.min.js"></script>
<script type="text/javascript" src="/sicomoro/js/jquery.jmesa.min.js"></script>
<script type="text/javascript" src="/sicomoro/js/jquery.numeric.js"></script>
<script type="text/javascript">
	$(document).ajaxStart(function(){
		$("body").addClass("loading");
	});
	$(document).ajaxStop(function(){
		$("body").removeClass("loading");
	});
	$(function(){
		$(".decimal").numeric({ precision: 2 });
	});
</script>
<decorator:head />
</head>
<body>
	<div class="container">
		<div class="span-24 large last">
			<a href="/sicomoro/catalog/contributor/all.html"><spring:message code="menu.catalog.contributor" /></a>
			<a href="/sicomoro/movement/new.html"><spring:message code="menu.movement.title" /></a>
			<a href="/sicomoro/logout.html"><spring:message code="menu.logout" /></a>
		</div>
		<div class="span-24 last"><decorator:title /></div>
		<div class="span-24 last">
			<decorator:body />
		</div>
		<div class="span-24 last">
			<div class="loadingDiv"><spring:message code="form.loading" /></div>
		</div>
	</div>
</body>
</html>