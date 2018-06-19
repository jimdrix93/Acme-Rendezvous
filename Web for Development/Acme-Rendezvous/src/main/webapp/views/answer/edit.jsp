<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="formularioPreguntas"
	method="post">

	<form:hidden path="rendezvous" />
	<form:hidden path="user" />

	<jstl:forEach items="${formularioPreguntas.cuestionario}" var="mapEntry">

		<br />
		<form:label path="cuestionario[${mapEntry.key}]">${mapEntry.key}</form:label>
		<form:input path="cuestionario[${mapEntry.key}]" />
	</jstl:forEach>

	<br />

	<acme:cancel url="/" code="comment.cancel" />
	<acme:submit name="save" code="comment.save" />


</form:form>