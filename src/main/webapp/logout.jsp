<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <jsp:include page="head.jsp" />
    <body>
        <jsp:include page="navigation.jsp" />

		<div class="container">
            <div class="card">
            	<h1>Bis zum nächsten Mal, ${ kunde.getVorname() } ${ kunde.getNachname() }!</h1>
                <div class="card-body">
                    <a href="login.jsp">Hier</a> können Sie sich wieder anmelden 
                    oder <a href="registrierung.jsp">registrieren</a>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
