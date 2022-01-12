<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <jsp:include page="head.jsp" />
    <body>
        <jsp:include page="navigation.jsp" />

        <div class="container">
        
          <!--  Wenn der Kunde NICHT angemeldet ist -->
         <c:if test="${ sessionScope.kunde == null }">
	     	<p>Du bist nicht angemeldet</p>
	     	<p>Bitte <a href="registrierung.jsp">registriere</a> dich zuerst oder <a href="login.jsp">melde dich mit deinem bestehenden Konto an</a></p>
          </c:if>
          
          <!-- Wenn der Kunde angemeldet ist -->
          <c:if test="${ sessionScope.kunde != null }">
          
            <h1>Hallo, ${ sessionScope.kunde.getVorname() } ${ sessionScope.kunde.getNachname() } </h1>

			<h2>Hier können sie weitere Konten anlegen:</h2>

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
            
            <form method="POST" action="KontoServlet">
                <input type="text" name="kontoname" placeholder="Kontoname" />
                <input type="submit" value="Konto anlegen" />
            </form>
            
            <p>Liste ihrer Konten bei uns: </p>
            ${ sessionScope.kontenForm }

			${ sessionScope.showKonto }

			${ sessionScope.kontostand }
        
          </c:if>
          
        </div>
     

        <jsp:include page="footer.jsp" />
    </body>
</html>
