<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <jsp:include page="head.jsp" />
    <body>
        <jsp:include page="navigation.jsp" />

        <div class="container">
            <h1>Sie haben noch kein Konto bei uns?</h1>

            <h2>Dann ist es an der Zeit das zu ändern</h2>

            <form method="POST" action="RegistrierungsServlet">
            	<p class="text-danger">${ fehlertyp }</p>
            
                <div class="mb-3">
                    <input type="text" name="vorname" placeholder="Vorname" value="${ vorname }" class="form-control" required>
                </div>

                <div class="mb-3">
                    <input type="text" name="nachname" placeholder="Nachname" value="${ nachname }" class="form-control" required>
                </div>

                <div class="mb-3">
                    <input type="number" name="alter" placeholder="Alter" value="${ alter }" class="form-control"  aria-describedby="alterHinweis" required>
                    <div id="alterHinweis" class="form-text">Sie müssen volljährig sein.</div>
                </div>

                <div class="mb-3">
                    <input type="email" name="email" placeholder="E-Mail Adresse" value="${ email }" class="form-control"  aria-describedby="emailHinweis" required>
                    <div id="emailHinweis" class="form-text">Ihre E-Mail wird nicht an Dritte weitergegeben.</div>
                </div>

                <div class="mb-3">
                    <input type="text" name="bankinstitut" placeholder="Bankinstitut" value="${ bankinstitut }" class="form-control" required>
                </div>

                <div class="mb-3">
                    <input type="password" name="passwort" placeholder="Wählen Sie ein Passwort" class="form-control" required>
                </div>

                <div class="mb-3">
                    <input type="password" name="passwortBestaetigung" placeholder="Passwort wiederholen" class="form-control" required>
                </div>

                <div class="mb-3 form-check">
                    <input type="checkbox" name="bedingungen" class="form-check-input" id="check1" ${ bedingungen } required>
                    <label class="form-check-label" for="bedingungen">Ich akzeptiere die Geschäftsbedingungen</label>
                </div>

                <div class="mb-3 form-check">
                    <input type="checkbox" name="newsletter" class="form-check-input" ${ newsletter } id="check2">
                    <label class="form-check-label" for="newsletter">Ich möchte den Newsletter erhalten (optional)</label>
                </div>

                <button type="submit" class="btn btn-primary">Registrieren</button>
            </form>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
