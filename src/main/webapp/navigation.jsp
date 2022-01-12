<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!--  JSP datei für die navgationsleiste in bootstrap -->

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Online Banking</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="index.jsp">Start</a>
                </li>
                
                <!--  Wenn der Kunde NICHT angemeldet ist -->
                <c:if test="${ sessionScope.kunde == null }">
	               	<li class="nav-item">
	                    <a class="nav-link" href="login.jsp">Login</a>
	                </li>
	                <li class="nav-item">
	                    <a class="nav-link" href="registrierung.jsp">Registrierung</a>
	                </li>
                </c:if>
                
                <!--  Wenn der Kunde angemeldet ist -->
                <c:if test="${ sessionScope.kunde != null}">
	                <li class="nav-item">
	                    <a class="nav-link" href="konto.jsp">Konto</a>
	                </li>
	                 <li class="nav-item">
	                    <a class="nav-link" href="logout.jsp">Logout</a>
	                </li>
                </c:if>
                
          
             
            </ul>
            <span class="navbar-text">
            	<c:if test="${ sessionScope.kunde != null }">
            		Hallo, <b>${ sessionScope.kunde.getVorname() }</b>
            	</c:if>
            	<c:if test="${ sessionScope.kunde == null }">
            		Online Bank
            	</c:if>
                
            </span>
        </div>
    </div>
</nav>
