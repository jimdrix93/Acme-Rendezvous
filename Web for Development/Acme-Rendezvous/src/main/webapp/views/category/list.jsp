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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Listing grid -->

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="categories" requestURI="${requestUri}" id="row">

	<jstl:choose>
		<jstl:when test="${admin==true}">
			<spring:message code="category.name" var="nameHeader" />
			<display:column title="${nameHeader}">

				<spring:url
					value="category/administrator/list.do?parentCategoryId=${row.id}"
					var="tripsByCategoryURL" />
				<a href="${tripsByCategoryURL}"><jstl:out value="${row.name}" /></a>

			</display:column>

			<spring:message code="category.parentCategory" var="columnHeader" />
			<display:column property="parentCategory.name" title="${columnHeader}">
			</display:column>


			<spring:message code="category.services" var="columnHeader" />

			<display:column title="${columnHeader}">
				<spring:url
					value="servicio/administrator/list.do?categoryId=${row.id}"
					var="childCategoriesURL" />
				<a href="${childCategoriesURL}"><spring:message
						code="category.services" /></a>
			</display:column>


			<spring:message code="category.edit" var="columnHeader" />
			<display:column title="${columnHeader}">
				<a href="category/administrator/edit.do?categoryId=${row.id}"><spring:message
						code="category.edit" /></a>
			</display:column>


		</jstl:when>

		<jstl:otherwise>

			<acme:column property="name" title="category.name" />

			<spring:message code="category.rendezvous" var="columnRendezvous" />
			<display:column title="${columnRendezvous}">
				<spring:url value="rendezvous/listCategory.do?categoryId=${row.id}"
					var="rendezvousCategory" />
				<a href="${rendezvousCategory}"><spring:message
						code="category.rendezvous" /></a>
			</display:column>

		</jstl:otherwise>
	</jstl:choose>


</display:table>

<security:authorize access="hasRole('ADMINISTRATOR')">
	<a href="category/administrator/create.do"><spring:message
			code="category.create" /></a>
</security:authorize>

<acme:backButton text="msg.back" />
<br />
