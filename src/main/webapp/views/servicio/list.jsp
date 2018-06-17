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


<display:table pagesize="5" class="displaytag" name="servicios"
	requestURI="${requestURI}" id="row">

	<jstl:if test="${row.cancelled == true}">
		<jstl:set var="classTd" value="cancelled" />
	</jstl:if>

	<jstl:if test="${row.cancelled == false}">
		<jstl:set var="classTd" value="" />
	</jstl:if>
    
	<spring:message code="servicio.name" var="servicioName" />
	<display:column property="name" title="${servicioName}"  class="${classTd}"/>

	<spring:message code="servicio.cancelled" var="cancelledStatus" />
	<display:column title="${cancelledStatus }" class="${classTd}">
		<jstl:choose>
			<jstl:when test="${row.cancelled eq true }">
				<spring:message code="servicio.yes" var="yes" />
				<jstl:out value="${yes }" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="servicio.no" var="no" />
				<jstl:out value="${no }" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>

	<spring:message code="servicio.description" var="servicioDescription" />
	<display:column property="description" title="${servicioDescription}" class="${classTd}" />

	<spring:message code="comment.picture" var="picture" />
	<display:column  title="${picture}" class="${classTd}">
		<IMG src="${row.picture}" class="tableImg"/>
	</display:column>
	<spring:message code="servicio.category" var="servicioCategory" />
	<display:column property="category.name" title="${servicioCategory}" class="${classTd}"/>

	<security:authorize access="hasRole('MANAGER')">

		<jstl:if test="${mine eq true }">

			<display:column class="${classTd}">

				<a href="servicio/manager/edit.do?servicioId=${row.id}"> <spring:message
						code="servicio.edit" />
				</a>

			</display:column>


		</jstl:if>


	</security:authorize>

	<security:authorize access="hasRole('ADMINISTRATOR')">


		<display:column class="${classTd}">

			<jstl:if test="${row.cancelled == false}">
				<div>
					<a href="servicio/administrator/cancel.do?servicioId=${row.id}">
						<spring:message code="msg.cancel" />
					</a>
				</div>
			</jstl:if>
			<jstl:if test="${row.cancelled == true}">
				<div>
					<a href="servicio/administrator/cancel.do?servicioId=${row.id}"
						class="${classTd}"> <spring:message code="msg.undocancel" />
					</a>
				</div>
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>

<acme:backButton text="msg.back" />
<br />
