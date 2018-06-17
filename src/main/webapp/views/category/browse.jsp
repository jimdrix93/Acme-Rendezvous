<%--
 * list.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Listing grid -->

<div class="dropdown">
	<button onclick="dropDown()" class="dropbtn">
		<spring:message code="category.categories" />
	</button>
	<div id="myDropdown" class="dropdown-content">${tree}</div>
</div>
<div class="dropdown">
<security:authorize access="hasRole('USER')">

	<jstl:if test="${selected.id !=0}">
		<jstl:forEach var="level" items="${ruta}">
			<button
				onclick="window.location.href='category/user/browse.do?categoryId=${level.id}'"
				class="dropbtn">${level.name}</button>

		</jstl:forEach>
	</jstl:if>

</security:authorize>
<security:authorize access="hasRole('ADMINISTRATOR')">

	<jstl:if test="${selected.id !=0}">
		<jstl:forEach var="level" items="${ruta}">
			<button
				onclick="window.location.href='category/administrator/browse.do?categoryId=${level.id}'"
				class="dropbtn">${level.name}</button>>

		</jstl:forEach>
	</jstl:if>

</security:authorize>
<security:authorize access="isAnonymous()">

	<jstl:if test="${selected.id !=0}">
		<jstl:forEach var="level" items="${ruta}">
			<button
				onclick="window.location.href='category/browse.do?categoryId=${level.id}'"
				class="dropbtn">${level.name}</button>

		</jstl:forEach>
	</jstl:if>

</security:authorize>
</div>
<p>
	<security:authorize access="hasRole('ADMINISTRATOR')">
		<button
			onclick="window.location.href='category/administrator/create.do?parentId=${selected.id}'"
			class="dropbtn">
			<spring:message code="category.new" />
		</button>

		<jstl:if test="${selected.id !=0}">
			<button
				onclick="window.location.href='category/administrator/edit.do?categoryId=${selected.id}'"
				class="dropbtn">
				<spring:message code="msg.edit" />
			</button>
		</jstl:if>
	</security:authorize>
<security:authorize access="isAnonymous()">
	<jstl:if test="${selected.id !=0}">
		<button
			onclick="window.location.href='category/browse.do?categoryId=${selected.parentCategory.id}'"
			class="dropbtn">
			<spring:message code="msg.up" />
		</button>
	</jstl:if>
	</security:authorize>
<security:authorize access="hasRole('USER')">
	<jstl:if test="${selected.id !=0}">
		<button
			onclick="window.location.href='category/user/browse.do?categoryId=${selected.parentCategory.id}'"
			class="dropbtn">
			<spring:message code="msg.up" />
		</button>
	</jstl:if>
	</security:authorize>
<security:authorize access="hasRole('ADMINISTRATOR')">
	<jstl:if test="${selected.id !=0}">
		<button
			onclick="window.location.href='category/administrator/browse.do?categoryId=${selected.parentCategory.id}'"
			class="dropbtn">
			<spring:message code="msg.up" />
		</button>
	</jstl:if>
	</security:authorize>
</p>
<security:authorize access="!hasRole('ADMINISTRATOR')">
<h3>
	<jstl:if test="${selected.name !='-'}">
		<spring:message code="category.header.rendezvous.by.category" /> ${selected.name}: 
	</jstl:if>
	<jstl:if test="${selected.name =='-'}">
		<spring:message code="category.header.rendezvous.uncategorized" />
	</jstl:if>

</h3>

<%@include file="/views/rendezvous/list.jsp"%>	

</security:authorize>

			