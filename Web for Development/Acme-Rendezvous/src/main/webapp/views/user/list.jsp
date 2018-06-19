<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="org.hibernate.engine.spi.RowSelection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table pagesize="5" class="displaytag" name="users" requestURI="${requestUri}" id="row">
	
	<spring:message code="user.name" var="userName" />
	<display:column property="name" title="${userName}" sortable="true" />

	<spring:message code="user.surname" var="userSurname" />
	<display:column property="surname" title="${userSurname}" sortable="true" />
		
	<display:column>
		<div>
			<a href="user/display.do?userId=${row.id}"> <spring:message
 				code="user.display" />
			</a>
		</div>
	</display:column>
	
	
	
	<jstl:if test="${showAnswer}">
	<display:column>
		<div>
			<a href="answer/list.do?userId=${row.id}&rendezvousId=${rendezvousId}"> 
				<spring:message code="user.question.answer" />
			</a>
		</div>
	</display:column>
	</jstl:if>
	
	
	
</display:table>

<acme:backButton text="msg.back" />
<br />	
	
