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
<%@ page import="java.util.Date"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table pagesize="5" class="displaytag" name="requests"
	requestURI="${requestURI}" id="row">

	<jstl:if test="${row.servicio.cancelled == true}">
		<jstl:set var="classTd" value="cancelled" />
	</jstl:if>

	<jstl:if test="${row.servicio.cancelled == false}">
		<jstl:set var="classTd" value="" />
	</jstl:if>
    
	<spring:message code="servicio.name" var="servicioTile" />
	<display:column property="servicio.name" title="${servicioTile}"  class="${classTd}"/>
	<spring:message code="request.comments" var="commentTile" />
	<display:column property="comments" title="${commentTile}"  class="${classTd}"/>
	<spring:message code="msg.rendezvous" var="rendezvousTitle" />
	<display:column property="rendezvous.name" title="${rendezvousTitle}"  class="${classTd}"/>

	<spring:message code="servicio.cancelled" var="cancelledStatus" />
	<display:column title="${cancelledStatus }" class="${classTd}">
		<jstl:choose>
			<jstl:when test="${row.servicio.cancelled eq true }">
				<spring:message code="servicio.yes" var="yes" />
				<jstl:out value="${yes }" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="servicio.no" var="no" />
				<jstl:out value="${no }" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>

	
	<spring:message code="servicio.category" var="servicioCategory" />
	<display:column property="servicio.category.name" title="${servicioCategory}" class="${classTd}"/>
	
</display:table>

<acme:backButton text="msg.back" />
<br />