<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

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

<form:form action="${requestURI}" modelAttribute="userAccount" >

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="authorities"/>
	
	<form:label path="username">
		<spring:message code="userAccount.username" />:
	</form:label>
	
	<form:input type="text" path="username"/>
	<form:errors cssClass="error" path="username"/>
<br/>

	<form:label path="password">
		<spring:message code="userAccount.password" />:
	</form:label>
	
	<form:password  path="password"/>
	<form:errors cssClass="error" path="password"/>
<br/>

	<input type="submit" name="save"
		value="<spring:message code="userAccount.create" />"/>
	
	<input type="button" name="cancel"
		value="<spring:message code="userAccount.cancel" />"
		onclick="javascript: relativeRedir('administrator/actor/list.do');" />



</form:form>
