
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
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


<p>
	<spring:message code="user.name" var="userName" />
	<jstl:out value="${userName} ${user.name }" />
</p>
<p>
	<spring:message code="user.surname" var="userSurname" />
	<jstl:out value="${userSurname} ${user.surname}" />
</p>

<p>
	<spring:message code="user.email" var="userEmail" />
	<jstl:out value="${userEmail} ${user.email }" />
</p>

<p>
	<spring:message code="user.phone" var="userPhone" />
	<jstl:out value="${userPhone} ${user.phone }" />
</p>

<p>
	<spring:message code="user.address" var="userAddress" />
	<jstl:out value="${userAddress} ${user.address }" />
</p>

<p>
	<spring:message code="user.adult" var="userAdult" />
	<jstl:out value="${userAdult} ${user.adult }" />
</p>


<h4>
	<spring:message code="user.rendezvous" />
</h4>

<display:table name="rendezvouses" class="displaytag" id="row">

	<acme:column property="name" title="rendezvous.name" />
	<br />

	<acme:column property="description" title="rendezvous.description" />
	<br />

	<acme:column property="moment" title="rendezvous.moment"
		format="moment.format" />
	<br />


	<spring:message code="rendezvous.picture" var="picture" />
	<display:column title="${picture}">
		<IMG src="${row.picture}" class="tableImg" />
	</display:column>

	<acme:column property="location.longitude"
		title="rendezvous.location.longitude" />

	<acme:column property="location.latitude"
		title="rendezvous.location.latitude" />

</display:table>
<br />
<br />

<acme:cancel code="user.back" url="/" />




