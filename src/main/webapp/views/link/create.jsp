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

<form:form action="${requestURI}" modelAttribute="link" method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="rendezvous" />
	
	
	<form:label path="linkedToRendezvous">
			<spring:message code="link.linkedToRendezvous" />
		</form:label>
		<form:select id="linkedToRendezvousId" path="linkedToRendezvous">
			<form:options items="${linkedToRendezvous}" itemValue="id" itemLabel="name" />
		</form:select>
		<form:errors cssClass="error" path="linkedToRendezvous" />
	
	<acme:cancel url="/" code="link.cancel"/>
	<acme:submit name="save" code="link.save"/>


</form:form>