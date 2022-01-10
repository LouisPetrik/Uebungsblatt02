<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <jsp:include page="head.jsp" />
    <body>
        <jsp:include page="navigation.jsp" />

        <div class="container">
            <h1>Anmeldung in ihr Konto</h1>

            <form method="POST" action="LoginServlet">
                <div class="mb-3">
                    <input type="email" name="email" placeholder="E-Mail Adresse" class="form-control" required>
                </div>

                <div class="mb-3">
                    <input type="password" name="passwort" placeholder="Ihr Passwort" class="form-control" required>
                </div>

                <button type="submit" class="btn btn-primary">Anmelden</button>
            </form>

            <p class="text-danger">${ fehlertyp }</p>

        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
