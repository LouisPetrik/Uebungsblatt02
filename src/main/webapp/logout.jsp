<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
    <jsp:include page="head.jsp" />
    <body>
        <jsp:include page="navigation.jsp" />

		<div class="container">
		
			<!-- Diese Card wird angezeigt, wenn der nutzer bereits sich abgemeldet hat, und somit das kundenobjekt schon aus 
			der session entfernt wurde. Dient also nur der verabschiedung. 
			Man kann sich über konto.jsp abmelden oder auch hier über das gleiche form  -->
			<c:if test="${ sessionScope.kunde == null}">
				<div class="card">
	            	<h1>Bis zum nächsten Mal, ${ kunde_vorname } ${ kunde_nachname }!</h1>
	                <div class="card-body">
	                    <a href="login.jsp">Hier</a> können Sie sich wieder anmelden 
	                    oder <a href="registrierung.jsp">registrieren</a>
	                </div>
            	</div>
			</c:if>
     
            
            <!--  Diese Card wird angezeigt, wenn der Nutzer noch nciht abgemeldet ist, damit er sich abmelden kann -->
            <c:if test="${ sessionScope.kunde != null}">
	            <div class="card">
	              <div class="card-body">
	              	<form method="POST" action="LogoutServlet">
	                	Sie sind angemeldet als: ${ sessionScope.kunde.getEmail() }
	                	<input type="submit" value="Ausloggen" />
	                	<!--  Das Form sendet ebenfalls die email des nutzers, der sich abmelden will, damit
	                	er darüber identifiziert und abgemeldet werden kann. Die Email deshalb, weil sie im Gegensatz
	                	zum Namen eines Kunden einzigartig sein MUSS -->
	                	<input type="hidden" name="email" value="${ sessionScope.kunde.getEmail() }" />
	            	</form>
	              </div>
	            </div>
            </c:if>

        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
