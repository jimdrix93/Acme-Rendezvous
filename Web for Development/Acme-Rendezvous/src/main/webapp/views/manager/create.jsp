
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

 	 
<form:form action="manager/edit.do" modelAttribute="manager">	
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="userAccount" />
	<form:hidden path="userAccount.authorities[0].authority" />
	
	
	<acme:textbox code="manager.name" path="name"/>
	<br/>
	
	<acme:textbox code="manager.surname" path="surname"/>
	<br/>
	
	<acme:textbox code="manager.VATnumber" path="VATnumber"/>
	<br />
	
	<acme:textbox code="manager.email" path="email"/>
	<br />
	
	<acme:textbox code="manager.phone" path="phone"/>
	<br />
	
	<acme:textbox code="manager.address" path="address"/>
	<br />
	
	<acme:checkBox code="manager.adult" path="adult"/>
	<br/>
	
	<acme:textbox code="manager.userAccount.username" path="userAccount.username"/>
	<br />
	
	<acme:password code="manager.userAccount.password" path="userAccount.password"/>
	<br />
	<br />
	
	<jstl:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'], 'create')}">
		<p style="color:rgb(120,120,120)"><spring:message code="term.registration"/></p>
		<br />
	</jstl:if>
	
	
	<input type="submit" name="save"
		value='<spring:message code="manager.save"/>' />
		
	<acme:cancel url="/" code="manager.cancel"/>
	
	
</form:form>
	
