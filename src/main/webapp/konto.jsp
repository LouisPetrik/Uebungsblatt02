<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <jsp:include page="head.jsp" />
    <body>
        <jsp:include page="navigation.jsp" />

        <div class="container">
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
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
