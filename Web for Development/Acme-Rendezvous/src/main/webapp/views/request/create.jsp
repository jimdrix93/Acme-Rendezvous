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

<form:form action="${requestURI}" modelAttribute="request"  method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="creditCard.id" />
	<form:hidden path="creditCard.version" />


	<acme:textbox code="request.comments" path="comments" />
	<br/>
	
	<acme:select items="${servicios }" itemLabel="name" code="request.servicio" path="servicio"/>
	<br/>
	
	<acme:select items="${rendezvouses }" itemLabel="name" code="request.rendezvous" path="rendezvous"/>
	<br/>
	<br/>
	

	<spring:message code="creditCard.main" />
	<br/>
	<br/>	
	
	<acme:textboxWithValue code="creditCard.holderName" path="creditCard.holderName" value="${holderName}"/>
	<br/>
	
	<acme:textboxWithValue code="creditCard.brandName" path="creditCard.brandName" value="${brandName}"/>
	<br/>
	
	<acme:textboxWithValue code="creditCard.number" path="creditCard.number" value="${number}"/>
	<br/>
	
	<acme:textboxWithValue code="creditCard.expirationMonth" path="creditCard.expirationMonth" value="${expirationMonth}"/>
	<br/>
	
	<acme:textboxWithValue code="creditCard.expirationYear" path="creditCard.expirationYear" value="${expirationYear}"/>
	<br/>
	
	<acme:textboxWithValue code="creditCard.cvv" path="creditCard.cvv" value="${cvv}"/>
	<br/>

	
	<acme:cancel url="/" code="request.cancel"/>
	<acme:submit name="save" code="request.save"/>


</form:form>