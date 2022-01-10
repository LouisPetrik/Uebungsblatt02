<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <jsp:include page="head.jsp" />
    <body>
        <jsp:include page="navigation.jsp" />
        <div class="container">
            <h1>Hallo ${ sessionScope.kunde.getVorname() } ${ sessionScope.kunde.getNachname() }</h1>
            <div class="card">
                <div class="card-body">
                    Falls Sie bereits angemeldet sind, erscheint ihre E-Mail hier: ${ sessionScope.kunde.getEmail() }
                </div>
            </div>

            <div class="card">
                <div class="card-body">
                    Bitte <a href="login.jsp">melden Sie sich an</a>, oder <a href="registrierung.jsp">registrieren Sie sich</a>, falls Sie noch kein Konto bei uns besitzen.
                </div>
            </div>
        </div>

        <div class="introbox">
        	<h1>Warum ausgerechnet wir?</h1>
        		<p>
					weit über 450 Jahren vertrauen uns Anleger aller Einkommensklassen und legen ihr hart verdientes Geld bei uns an. <br/>
         		   Wir garantieren Anlangen, die so viel Rendite ausschütten, dass Sie damit sogar fast ihr Geld vor der Inflation schützen. <br/>
           		 Außerdem legen wir besonderen Wert darauf, nicht in ethisch fragwürdige Projekte zu investieren - Wenn Sie genauso wenig verstehen was wir damit meinen wie wir selbst, 
           		 steht ihnen bei uns nichts mehr im Wege! 
           	 </p>
        	</div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
