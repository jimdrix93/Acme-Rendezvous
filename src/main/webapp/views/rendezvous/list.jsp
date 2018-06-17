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

<jsp:useBean id="date" class="java.util.Date" />

<display:table pagesize="5" class="displaytag" name="rendezvouses" requestURI="${requestUri}" id="row">

	<jstl:if test="${row.moment < date}">
		<jstl:set var="classTd" value="passed" />
	</jstl:if>
	<jstl:if test="${row.moment > date}">
		<jstl:set var="classTd" value="" />
	</jstl:if>
	<jstl:if test="${row.adult == true}">
		<jstl:set var="classTd" value="${classTd}adult" />
	</jstl:if>
	<jstl:if test="${row.deleted == true}">
		<jstl:set var="classTd" value="deleted" />
	</jstl:if>
	<spring:message code="rendezvous.name" var="rendezvousName" />
	<display:column property="name" title="${rendezvousName}" class="${classTd}"/>
	
	
	<spring:message code="rendezvous.description" var="rendezvousDescription" />
	<display:column property="description" title="${rendezvousDescription}" class="${classTd}"/>

	<spring:message code="moment.format" var="momentFormat" />
	<spring:message code="rendezvous.moment" var="rendezvousMoment" />
	<display:column property="moment" title="${rendezvousMoment}" format="${momentFormat}" class="${classTd}"/>
	
	<spring:message code="rendezvous.picture" var="picture" />
	<display:column title="${picture}" class="${classTd}">
		<IMG src="${row.picture}" class="tableImg"/>
	</display:column>
	
	<spring:message code="rendezvous.location.longitude" var="rendezvousLongitude" />
	<display:column property="location.longitude" title="${rendezvousLongitude}" class="${classTd}"/>
	<spring:message code="rendezvous.location.latitude" var="rendezvousLatitude" />
	<display:column property="location.latitude" title="${rendezvousLatitude}" class="${classTd}"/>
	
	<spring:message code="rendezvous.user" var="rendezvousUser" />
	<display:column title="${rendezvousUser}" class="${classTd}">
		<div>
			<a href="user/display.do?userId=${row.user.id}"> 
				<jstl:out value="${row.user.name}"/>
			</a>
		</div>
	</display:column>
	
	<spring:message code="rendezvous.attendants" var="rendezvousAttendants" />
	<display:column class="${classTd}">
		<div>
			<a href="user/listAttendants.do?rendezvousId=${row.id}"> 
				<spring:message code="rendezvous.attendants" />
			</a>
		</div>
	</display:column>
	
	<spring:message code="rendezvous.announcements" var="rendezvousAnnouncements" />
	<display:column class="${classTd}">
		<div>
			<a href="announcement/list.do?rendezvousId=${row.id}"> 
				<spring:message code="rendezvous.announcements" />
			</a>
		</div>
	</display:column>
	
	<security:authorize access="isAnonymous()">
	<spring:message code="rendezvous.similar" var="rendezvousSimilar" />
	<display:column class="${classTd}">
		<div>
			<a href="rendezvous/listSimilar.do?rendezvousId=${row.id}"> 
				<spring:message code="rendezvous.similar" />
			</a>
		</div>
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
	<spring:message code="rendezvous.similar" var="rendezvousSimilar" />
	<display:column class="${classTd}">
		<div>
			<a href="rendezvous/user/listSimilar.do?rendezvousId=${row.id}"> 
				<spring:message code="rendezvous.similar" />
			</a>
		</div>
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
	<display:column class="${classTd}">
		<div>
			<a href="comment/user/list.do?rendezvousId=${row.id}"> 
				<spring:message code="rendezvous.comments" />
			</a>
		</div>
	</display:column>
	</security:authorize>

	<security:authorize access="hasRole('USER')">
		<display:column class="${classTd}">
		<jsp:useBean id="now" class="java.util.Date"/>
			<jstl:if
				test="${now lt row.moment}">
				<jstl:choose>
				<jstl:when test="${reserved.contains(row) and user ne row.user and !row.deleted}">
					<div>
						<a href="rendezvous/user/cancel.do?rendezvousId=${row.id}"> <spring:message
								code="rendezvous.cancel" />
						</a>
					</div>
				</jstl:when>
				<jstl:when test="${!reserved.contains(row) and user ne row.user and !row.deleted}">
					<div>
						<a href="reservation/user/reserve.do?rendezvousId=${row.id}">
							<spring:message code="rendezvous.reserve" />
						</a>
					</div>
				</jstl:when>
			</jstl:choose>
			</jstl:if>
		</display:column>

		<display:column class="${classTd}">
			<jstl:if
				test="${user == row.user and row.deleted==false and row.draft==true}">
				<div>
					<a href="rendezvous/user/editForm.do?rendezvousId=${row.id}"> <spring:message
							code="rendezvous.edit" />
					</a>
				</div>
			</jstl:if>
		</display:column>
		
		<display:column class="${classTd}">
			<jstl:if test="${user == row.user}">
				<div>
					<a href="announcement/user/create.do?rendezvousId=${row.id}"> <spring:message
							code="rendezvous.announcement.create" />
					</a>
				</div>
			</jstl:if>
		</display:column>

		<display:column class="${classTd}">
			<jstl:if test="${reservedRendezvous}">
				<div>
					<a href="comment/user/create.do?rendezvousId=${row.id}"> <spring:message
							code="rendezvous.comment.write" />
					</a>
				</div>
			</jstl:if>
		</display:column>

		<display:column class="${classTd}">
			<jstl:if test="${user == row.user}">
				<div>
					<a href="link/user/create.do?rendezvousId=${row.id}"> <spring:message
							code="rendezvous.link.create" />
					</a>
				</div>
			</jstl:if>
		</display:column>

		

		<%-- 		<display:column> --%>
		<%-- 			<jstl:if test="${isCreater}"> --%>
		<!-- 			<div> -->
		<%-- 				<a href="link/user/delete.do?linkedToRendezvousId=${row.id}&rendezvousId=${rendezvousId}">  --%>
		<%-- 					<spring:message code="rendezvous.link.delete" /> --%>
		<!-- 				</a> -->
		<!-- 			</div> -->
		<%-- 			</jstl:if> --%>
		<%-- 		</display:column> --%>

		<display:column class="${classTd}">
			<jstl:if test="${isCreater}">
				<div>
					<a href="rendezvous/user/listSimilar.do?rendezvousId=${row.id}">
						<spring:message code="rendezvous.edit.similars" />
					</a>
				</div>
			</jstl:if>
		</display:column>

		<spring:message code="rendezvous.adult" var="adult" />
		<display:column title="${adult}" class="${classTd}">
			<jstl:if test="${(row.adult == true)}">
				<IMG src="images/YES.png" class="adultImg" />
			</jstl:if>
			<jstl:if test="${(row.adult == false)}">
				<IMG src="images/NO.png" class="adultImg" />
			</jstl:if>
		</display:column>
	</security:authorize>

	<spring:message code ="rendezvous.deleted" var="deleted"/>
	<display:column title="${deleted}"  class="${classTd}">
		<jstl:if test="${row.deleted == true }">
			<jstl:out value="${deleted }"/>
		</jstl:if>
	</display:column>

	

	<spring:message code ="rendezvous.passed" var="passed"/>
	<display:column title="${passed}"  class="${classTd}">
		<jstl:if test="${row.moment < date}">
			<jstl:out value="${passed}"/>
		</jstl:if>
	</display:column>
	
	<spring:message code ="rendezvous.canceled" var="cancelado"/>
	<display:column class="${classTd}">
		<jstl:if test="${canceled.contains(row)}">
		<div>
			<jstl:out value="${cancelado}"/>
		</div>
		</jstl:if>
	</display:column>



	<security:authorize access="hasRole('ADMINISTRATOR')">
		<spring:message code="rendezvous.adult" var="adult" />
		<display:column title="${adult}" class="${classTd}">
			<jstl:if test="${(row.adult == true)}">
				<IMG src="images/YES.png" class="adultImg" />
			</jstl:if>
			<jstl:if test="${(row.adult == false)}">
				<IMG src="images/NO.png" class="adultImg" />
			</jstl:if>
		</display:column>
		<display:column class="${classTd}">
			<div>
				<a href="rendezvous/administrator/delete.do?rendezvousId=${row.id}">
					<spring:message code="rendezvous.delete" />
				</a>
			</div>
		</display:column>
	</security:authorize>


	<display:column class="${classTd}">
		<jstl:if test="${user == row.user}">
			<div>
				<a href="question/user/list.do?rendezvousId=${row.id}"> 
					<spring:message code="rendezvous.questions" />
				</a>
			</div>
		</jstl:if>
	</display:column>
	
	<jstl:if test="${delete}">
		<display:column class="${classTd}">
			<div>
				<a href="link/user/delete.do?rendezvousId=${rendezvousId}&linkedToRendezvousId=${row.id}"> 
					<spring:message code="rendezvous.link.delete" />
				</a>
			</div>
		</display:column>
	</jstl:if>

</display:table>

<acme:backButton text="msg.back" />
<br />
	
