
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="servicio/manager/edit.do" modelAttribute="servicio">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="cancelled" />

	<acme:textbox code="servicio.name" path="name" />
	<br />

	<acme:textbox code="servicio.description" path="description" />
	<br />

	<acme:textbox code="servicio.picture" path="picture" />
	<br />

	<acme:select items="${categories}" itemLabel="name"
		code="servicio.category" path="category" />
	<br />

	<acme:submit name="save" code="servicio.save" />

	<acme:cancel url="/" code="servicio.cancel" />

	<jstl:if test="${servicio.id!=0 }">

		<acme:submit name="delete" code="servicio.delete" />

	</jstl:if>




</form:form>

