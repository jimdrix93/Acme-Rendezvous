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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table pagesize="5" class="displaytag" name="questions" requestURI="${requestUri}" id="row">
	
	<acme:column property="text" title="question.text"/>
	
	<security:authorize access="hasRole('USER')">
	<display:column>
		<jstl:if test="${showAnswerQuestion}">
		<div>
			<a href="answer/user/create.do?questionId=${row.id}&rendezvousId=${rendezvous}"> 
				<spring:message code="question.answer" />
			</a>
		</div>
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${showEdit}">
		<div>
			<a href="question/user/editForm.do?questionId=${row.id}"> 
				<spring:message code="question.edit.question" />
			</a>
		</div>
		</jstl:if>
	</display:column>

	<display:column>
		<jstl:if test="${showEdit}">
		<div>
			<a href="question/user/delete.do?questionId=${row.id}"> 
				<spring:message code="question.delete" />
			</a>
		</div>
		</jstl:if>
	</display:column>


	</security:authorize>

</display:table>

<acme:backButton text="msg.back" />
<jstl:if test="${showEdit}">
	<acme:button url="question/user/create.do?rendezvousId=${rendezvous}" text="question.new" />
</jstl:if>

