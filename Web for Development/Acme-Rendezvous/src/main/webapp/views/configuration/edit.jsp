
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
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


<form:form requestUri="configuration/administrator/edit.do" modelAttribute="configuration">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:label path="name">
		<spring:message code="configuration.name" />:
	</form:label>
	<form:input path="name" size="100px"/>
	<form:errors cssClass="error" path="name" />
	<br />
	
	<form:label path="banner">
		<spring:message code="configuration.banner" />:
	</form:label>
	<form:input path="banner" size="100px"/>
	<form:errors cssClass="error" path="banner" />
	<br />

	<form:label path="welcomeMessageEs">
		<spring:message code="configuration.welcomemessages" /> Es
	</form:label>
	<form:input path="welcomeMessageEs" size="100px"/>
	<form:errors cssClass="error" path="welcomeMessageEs" />
	<br />
	
	<form:label path="welcomeMessageEn">
		<spring:message code="configuration.welcomemessages" /> En
	</form:label>
	<form:input path="welcomeMessageEn" size="100px"/>
	<form:errors cssClass="error" path="welcomeMessageEn" />
	<br />	

	<input type="submit" name="save"
		value="<spring:message code="configuration.save" />" />&nbsp; 
		
    <acme:cancel url="/" code="configuration.cancel"/>
	<br />

</form:form>
