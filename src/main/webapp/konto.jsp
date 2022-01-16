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
                <p>Kundenid: ${ sessionScope.kunde.getKundenid() }

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

                <c:if test="${ sessionScope.anlegenErr != null }">
                    <p class="text-danger">${ sessionScope.anlegenErr }</p>
                </c:if>

                <c:if test="${ sessionScope.konten != null }">
                    Liste ihrer Konten bei uns:
                    <form method='POST' action='KontoServlet'>
                        <select name='selectedKonto'>
                            ${ sessionScope.konten }
                        </select>
                        <input type='submit' value='Konto anzeigen'/>
                    </form><br/>

                    <form method='POST' action='MultipartServlet' enctype='multipart/form-data'>
                        <select name='selectedKonto'>
                            ${ sessionScope.konten }
                        </select>
                        <input type='submit' value='CSV hochladen'><input type='file' name='csvFile'/><br>
                        <c:if test="${ sessionScope.csvError != null }">
                            <p class="text-danger">${ sessionScope.csvError }</p>
                        </c:if>
                    </form>
                    <c:if test="${ sessionScope.showKonto != null }">
                        <div class="card konto">
                            <div class="card-body">
                                <h3>Transaktion des Kontos:</h3>
                                ${ sessionScope.showKonto }
                                <c:if test="${ sessionScope.kontostand != null }">
                                    <hr/>
                                    <p>kontostand: ${ sessionScope.kontostand }</p>
                                </c:if>

                                <h1>Kategorie mit Schlagwörtern erstellen: </h1>
                                <p>Gib bitte mindestens ein Schlagwort ein, trenne alle weiteren mit Komma ohne Leerzeichen, z.B, "rewe,edeka,lebensmittel" </p>
                                <p>Tauchen die Schlagwörter im Verwendungszweck einer Transaktion auf, werden diese Posten automatisch der Kategorie zugeordnet </p>

                                <form method="POST" action="KategorienServlet">
                                    <input type="text" name="kategorie" placeholder="Kategorie" />
                                    <input type="text" name="schlagwoerter" placeholder="Schlagwörter" />

                                    <input type="submit" value="Kategorie anlegen" />
                                </form>

                                <h2>Ordnen aller Transaktionen nach einer Kategorie</h2>
                                <p>Wähle dazu eine Kategorie aus, von der alle zugehörigen Posten angezeigt werden sollen. </p>

                                <c:if test="${ sessionScope.kategorienListe.size() < 1 }">
                                    <p> Es wurden noch keine Kategorien angelegt </p>
                                </c:if>

                                <c:if test="${ sessionScope.kategorienListe.size() > 0}">
                                    <form method="POST" action="KategorienServlet">
                                        <select name="kategorie_ausgewaehlt">
                                            <c:forEach var="i" begin="0" end="${sessionScope.kategorienListe.size() - 1}">
                                                <option value="${sessionScope.kategorienListe.get(i)}">
                                                    ${sessionScope.kategorienListe.get(i)}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <button type="submit">Transaktionen anzeigen</button>
                                    </form>
                                </c:if>

                                <c:if test="${ transaktionsFazit != null }">
                                    <p>Hier sind ihre Transaktionen in der Kategorie: </p>
                                    <c:forEach var="i" begin="0" end="${transaktionsFazit.size() - 1}">
                                        <c:if test="${ i < transaktionsFazit.size() - 1}">
                                            <p>${ transaktionsFazit.get(i) }</p>
                                        </c:if>

                                        <c:if test="${ i == transaktionsFazit.size() - 1}">
                                            Summe: ${ transaktionsFazit.get(i) }
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </c:if>
            </c:if>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
