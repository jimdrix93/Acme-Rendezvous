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



<display:table pagesize="5" class="displaytag" name="comments" requestURI="${requestUri}" id="row">

	<acme:column property="text" title="comment.text"/>
	
	<acme:column property="moment" title="comment.moment" format="moment.format"/>
	
	<spring:message code="comment.picture" var="picture" />
	<display:column  title="${picture}">
		<IMG src="${row.picture}" class="tableImg"/>
	</display:column>
	
	<security:authorize access="hasRole('USER')">
	<display:column>
		<div>
			<a href="reply/user/create.do?commentId=${row.id}"> 
				<spring:message code="comment.reply.write" />
			</a>
		</div>
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
	<display:column>
		<div>
			<a href="reply/user/list.do?commentId=${row.id}"> 
				<spring:message code="comment.reply.list" />
			</a>
		</div>
	</display:column>
	</security:authorize>


	<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column>
		<div>
			<a href="reply/administrator/list.do?commentId=${row.id}"> 
				<spring:message code="comment.reply.list" />
			</a>
		</div>
	</display:column>
	<display:column>
			<a href="comment/administrator/delete.do?Id=${row.id}"> <spring:message
					code="comment.delete" />
			</a>
		</display:column>
	</security:authorize>

</display:table>

<acme:backButton text="msg.back" />
<br />
