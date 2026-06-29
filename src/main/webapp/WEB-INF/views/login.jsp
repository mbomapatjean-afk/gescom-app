<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Connexion">
    <h2>Bienvenue sur GESCOM</h2>
    <p class="welcome-msg">Veuillez vous identifier</p>
    <p class="error-msg" style="color: red; text-align: center">${error}</p>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">Identifiant :</label>
        <input type="text" id="username" name="username" placeholder="Votre identifiant" value="${nomClient}">

        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" placeholder="Votre mot de passe" value="${emailClient}" required>

        <input type="submit" value="SE CONNECTER">
    </form>

    <form action="createClient" method="get">
        <input type="submit" value="Créer un nouveau compte"  style="background-color: var(--orange); width: 100%;">
    </form>
</t:layout>
